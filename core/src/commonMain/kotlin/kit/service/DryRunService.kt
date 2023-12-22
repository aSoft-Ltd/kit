package kit.service

import kit.safe
import kotlinx.coroutines.flow.flow

class DryRunService(dir: String) : AbstractKitService(dir) {

    private fun fake(vararg command: String) = flow {
        val result = submodules()
        val cmd = arrayOf("echo", command.joinToString(" "))
        executeSubmodules(result, *cmd)
        executeRoot(result, *cmd)
    }

    override fun add() = fake("git", "add")
    override fun commit(message: String) = fake("git", "commit", message.safe())

    override fun addCommit(message: String) = fake("git", "add", "commit", message)

    override fun fetch(remote: String, branch: String) = fake("git", "fetch", remote, branch)
    override fun merge(remote: String, branch: String) = fake("git", "merge", remote, branch)
    override fun push(remote: String, vararg branches: String) = fake("git", "merge", remote, branches.joinToString(","))
}