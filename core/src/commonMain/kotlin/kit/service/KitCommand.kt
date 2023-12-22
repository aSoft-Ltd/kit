package kit.service

sealed interface KitCommand {
    data object Status : KitCommand
    data object Add : KitCommand
    data object Help : KitCommand
    data class Unknown(val name: String) : KitCommand

    companion object {
        val entries = listOf(Status, Add, Help)
        fun parse(command: String?): KitCommand? {
            if (command == null) return null
            return entries.find {
                command.contains("${it::class.simpleName}", ignoreCase = true)
            } ?: return Unknown(command)
        }
    }
}