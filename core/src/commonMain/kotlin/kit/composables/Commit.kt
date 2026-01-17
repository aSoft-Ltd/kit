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
import kit.service.KitService

@Composable
internal fun Commit(service: KitService, message: String, verbose: Boolean) {
    var state by remember { mutableStateOf(CommandResult(emptyList())) }
    LaunchedEffect(service, message, verbose) {
        service.commit(message).collect { state = it }
    }
    Column {
        Text("${state.status.toEmoji()} ${state.percent}%")
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

    is ModuleResult.Status.Failure -> ModuleImportance(
        module = module,
        show = true,
        status = status,
        text = s.output.joinToString("\n")
    )

    is ModuleResult.Status.Success -> ModuleImportance(
        module = module,
        status = status,
        show = s.output.getOrNull(1)?.contains("nothing to commit") == false,
        text = s.output.joinToString("\n")
    )
}