package dev.apollointhehouse.asm

data class ASM(
    val instructions: List<Instruction>,
    val symbolTable: Map<String, Address.Raw>
)