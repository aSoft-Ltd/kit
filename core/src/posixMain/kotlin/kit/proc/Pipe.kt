@file:OptIn(ExperimentalForeignApi::class)

package kit.proc

import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.FILE
import platform.posix.pclose
import platform.posix.popen

actual fun pipeOpen(cmd: String, flags: String): Any? {
    val p = popen(cmd, flags)
    return p
}

actual fun pipeClose(pipe: Any?) {
    pclose(pipe as CValuesRef<FILE>)
}

actual fun tmp(path: String): String = "/tmp/$path"