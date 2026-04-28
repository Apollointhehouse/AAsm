package dev.apollointhehouse

import dev.apollointhehouse.Mode.*
import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.execution.ControlUnit
import dev.apollointhehouse.execution.HaltException
import dev.apollointhehouse.execution.Memory
import dev.apollointhehouse.parsing.IO.readAAEXE
import dev.apollointhehouse.parsing.IO.writeAAEXE
import dev.apollointhehouse.parsing.IO.writeAASM
import dev.apollointhehouse.parsing.Linker
import dev.apollointhehouse.parsing.Parser
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.name
import kotlin.io.path.readText

fun main(args: Array<String>) {
    val iter = args.iterator()
    val mode = Mode.getByCode(iter.nextOr { throw IllegalArgumentException("Must provide a mode!") })

    when (mode) {
        Execute -> {
            val input = Path(iter.nextOr { throw IllegalArgumentException("Must provide input path for aaexe file!") })
            val instructions = readAAEXE(input)
            val debug = iter.nextOr { null }.equals("Debug", true)
            execute(instructions, debug)
        }
        Assemble -> {
            val output = Path(iter.nextOr { throw IllegalArgumentException("Must provide output path for write file!") })
            val parent = output.parent
            val files = args
                .drop(2)
                .map { parent / it }
                .takeIf { it.isNotEmpty() }
                ?: throw IllegalArgumentException("Must provide aasm files for write file!")

            val parsedFiles = files.map { file ->
                val text = file
                    .readText()
                val parser = Parser()

                println()
                println("Parsing ${file.name}:")
                parser.parse(text)
            }

            println()
            println("Linking:")
            val linker = Linker(parsedFiles)
            val instructions = linker.link()

            writeAAEXE(instructions, output)
        }
        Disassemble -> {
            val input = Path(iter.nextOr { throw IllegalArgumentException("Must provide input path for aaexe file!") })
            val instructions = readAAEXE(input)

            writeAASM(instructions, input)
        }
    }
}



private fun execute(instructions: List<Instruction.Raw>, debug: Boolean = false) {
    val memory = Memory(
        memory =  Array(4096) { 0 }
    )

    println("Instructions:")
    println(instructions)
    println()

    val hexFormat = HexFormat {
        upperCase = true
        number {
            removeLeadingZeros = true
            minLength = 4
        }
    }

    println("Writing instructions to memory:")
    instructions.forEachIndexed { ptr, (bin) ->
        println("${":%04d".format(ptr)} : ${bin.toUShort().toInt().toBinString(16)} | ${bin.toUShort().toInt().toHexString(hexFormat)}")
        memory.store(Address.Raw(ptr.toShort()), bin)
    }
    println()

    println("Starting Execution:")
    val controlUnit = ControlUnit(
        memory = memory,
        debug = debug
    )
    try {
        while (true) {
            controlUnit.clock()
        }
    } catch (_: HaltException) {}

    println()
    println("Execution Completed!")
}