package dev.apollointhehouse.asm

data class Instruction<out T : Address>(val opCode: OpCode, val addr: T)