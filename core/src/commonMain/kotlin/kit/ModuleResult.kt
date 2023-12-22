package kit

data class ModuleResult(
    val module: Module,
    var status: Status
) {
    sealed interface Status {
        data class Executing(val message: String) : Status
        data class Success(val output: List<String>) : Status

        data class Failure(val output: List<String>) : Status
    }
}