package dev.apollointhehouse.asm

sealed interface Instruction {
    data class Parsed(val opCode: OpCode, val addr: Address) : Instruction
    data class Raw(val bin: Short) : Instruction {
        private val hexFormat = HexFormat {
            upperCase = true
            number {
                removeLeadingZeros = true
                minLength = 4
            }
        }

        override fun toString(): String {
            return "Instruction(bin=${bin.toUShort().toInt().toHexString(format = hexFormat)})"
        }
    }
}