package kit.proc

expect fun pipeOpen(cmd: String,flags: String) : Any?

expect fun pipeClose(pipe: Any?)

expect fun tmp(path: String) : String