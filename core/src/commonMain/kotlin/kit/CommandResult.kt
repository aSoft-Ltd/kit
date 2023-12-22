package kit

data class CommandResult(
    val modules: List<ModuleResult>
) {

    val status
        get() = run {
            if(modules.isEmpty()) {
                return@run ModuleResult.Status.Executing("executing")
            }
            if (modules.all { it.status is ModuleResult.Status.Success }) {
                ModuleResult.Status.Success(emptyList())
            } else if(modules.any { it.status is ModuleResult.Status.Failure }) {
                ModuleResult.Status.Failure(emptyList())
            } else {
                ModuleResult.Status.Executing("executing")
            }
        }
    val percent
        get() = run {
            val done = modules.filterNot {
                it.status is ModuleResult.Status.Executing
            }
            if (modules.isEmpty()) return@run 0
            ((done.size * 100) / modules.size)
        }
}