package dev.apollointhehouse.parsing

import dev.apollointhehouse.asm.ASM
import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.asm.OpCode
import kotlin.collections.map

class Parser {
    private val symbolTable = mutableMapOf<String, Address.Raw>()

    fun parseASM(asm: String): ASM {
        val lines = asm
            .split("\n")
            .map { it.substringBefore(";").trim() } // Support comments
            .filter { it.isNotEmpty() }


        val instructions = parseInstructions(lines)

        return ASM(instructions, symbolTable)
    }

    private fun parseInstructions(
        instructions: List<String>
    ): List<Instruction> {
        instructions.forEachIndexed { index, line ->
            val values = line
                .split(" ", ":", ".DATA")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (line.contains(":")) {
                symbolTable[values[0]] = Address.Raw(index)
            }
        }

        val result = instructions.map { line ->
            val values = line
                .split(" ", ":", ".DATA")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (values.isEmpty()) throw IllegalStateException("No instructions found")

            var start = 0
            if (line.contains(":")) {
                start++ // Skip label
            }

            if (line.contains(".DATA")) {
                val data = values[start]
                    .toInt()

                return@map Instruction(data)
            }

            var bin = 0

            if (values.size > start) {
                val value = values[start]
                val opCode = OpCode.from(value)
                    ?: value.toIntOrNull()
                    ?.let { OpCode.from(it) }
                    ?: throw IllegalStateException("No OP code found")

                bin += opCode.code

                start++
            }

            if (values.size > start) {
                val value = values[start]
                val addr = value
                    .toIntOrNull()
                    ?.let { Address.Raw(it) }
                    ?: Address.Named(value)

                bin += addr.resolve(symbolTable).value
            }

            Instruction(bin)
        }

        return result
    }
}