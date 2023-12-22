package kit.service

import kit.CommandResult
import kit.proc.proc
import kit.proc.result
import kit.safe
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class WetRunService(dir: String) : AbstractKitService(dir) {

    suspend fun FlowCollector<CommandResult>.executeSubmodules(
        result: CommandResult,
        vararg command: String
    ) {
        result.modules.dropLast(1).map {
            it to proc("$dir/${it.module.path}", *command)
        }.map { (module, proc) ->
            module.status = proc.result()
            proc.destroy()
            emit(result)
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

    fun execute(vararg command: String) = flow {
        val result = submodules()
        executeSubmodules(result, *command)
        executeRoot(result, *command)
    }

    override fun add() = execute("git", "add", ".")

    override fun commit(message: String) = execute("git", "commit", "-m", message.safe())

    override fun addCommit(message: String) = flow {
        val result = submodules()
        val add = arrayOf("git", "add", ".")
        val commit = arrayOf("git", "commit", "-m", "message")
        executeSubmodules(result, *add)
        executeSubmodules(result, *commit)
        executeRoot(result, *add)
        executeRoot(result, *commit)
    }
}