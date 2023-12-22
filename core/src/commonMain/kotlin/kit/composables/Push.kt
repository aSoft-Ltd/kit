package kit.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.MosaicScope
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import kit.CommandResult
import kit.ModuleImportance
import kit.ModuleResult
import kit.aggregate
import kit.service.KitService

internal suspend fun MosaicScope.Push(
    service: KitService,
    remote: String,
    vararg branches: String
) {
    var state by mutableStateOf(CommandResult(emptyList()))
    setContent {
        Column {
            val status = state.modules.map { it.importance().status }.aggregate()
            Text("${status.toEmoji()} ${state.percent}%")
            state.modules.forEach {
                val importance = it.importance()
                if (importance.show) {
                    Text("${importance.status.toEmoji()} ${it.module.name}: ${importance.text}")
                }
            }
        }
    }
    service.push(remote, *branches).collect { state = it }
}

private fun ModuleResult.importance() = when (val s = status) {
    is ModuleResult.Status.Executing -> ModuleImportance(
        module = module,
        show = false,
        status = status,
        text = "Executing"
    )

    is ModuleResult.Status.Failure -> {
        val text = s.output.joinToString("\n")
        val pseudoSuccess = text.contains("up-to-date") || text.contains("new branch")
        ModuleImportance(
            module = module,
            show = !pseudoSuccess,
            status = if (pseudoSuccess) ModuleResult.Status.Success(
                s.output
            ) else ModuleResult.Status.Failure(s.output),
            text = text
        )
    }

    is ModuleResult.Status.Success -> ModuleImportance(
        module = module,
        show = s.output.getOrNull(1)?.contains("nothing to commit") == false,
        status = status,
        text = s.output.joinToString("\n")
    )
}