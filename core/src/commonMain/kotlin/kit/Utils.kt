package kit

internal fun String.safe() = """"${this.replace("\"", "\\\"")}""""

internal fun List<ModuleResult.Status>.aggregate(): ModuleResult.Status {
    if (isEmpty()) {
        return ModuleResult.Status.Executing("executing")
    }
    return if (all { it is ModuleResult.Status.Success }) {
        ModuleResult.Status.Success(emptyList())
    } else if (any { it is ModuleResult.Status.Failure }) {
        ModuleResult.Status.Failure(emptyList())
    } else {
        ModuleResult.Status.Executing("executing")
    }
}