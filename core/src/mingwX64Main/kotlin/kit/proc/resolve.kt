@file:OptIn(ExperimentalForeignApi::class)

package kit.proc

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.posix._fullpath
import platform.windows.GetTempPathW
import platform.windows.MAX_PATH
import platform.windows.TCHARVar

actual fun resolve(path: String): String = _fullpath(null, path, 255uL)?.pointed?.ptr?.toKString() ?: path

actual fun tmp(path: String): String = memScoped {
    val buffer = allocArray<TCHARVar>(MAX_PATH)
    GetTempPathW(MAX_PATH.toUInt(), buffer)
    buffer.toKString() + path
}