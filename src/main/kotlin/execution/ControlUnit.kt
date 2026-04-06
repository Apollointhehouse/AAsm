package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.OpCode
import dev.apollointhehouse.asm.OpCode.*

class ControlUnit(
    private val memory: Memory
) {
    private val programCounter = ProgramCounter(memory)
    private var register: Int = 0
        set(value) {
//            println("Register: $value")
            field = value
        }
    private var cond: Int = 0
    fun clock() {
        val data   =  programCounter.load()
        val opCode =  data and 0xF000
        val addr   = (data and 0x0FFF)

//        println("OP: ${(opCode shr 12).toString(16).padEnd(1, '0')}, ADDR: ${addr.toString(16).padEnd(3, '0')}".uppercase())

        operation(opCode, addr)

        programCounter.clock()
    }

    private fun operation(opCode: Int, addr: Int) {
        when (OpCode.entries[opCode shr 12]) {
            LOAD -> register = memory.load(addr)
            STORE -> memory.store(addr, register)
            CLEAR -> memory.store(addr, 0)
            ADD -> register = (register + memory.load(addr))
            INC -> memory.store(addr, register + 1)
            SUB -> register = (register - memory.load(addr))
            DEC -> memory.store(addr, register - 1)
            COMP -> {
                val x = memory.load(addr)
                when {
                    x >  register -> cond = 0b00
                    x == register -> cond = 0b01
                    x <  register -> cond = 0b10
                }
            }
            JMP -> programCounter.set(addr)
            JMPGT -> {
                if (cond == 0b00) {
                    cond = 0b11
                    programCounter.set(addr)
                }
            }
            JMPEQ -> {
                if (cond == 0b01) {
                    cond = 0b11
                    programCounter.set(addr)
                }
            }
            JMPLT -> {
                if (cond == 0b10) {
                    cond = 0b11
                    programCounter.set(addr)
                }
            }
            JMPNE -> {
                if (cond != 0b01) {
                    cond = 0b11
                    programCounter.set(addr)
                }
            }
            IN -> {
                val input = readln().toInt()
                memory.store(addr, input)
            }
            OUT -> {
                println(memory.load(addr))
            }
            HALT -> throw HaltException()
        }
    }
}