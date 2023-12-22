package kit.proc

import kit.ModuleResult

internal suspend fun Process.result() = when (val res = await()) {
    is Result.Failure -> ModuleResult.Status.Failure(res.output)
    is Result.Success -> ModuleResult.Status.Success(res.output)
}