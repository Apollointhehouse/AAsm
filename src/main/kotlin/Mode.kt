package dev.apollointhehouse

enum class Mode(val code: String) {
    Execute("EXECUTE"),
    Assemble("ASSEMBLE");

    companion object {
        fun getByCode(code: String?): Mode = entries.find { it.code.equals(code, ignoreCase = true) } ?: Execute
    }
}