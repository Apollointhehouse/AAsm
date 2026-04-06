package dev.apollointhehouse.asm

enum class OpCode(val code: Int) {
    LOAD(0x0000),
    STORE(0x1000),
    CLEAR(0x2000),
    ADD(0x3000),
    INC(0x4000),
    SUB(0x5000),
    DEC(0x6000),
    COMP(0x7000),
    JMP(0x8000),
    JMPGT(0x9000),
    JMPEQ(0xA000),
    JMPLT(0xB000),
    JMPNE(0xC000),
    IN(0xD000),
    OUT(0xE000),
    HALT(0xF000);

    infix fun or(addr: Int) = code or addr
    operator fun plus(addr: Int) = code or addr

    companion object {
        fun from(name: String): OpCode =
            entries.first { it.name == name }
    }
}