package kit.proc

import kotlinx.coroutines.future.await
import java.lang.Process as JProcess

actual class Process(val wrapped: JProcess) {
    actual suspend fun await(): Result {
        val p = wrapped.onExit().await()
        val out = p.inputStream.bufferedReader().readLines()
        return if (p.exitValue() != 0) {
            val text = out + p.errorStream.bufferedReader().readLines()
            Result.Failure(text)
        } else {
            Result.Success(out)
        }
    }
}