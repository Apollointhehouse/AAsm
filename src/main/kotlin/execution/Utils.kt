package dev.apollointhehouse.execution

import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.toBinString

object Utils {
    fun execute(instructions: List<Instruction.Raw>, debug: Boolean = false) {
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
}