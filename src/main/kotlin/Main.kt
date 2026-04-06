package dev.apollointhehouse

import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.execution.ControlUnit
import dev.apollointhehouse.execution.HaltException
import dev.apollointhehouse.execution.Memory
import dev.apollointhehouse.parsing.Parser

const val example = """
        IN N
    
START:  LOAD  ZERO
        COMP  N
        JMPLT END
        
        LOAD  SUM
        ADD   N
        STORE SUM
        
        IN N
        JMP START
        
END:    OUT SUM
        HALT
        
        SUM: 0
        N: 0
        ZERO: 0
"""

fun main() {
    val parser = Parser()
    val memory = Memory(
        memory =  Array(1024) { 0 }
    )

    println("Parsing:")
    val asm = parser.parseASM(example)

    println(asm)
    println(asm.symbolTable)
    println()

    println("Writing Data:")
    for ((name, value) in asm.data) {
        println(name to value to asm.symbolTable[name]!!)
        memory.store(asm.symbolTable[name]!!, value)
    }
    println()

    println("Writing instructions:")
    asm.instructions.forEachIndexed { ptr, (op, addr) ->
        println((op + addr.value).toString(2).padEnd(16, '0'))
        memory.store(Address.Raw(ptr), op + addr.value)
    }
    println()

    println("Starting Execution:")
    val controlUnit = ControlUnit(
        memory = memory,
        debug = true
    )
    try {
        while (true) {
            controlUnit.clock()
        }
    } catch (_: HaltException) {}

    println()
    println("Execution Completed!")
}