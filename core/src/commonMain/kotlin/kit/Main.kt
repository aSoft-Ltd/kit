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

fun main(vararg args: String) = runMosaicBlocking {
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
        is KitCommand.Commit -> try {
            val message = arguments.getOrNull(1) ?: throw IllegalArgumentException("missing commit message")
            Commit(service, message, verbose)
        } catch (err: Throwable) {
            setContent { Usage(err.message ?: "Unknown error") }
        }

        is KitCommand.AddCommit -> try {
            val message = arguments.getOrNull(1) ?: throw IllegalArgumentException("missing commit message")
            AddCommit(service, message, verbose)
        } catch (err: Throwable) {
            setContent { Usage(err.message ?: "Unknown error") }
        }

        is KitCommand.Fetch -> try {
            val remote = arguments.getOrNull(1) ?: throw IllegalArgumentException("Missing remote (i.e. origin) name")
            val branch = arguments.getOrNull(2) ?: throw IllegalArgumentException("Missing branch name")
            Fetch(service, remote, branch, verbose)
        } catch (err: Throwable) {
            setContent { Usage(err.message ?: "Unknown error") }
        }

        is KitCommand.Merge -> try {
            val remote = arguments.getOrNull(1) ?: throw IllegalArgumentException("Missing remote (i.e. origin) name")
            val branch = arguments.getOrNull(2) ?: throw IllegalArgumentException("Missing branch name")
            Merge(service, remote, branch, verbose)
        } catch (err: Throwable) {
            setContent { Usage(err.message ?: "Unknown error") }
        }

        is KitCommand.Push -> try {
            val remote = arguments.getOrNull(1) ?: throw IllegalArgumentException("Missing remote (i.e. origin) name")
            val branches = arguments.subList(2, arguments.size).toTypedArray()
            if (branches.isEmpty()) throw IllegalArgumentException("Must have at least one branch to push to")
            Push(service, remote, *branches)
        } catch (err: Throwable) {
            setContent { Usage(err.message ?: "Unknown error") }
        }

        is KitCommand.Help -> setContent { Usage("Welcome to the help section") }
        is KitCommand.Unknown -> setContent { Usage("Unknown command ${command.name}") }
        null -> setContent { Usage("No command was provided") }
    }
}