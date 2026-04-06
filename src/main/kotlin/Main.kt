package dev.apollointhehouse

import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.execution.ControlUnit
import dev.apollointhehouse.execution.HaltException
import dev.apollointhehouse.execution.Memory
import dev.apollointhehouse.parsing.Parser

const val example = """
    IN N
    
    LOAD  ZERO
    COMP  N
    JMPLT 9
    
    LOAD  SUM
    ADD   N
    STORE SUM
    
    IN N
    JMP 1
    
    OUT SUM
    HALT
    
    SUM: 0
    N: 0
    ZERO: 0
"""

fun main() {
    val parser = Parser()
    val memory = Memory()

    val asm = parser.parseASM(example)

    val symbolTable = asm.data.associate { (name, value) ->
        name to memory.push(value)
    }

    val instructions = asm.instructions.map { (opCode, addr) ->
        opCode to addr.resolve(symbolTable)
    }

    instructions.fold(0) { ptr, (op, addr) ->
        memory.store(ptr, op + addr.value)
    }

    println("Starting Execution:")
    val controlUnit = ControlUnit(memory, debug = true)
    try {
        while (true) {
            controlUnit.clock()
        }
    } catch (_: HaltException) {
        println()
        println("Execution Completed!")
    }
}