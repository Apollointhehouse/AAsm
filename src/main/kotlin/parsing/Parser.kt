package dev.apollointhehouse.parsing

import dev.apollointhehouse.asm.ASM
import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Data
import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.asm.OpCode

class Parser {
    fun parseASM(asm: String): ASM {
        val lines = asm
            .trimIndent()
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val split = lines.indexOf("HALT") + 1

        val instructions = parseInstructions(lines.subList(0, split))
        val data = parseData(lines.subList(split, lines.size))

        return ASM(instructions, data)
    }

    private fun parseInstructions(
        instructions: List<String>
    ): List<Instruction> = instructions.map { line ->
        val values = line
            .split(" ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (values.isEmpty()) throw IllegalStateException("No instructions found")
        val opCode = OpCode.from(values[0])

        if (values.size == 2) {
            val addr: Address = values[1]
                .toIntOrNull()
                ?.let { Address.Raw(it) }
                ?: Address.Named(values[1])

            return@map Instruction(opCode, addr)
        }

        Instruction(opCode, Address.Raw(0))
    }

    private fun parseData(data: List<String>): List<Data> = data.map { line ->
        val (name, value) = line
            .split(":")
            .map { it.trim() }

        Data(name, value.toInt())
    }
}