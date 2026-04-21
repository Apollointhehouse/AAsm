package dev.apollointhehouse.parsing

import dev.apollointhehouse.asm.ASM
import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.asm.OpCode

class Parser {
    private val symbolTable = mutableMapOf<String, Address.Raw>()

    fun parse(asm: String): ASM {
        println()
        println("Preprocessing ASM:")
        val lines = preprocess(asm)
        println(lines.joinToString("\n"))

        val instructions = parseInstructions(lines)

        return ASM(instructions, symbolTable)
    }

    private fun preprocess(asm: String): List<String> {
        return asm
            .split("\n")
            .map { it.substringBefore(";").trim() }
            .filter { it.isNotEmpty() }
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
                symbolTable[values[0]] = Address.Raw(index.toShort())
            }
        }
        println()
        println("Symbol Table:")
        println(symbolTable)

        val result = instructions.mapIndexed { index, line ->
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

                try {
                    return@mapIndexed Instruction.Raw(data.hexToShort())
                } catch (_: Exception) {
                    throw IllegalStateException("Failed to parse hex at lineL $index}")
                }
            }

            var opCode = OpCode.LOAD

            if (values.size > start) {
                val value = values[start]
                opCode = OpCode.from(value)
                    ?: try {
                        OpCode.from(value.hexToShort().toInt())!!
                    } catch (_: Exception) {
                        throw IllegalStateException("No OP code found at line: $index")
                    }

                start++
            }

            var addr: Address = Address.Raw(0)

            if (values.size > start) {
                val value = values[start]
                addr = value
                    .toShortOrNull()
                    ?.let { Address.Raw(it) }
                    ?: Address.Named(value)
            }

            Instruction.Parsed(opCode, addr)
        }

        return result
    }
}