@file:OptIn(ExperimentalForeignApi::class)

package kit.proc

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.posix.realpath

actual fun resolve(path: String): String = realpath(path, null)?.pointed?.ptr?.toKString() ?: path