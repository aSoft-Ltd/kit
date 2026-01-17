package kit.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import kit.CommandResult
import kit.ModuleImportance
import kit.ModuleResult
import kit.aggregate
import kit.service.KitService

@Composable
internal fun Fetch(
    service: KitService,
    remote: String,
    branch: String,
    verbose: Boolean
) {
    var state by remember { mutableStateOf(CommandResult(emptyList())) }

    LaunchedEffect(service, remote, branch, verbose) {
        service.fetch(remote, branch).collect { state = it }
    }
    Column {
        val status = state.modules.map { it.importance().status }.aggregate()
        Text("${status.toEmoji()} ${state.percent}%")
        state.modules.forEach {
            val importance = it.importance()
            if (verbose || importance.show) {
                Text("${importance.status.toEmoji()} ${it.module.name}: ${importance.text}")
            }
        }
    }
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
        val pseudoSuccess = text.contains("-> FETCH_HEAD")
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