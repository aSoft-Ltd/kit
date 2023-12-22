package kit.proc

actual fun proc(dir: String, vararg command: String): Process = Process(dir, command.toList())