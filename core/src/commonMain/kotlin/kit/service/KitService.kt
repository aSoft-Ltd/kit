package kit.service

import kit.CommandResult
import kotlinx.coroutines.flow.Flow

interface KitService {
    suspend fun submodules(): CommandResult
    fun status(): Flow<CommandResult>
    fun add(): Flow<CommandResult>
    fun commit(message: String): Flow<CommandResult>
    fun addCommit(message: String): Flow<CommandResult>
}