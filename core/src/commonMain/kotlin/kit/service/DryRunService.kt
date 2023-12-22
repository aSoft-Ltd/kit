package kit.service

import kit.CommandResult
import kit.proc.proc
import kit.proc.result
import kit.safe
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class DryRunService(dir: String) : AbstractKitService(dir) {

    private fun fake(vararg command: String) = flow {
        val result = submodules()
        val cmd = command.joinToString(" ")
        coroutineScope {
            result.modules.dropLast(1).map {
                it to proc("$dir/${it.module.path}", "echo", cmd)
            }.map { (module, proc) ->
                module.status = proc.result()
                proc.destroy()
                emit(result)
            }

            val p = proc(dir, "echo", cmd)
            result.modules.last().status = p.result()
            p.destroy()
            emit(result)
        }
    }

    override fun add() = fake("git", "add")
    override fun commit(message: String) = fake("git", "commit", message.safe())

    override fun addCommit(message: String) = fake("git", "add", "commit", message)
}