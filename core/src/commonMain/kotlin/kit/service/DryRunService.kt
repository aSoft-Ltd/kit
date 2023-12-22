package kit.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class DryRunService(dir: String) : AbstractKitService(dir) {
    override fun add() = flow{
        emit("running git add")
        delay(10.seconds)
        emit("done running git add")
    }
}