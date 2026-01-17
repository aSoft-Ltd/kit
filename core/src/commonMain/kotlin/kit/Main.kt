package kit

import com.jakewharton.mosaic.runMosaicBlocking
import kit.composables.Add
import kit.composables.AddCommit
import kit.composables.Commit
import kit.composables.Fetch
import kit.composables.Merge
import kit.composables.Push
import kit.composables.Status
import kit.composables.Usage
import kit.proc.resolve
import kit.service.DryRunService
import kit.service.KitCommand
import kit.service.WetRunService

fun main(vararg args: String) {
    runMosaicBlocking {
        val flags = args.filter { it.startsWith("-") }.toSet()
        val dry = flags.any { it.contains("dry-run") }
        val dir = flags.find { it.contains("dir") }?.split("=")?.lastOrNull() ?: "."
        val verbose = flags.any { it.contains("verbose") }
        val service = if (dry) DryRunService(dir) else WetRunService(dir)

        println("Working on ${resolve(dir)}")

        val arguments = args.toList() - flags

        when (val command = KitCommand.parse(arguments.getOrNull(0))) {
            is KitCommand.Status -> Status(service, verbose)
            is KitCommand.Add -> Add(service, verbose)
            is KitCommand.Commit -> {
                val message = arguments.getOrNull(1) ?: return@runMosaicBlocking Usage("missing commit message")
                Commit(service, message, verbose)
            }

            is KitCommand.AddCommit -> {
                val message = arguments.getOrNull(1) ?: return@runMosaicBlocking Usage("missing commit message")
                AddCommit(service, message, verbose)
            }

            is KitCommand.Fetch -> {
                val remote = arguments.getOrNull(1) ?: return@runMosaicBlocking Usage("Missing remote (i.e. origin) name")
                val branch = arguments.getOrNull(2) ?: return@runMosaicBlocking Usage("Missing branch name")
                Fetch(service, remote, branch, verbose)
            }

            is KitCommand.Merge -> {
                val branch = arguments.getOrNull(1) ?: return@runMosaicBlocking Usage("Missing branch name")
                Merge(service, branch, verbose)
            }

            is KitCommand.Push -> {
                val remote = arguments.getOrNull(1) ?: return@runMosaicBlocking Usage("Missing remote (i.e. origin) name")
                val branches = arguments.subList(2, arguments.size).toTypedArray()
                if (branches.isEmpty()) return@runMosaicBlocking Usage("Must have at least one branch to push to")
                Push(service, remote, *branches)
            }

            is KitCommand.Help -> Usage("Welcome to the help section")
            is KitCommand.Unknown -> Usage("Unknown command ${command.name}")
            null -> Usage("No command was provided")
        }
    }
}