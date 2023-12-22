package kit.service

import kit.CommandResult
import kit.Module
import kit.ModuleResult
import kit.proc.Result
import kit.proc.proc
import kit.proc.result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

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

    override fun status() = flow {
        val result = submodules()
        coroutineScope {
            result.modules.dropLast(1).map {
                it to proc("$dir/${it.module.path}", "git", "status")
            }.map { (module, proc) ->
                module.status = proc.result()
                proc.destroy()
                emit(result)
            }

            val p = proc(dir, "git", "status")
            result.modules.last().status = p.result()
            p.destroy()
            emit(result)
        }
    }
}