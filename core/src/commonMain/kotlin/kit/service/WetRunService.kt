package kit.service

import kit.CommandResult
import kit.proc.proc
import kit.proc.result
import kit.safe
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WetRunService(dir: String) : AbstractKitService(dir) {

    fun execute(vararg command: String) = flow {
        val result = submodules()
        coroutineScope {
            result.modules.dropLast(1).map {
                it to proc("$dir/${it.module.path}", *command)
            }.map { (module, proc) ->
                module.status = proc.result()
                proc.destroy()
                emit(result)
            }

            val p = proc(dir, *command)
            result.modules.last().status = p.result()
            p.destroy()
            emit(result)
        }
    }

    override fun add() = execute("git", "add", ".")

    override fun commit(message: String) = execute("git", "commit", "-m", message.safe())

    override fun addCommit(message: String): Flow<CommandResult> {
        TODO("Not yet implemented")
    }
}