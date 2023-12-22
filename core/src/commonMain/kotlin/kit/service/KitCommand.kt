package kit.service

sealed interface KitCommand {
    data object Status : KitCommand
    data object Add : KitCommand
    data object Commit : KitCommand
    data object AddCommit : KitCommand
    data object Help : KitCommand
    data class Unknown(val name: String) : KitCommand

    companion object {
        val entries = listOf(Status, Add, Commit, AddCommit, Help)
        fun parse(command: String?): KitCommand? {
            if (command == null) return null
            if (command == "ac") return AddCommit
            return entries.find {
                command.contains("${it::class.simpleName}", ignoreCase = true)
            } ?: return Unknown(command)
        }
    }
}