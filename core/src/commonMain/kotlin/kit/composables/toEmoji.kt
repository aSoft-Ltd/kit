package kit.composables

import kit.ModuleResult

internal fun ModuleResult.Status.toEmoji(): String = when (this) {
    is ModuleResult.Status.Executing -> "🔵"
    is ModuleResult.Status.Failure -> "🔴"
    is ModuleResult.Status.Success -> "🟢"
}