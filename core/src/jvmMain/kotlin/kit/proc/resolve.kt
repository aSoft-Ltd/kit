package kit.proc

import java.io.File

actual fun resolve(path: String): String = File(path).canonicalPath