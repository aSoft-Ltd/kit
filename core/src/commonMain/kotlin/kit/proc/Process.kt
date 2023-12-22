package kit.proc

expect class Process {
    suspend fun await() : Result
}