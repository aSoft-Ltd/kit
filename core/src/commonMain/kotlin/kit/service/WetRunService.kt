package kit.service

import kotlinx.coroutines.flow.flow

class WetRunService(dir: String) : AbstractKitService(dir) {
    override fun add() = flow{
        emit("Starting")
        throw NotImplementedError("Not yet implemented")
    }
}