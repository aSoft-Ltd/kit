package kit.service

import kit.CommandResult
import kit.Module
import kit.ModuleResult
import kit.proc.proc
import kit.proc.result
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

abstract class AbstractKitService(protected var dir: String) : KitService {

    override suspend fun submodules(): CommandResult {
        val p = proc(dir, "git", "submodule", "status").await().output
        return CommandResult(
            modules = (p.map {
                val name = it.substringBefore(" (").substringAfterLast(" ")
                Module(name)
            } + home).map {
                ModuleResult(
                    module = it,
                    status = ModuleResult.Status.Executing("executing git status")
                )
            }
        )
    }

    val home = Module("root", dir)

    suspend fun FlowCollector<CommandResult>.executeSubmodules(
        result: CommandResult,
        vararg command: String
    ) {
        result.modules.dropLast(1).map {
            it to proc("$dir/${it.module.path}", *command)
        }.map { (module, proc) ->
            module.status = proc.result()
            emit(result)
            proc.destroy()
        }
    }

    suspend fun FlowCollector<CommandResult>.executeRoot(
        result: CommandResult,
        vararg command: String
    ) {
        val p = proc(dir, *command)
        result.modules.last().status = p.result()
        p.destroy()
        emit(result)
    }

    override fun status() = flow {
        val result = submodules()
        val command = arrayOf("git", "status")
        executeSubmodules(result, *command)
        executeRoot(result, *command)
    }
}