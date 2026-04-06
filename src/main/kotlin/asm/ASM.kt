package dev.apollointhehouse.asm

data class ASM(
    val instructions: List<Instruction<Address.Raw>>,
    val data: List<Data>,
    val symbolTable: Map<String, Address.Raw>
)