package dev.apollointhehouse

import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.execution.ControlUnit
import dev.apollointhehouse.execution.HaltException
import dev.apollointhehouse.execution.Memory
import dev.apollointhehouse.parsing.Parser

// Fibonacci example
const val example = """
        IN    MAX
START:  OUT   A
        LOAD  B
        STORE TEMP
        ADD   A
        STORE B

        LOAD  TEMP
        STORE A

        COMP  MAX
        JMPLT END

        JMP   START

END:    HALT

        MAX:  0
        TEMP: 0
        A:    0
        B:    1
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

    println("Writing instructions:")
    asm.instructions.forEachIndexed { ptr, (op, addr) ->
        val bin = op + addr.value
        println(bin.toString(2).padEnd(16, '0'))
        memory.store(Address.Raw(ptr), bin)
    }
    println()

    println("Writing Data:")
    for (data in asm.data) {
        val bin = data.value
        println(bin.toString(2).padStart(16, '0'))
        memory.store(data.addr, bin)
    }
    println()

    println("Starting Execution:")
    val controlUnit = ControlUnit(
        memory = memory,
        debug = false
    )
    try {
        while (true) {
            controlUnit.clock()
        }
    } catch (_: HaltException) {}

    println()
    println("Execution Completed!")
}