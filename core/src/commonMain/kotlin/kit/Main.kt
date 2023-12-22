package kit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.runMosaicBlocking
import com.jakewharton.mosaic.ui.Text
import kit.composables.Usage
import kit.service.DryRunService
import kit.service.KitCommand
import kit.service.WetRunService
import kotlinx.coroutines.launch

fun main(vararg args: String) = runMosaicBlocking {
    val flags = args.filter { it.startsWith("-") }.toSet()
    val dry = flags.any { it.contains("dry-run") }
    val dir = flags.find { it.contains("dir") }?.split("=")?.lastOrNull() ?: "../.."
    val service = if (dry) DryRunService(dir) else WetRunService(dir)

    when (val command = KitCommand.parse((args.toList() - flags).getOrNull(0))) {
        is KitCommand.Status -> {
            var state by mutableStateOf("running git status")
            setContent {
                Text(state)
            }
            service.status().collect {
                state = it
            }
        }

        is KitCommand.Add -> setContent {
            Text("Using dry? $dry")
            launch { service.add() }
        }

        is KitCommand.Help -> setContent { Usage("Welcome to the help section") }
        is KitCommand.Unknown -> setContent { Usage("Unknown command ${command.name}") }
        null -> setContent { Usage("No command was provided") }
    }
}