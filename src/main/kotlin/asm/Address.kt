package dev.apollointhehouse.asm

sealed interface Address {
    data class Raw(val value: Int) : Address {
        override fun resolve(symbolTable: Map<String, Int>): Raw =
            this
    }
    data class Named(val name: String) : Address {
        override fun resolve(symbolTable: Map<String, Int>): Raw =
            Raw(symbolTable[name]!!)
    }

    fun resolve(symbolTable: Map<String, Int>): Raw
}