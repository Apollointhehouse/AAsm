package dev.apollointhehouse.asm

private val hexFormat = HexFormat {
    upperCase = true
    number {
        removeLeadingZeros = true
        minLength = 4
    }
}

data class Instruction(val bin: Int) {
    val opCode: OpCode
        get() = OpCode.from(bin and 0xF000)!!

    val addr: Address.Raw
        get() = Address.Raw(bin and 0x0FFF)

    override fun toString(): String {
        return "Instruction(bin=${bin.toHexString(format = hexFormat)})"
    }
}