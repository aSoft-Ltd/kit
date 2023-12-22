package kit.service

import kit.proc.proc
import kit.proc.result
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class WetRunService(dir: String) : AbstractKitService(dir) {
    override fun add() = flow {
        val result = submodules()
        coroutineScope {
            result.modules.dropLast(1).map {
                it to proc("$dir/${it.module.path}", "git", "add", ".")
            }.map { (module, proc) ->
                module.status = proc.result()
                proc.destroy()
                emit(result)
            }

            val p = proc(dir, "git", "add", ".")
            result.modules.last().status = p.result()
            p.destroy()
            emit(result)
        }
    }
}