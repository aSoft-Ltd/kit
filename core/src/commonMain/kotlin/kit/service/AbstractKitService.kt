package kit.service

import kit.Module
import kit.proc.proc
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

abstract class AbstractKitService(protected var dir: String) : KitService {

    override suspend fun submodules(): List<Module> {
        val output = proc(dir, "git", "submodule", "status").await().output
        println("Output")
        output.forEach {
            println(it)
        }
        return 75
    }

    override fun status() = flow {
        emit("getting module count")
        val count = submodules()
        emit("$count modules found")
        emit("running git status")
        var completed = 0
        val started = TimeSource.Monotonic.markNow()
        coroutineScope {
            val jobs = (0..count).map {
                async {
                    delay((1..2).random().seconds)
                    0
                }
            }
            jobs.forEach {
                it.await()
                completed++
                emit("$completed/$count")
            }
        }
        delay(2.seconds)
        emit("done running git status")
        emit("finished in ${started.elapsedNow().inWholeSeconds}seconds")
    }
}