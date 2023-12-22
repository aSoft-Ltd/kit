package kit.proc

import java.io.File

actual fun proc(dir: String,vararg command: String) : Process {
    val builder = ProcessBuilder(*command).directory(File(dir))
    return Process(builder.start())
}