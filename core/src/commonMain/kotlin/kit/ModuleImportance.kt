package kit

data class ModuleImportance(
    val module: Module,
    var show: Boolean,
    val status: ModuleResult.Status,
    val text: String
)