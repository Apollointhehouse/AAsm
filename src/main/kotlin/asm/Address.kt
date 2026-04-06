package dev.apollointhehouse.asm

sealed interface Address {
    data class Raw(val value: Int) : Address {
        override fun resolve(symbolTable: Map<String, Int>): Int =
            value
    }
    data class Named(val name: String) : Address {
        override fun resolve(symbolTable: Map<String, Int>): Int =
            symbolTable[name]!!
    }

    fun resolve(symbolTable: Map<String, Int>): Int
}