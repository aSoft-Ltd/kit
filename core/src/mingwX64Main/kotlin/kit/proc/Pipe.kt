@file:OptIn(ExperimentalForeignApi::class)

package kit.proc

import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.invoke
import platform.posix.FILE
import platform.posix._pclose
import platform.posix._popen
import platform.posix.popen
import platform.posix.wpopen

actual fun pipeOpen(cmd: String, flags: String) : Any? {
    val p = _popen(cmd, flags)
    return p
}

actual fun pipeClose(pipe: Any?) {
    _pclose(pipe as CValuesRef<FILE>)
}