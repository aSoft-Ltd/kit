package kit.service

import kit.Module
import kotlinx.coroutines.flow.Flow

interface KitService {

    suspend fun submodules() : List<Module>
    fun status() : Flow<String>
    fun add() : Flow<String>
}