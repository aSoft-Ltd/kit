package kit.proc

sealed interface Result {
    val output: List<String>
    data class Success(override val output: List<String>) : Result
    data class Failure(override val output: List<String>) : Result
}