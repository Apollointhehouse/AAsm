package dev.apollointhehouse

import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.execution.ControlUnit
import dev.apollointhehouse.execution.HaltException
import dev.apollointhehouse.execution.Memory
import dev.apollointhehouse.parsing.Parser
import kotlin.io.path.Path
import kotlin.io.path.readText

private val hexFormat = HexFormat {
    upperCase = true
    number {
        removeLeadingZeros = true
        minLength = 4
    }
}

fun main(vararg args: String) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("Please provide the location of an AAsm program as an argument")
    }

    val parent = Path(args.first()).parent
    val input = Path(args.first())
        .readText()

    val parser = Parser()
    val memory = Memory(
        memory =  Array(4096) { 0 }
    )

    println("Parsing:")
    println(input)
    val asm = parser.parseASM(input, parent)
    println()

    println("Symbol Table:")
    println(asm.symbolTable)
    println()

    println("Instructions:")
    println(asm)
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