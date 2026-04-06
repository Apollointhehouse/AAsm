package dev.apollointhehouse.asm

sealed interface Address {
    data class Raw(val value: Int) : Address {
        override fun resolve(symbolTable: Map<String, Raw>): Raw =
            this

        override fun toString(): String {
            return "Raw(value=${value.toString(16).padEnd(3, '0').uppercase()})"
        }

        operator fun plus(other: Int) = Raw(value + other)
        operator fun plus(other: Raw) = Raw(value + other.value)
    }
    data class Named(val name: String) : Address {
        override fun resolve(symbolTable: Map<String, Raw>): Raw =
            symbolTable[name]!!
    }

    fun resolve(symbolTable: Map<String, Raw>): Raw
}