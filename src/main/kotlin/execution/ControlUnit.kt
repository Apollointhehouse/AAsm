package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.OpCode
import dev.apollointhehouse.asm.OpCode.*

private val hexFormatAddr = HexFormat {
    upperCase = true
    number {
        removeLeadingZeros = true
        minLength = 3
    }
}

private val hexFormatReg = HexFormat {
    upperCase = true
    number {
        removeLeadingZeros = true
        minLength = 4
    }
}

class ControlUnit(
    private val memory: Memory,
    private val debug: Boolean = false
) {
    private val programCounter = ProgramCounter(memory)
    private var register: Short = 0
    private var cRegister: Int = 0

    fun clock() {
        val data  = fetch()
        val (opCode, addr) = decode(data.toInt())

        if (debug) println("OP: $opCode, ADDR: ${addr.value.toHexString(hexFormatAddr)}, REG: ${register.toHexString(hexFormatReg)}")

        execute(opCode, addr)

        programCounter.clock()
    }

    private fun fetch(): Short {
        return programCounter.load()
    }

    private fun decode(data: Int): Pair<OpCode, Address.Raw> {
        val opCodeValue = data and 0xF000
        val opCode = OpCode.entries.find { it.code == opCodeValue } ?: throw IllegalStateException("Unknown OpCode: $opCodeValue")
        val addr   = Address.Raw((data and 0x0FFF).toShort())

        return opCode to addr
    }

    private fun execute(opCode: OpCode, addr: Address.Raw) {
        when (opCode) {
            LOAD -> {
                register = memory.load(addr)
            }
            STORE -> {
                memory.store(addr, register)
            }
            CLEAR -> {
                memory.store(addr, 0)
            }
            ADD -> {
                register = (register + memory.load(addr)).toShort()
            }
            INC -> {
                memory.store(addr, (memory.load(addr) + 1).toShort())
            }
            SUB -> {
                register = (register - memory.load(addr)).toShort()
            }
            DEC -> {
                memory.store(addr, (memory.load(addr) - 1).toShort())
            }
            COMP -> {
                val x = memory.load(addr)
                cRegister = when {
                    x >  register -> 0b00
                    x <  register -> 0b10
                    else -> 0b01
                }
            }
            JMP -> programCounter.set(addr)
            JMPGT -> {
                if (cRegister == 0b00) {
                    cRegister = 0b11
                    programCounter.set(addr)
                }
            }
            JMPEQ -> {
                if (cRegister == 0b01) {
                    cRegister = 0b11
                    programCounter.set(addr)
                }
            }
            JMPLT -> {
                if (cRegister == 0b10) {
                    cRegister = 0b11
                    programCounter.set(addr)
                }
            }
            JMPNE -> {
                if (cRegister != 0b01) {
                    cRegister = 0b11
                    programCounter.set(addr)
                }
            }
            IN -> {
                memory.store(addr, readln().toShort())
            }
            OUT -> {
                println(memory.load(addr))
            }
            HALT -> throw HaltException()
        }
    }
}