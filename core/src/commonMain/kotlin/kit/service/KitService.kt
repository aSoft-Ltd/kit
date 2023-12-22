package kit.service

import kit.CommandResult
import kotlinx.coroutines.flow.Flow

interface KitService {
    suspend fun submodules(): CommandResult
    fun status(): Flow<CommandResult>
    fun add(): Flow<CommandResult>
    fun commit(message: String): Flow<CommandResult>
    fun addCommit(message: String): Flow<CommandResult>

    fun fetch(remote: String, branch: String): Flow<CommandResult>

    fun merge(remote: String, branch: String): Flow<CommandResult>
    fun push(remote: String, vararg branches: String): Flow<CommandResult>
}