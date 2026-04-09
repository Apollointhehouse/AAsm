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

        MAX:  .DATA 0
        TEMP: .DATA 0
        A:    .DATA 0
        B:    .DATA 1
"""

fun main() {
    val parser = Parser()
    val memory = Memory(
        memory =  Array(4096) { 0 }
    )

    val hexFormat = HexFormat {
        upperCase = true
        number {
            removeLeadingZeros = true
            minLength = 4
        }
    }

    println("Parsing:")
    val asm = parser.parseASM(example)

    println("Instructions:")
    println(asm)
    println()

    println("Symbol Table:")
    println(asm.symbolTable)
    println()

    println("Writing instructions:")
    asm.instructions.forEachIndexed { ptr, (bin) ->
        println(bin.toBinString(16) + " | " + bin.toHexString(hexFormat))
        memory.store(Address.Raw(ptr), bin)
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