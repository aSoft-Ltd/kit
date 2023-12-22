package kit

import com.jakewharton.mosaic.runMosaicBlocking
import com.jakewharton.mosaic.ui.Text
import kit.composables.Add
import kit.composables.Status
import kit.composables.Usage
import kit.proc.resolve
import kit.service.DryRunService
import kit.service.KitCommand
import kit.service.WetRunService
import kotlinx.coroutines.launch

fun main(vararg args: String) = runMosaicBlocking {
    val flags = args.filter { it.startsWith("-") }.toSet()
    val dry = flags.any { it.contains("dry-run") }
    val dir = flags.find { it.contains("dir") }?.split("=")?.lastOrNull() ?: "../.."
    val verbose = flags.any { it.contains("verbose") }
    val service = if (dry) DryRunService(dir) else WetRunService(dir)

    println("Working on ${resolve(dir)}")

    when (val command = KitCommand.parse((args.toList() - flags).getOrNull(0))) {
        is KitCommand.Status -> Status(service, verbose)
        is KitCommand.Add -> Add(service, verbose)

        is KitCommand.Help -> setContent { Usage("Welcome to the help section") }
        is KitCommand.Unknown -> setContent { Usage("Unknown command ${command.name}") }
        null -> setContent { Usage("No command was provided") }
    }
}