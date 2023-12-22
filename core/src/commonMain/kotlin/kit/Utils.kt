package kit

internal fun String.safe() = """"${this.replace("\"", "\\\"")}""""