package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.OpCode
import dev.apollointhehouse.asm.OpCode.*

class ControlUnit(
    private val memory: Memory,
    private val debug: Boolean = false
) {
    private val programCounter = ProgramCounter(memory)
    private var register: Int = 0
    private var cRegister: Int = 0

    fun clock() {
        val data   =  programCounter.load()
        val opCode = OpCode.entries[(data and 0xF000) shr 12]
        val addr   = (data and 0x0FFF)

        if (debug) println("OP: $opCode, ADDR: ${addr.toString(16).padEnd(3, '0')}".uppercase())

        execute(opCode, addr)

        programCounter.clock()
    }

    private fun execute(opCode: OpCode, addr: Int) {
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
                register = (register + memory.load(addr))
            }
            INC -> {
                memory.store(addr, register + 1)
            }
            SUB -> {
                register = (register - memory.load(addr))
            }
            DEC -> {
                memory.store(addr, register - 1)
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
                memory.store(addr, readln().toInt())
            }
            OUT -> {
                println(memory.load(addr))
            }
            HALT -> throw HaltException()
        }
    }
}