package kit

data class CommandResult(
    val modules: List<ModuleResult>
) {

    val status get() = modules.map { it.status }.aggregate()
    val percent
        get() = run {
            val done = modules.filterNot {
                it.status is ModuleResult.Status.Executing
            }
            if (modules.isEmpty()) return@run 0
            ((done.size * 100) / modules.size)
        }
}