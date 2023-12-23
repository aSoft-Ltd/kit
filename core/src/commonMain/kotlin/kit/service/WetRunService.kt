package kit.service

import kit.CommandResult
import kit.safe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WetRunService(dir: String) : AbstractKitService(dir) {

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
        val commit = arrayOf("git", "commit", "-m", message.safe())
        executeSubmodules(result, *add)
        executeSubmodules(result, *commit)
        executeRoot(result, *add)
        executeRoot(result, *commit)
    }

    override fun fetch(remote: String, branch: String) = execute("git", "fetch", remote, branch)
    override fun merge(branch: String) = execute("git", "merge", branch)
    override fun push(remote: String, vararg branches: String): Flow<CommandResult> = execute("git", "push", remote, *branches)
}