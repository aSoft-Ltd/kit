plugins {
    alias(kotlinz.plugins.multiplatform) apply false
    alias(kotlinz.plugins.serialization) apply false
    alias(kotlinz.plugins.root.compiler.compose) apply false
    alias(asoft.plugins.library) apply false
}

repositories {
    publicRepos()
}