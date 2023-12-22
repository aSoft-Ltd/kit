@file:OptIn(ExperimentalForeignApi::class)

package kit.proc

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.free
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.posix.FILE
import platform.posix.WEXITED
import platform.posix.ferror
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.fread
import platform.posix.getenv
import platform.posix.malloc
import platform.posix.pclose
import platform.posix.popen
import platform.posix.system

actual class Process(private val dir: String, val command: List<String>) {

    private val id = ++count

    companion object {
        var count = 0
    }

    private val out = "/tmp/kit.$id.out.txt"
    private val err = "/tmp/kit.$id.err.txt"
    actual suspend fun await(): Result {
        val cmd = "cd $dir && ${command.joinToString(" ")} 1>$out 2>$err"
        val pipe = popen(cmd, "r") ?: throw RuntimeException("popen() failed")
        pclose(pipe)
        val success = read(out)
        val failure = read(err)

        return if (failure.size > 1) {
            Result.Failure(success + failure)
        } else {
            Result.Success(success)
        }
    }

    private fun read(path: String): List<String> {
        val MAX_BUFFER = 1024 * 10
        val buffer = nativeHeap.allocArray<ByteVar>(MAX_BUFFER)
        val file = fopen(path, "r") ?: return emptyList()
        var res = ""
        try {
            while (fgets(buffer.pointed.ptr, MAX_BUFFER, file) != null) {
                res += buffer.pointed.ptr.toKString()
            }
            if (ferror(file) != 0) throw RuntimeException("error reading from $path")
        } catch (err: Throwable) {
            throw err
        } finally {
            pclose(file)
        }
        nativeHeap.free(buffer)
        return res.split("\n")
    }

    actual suspend fun destroy() {

    }
}