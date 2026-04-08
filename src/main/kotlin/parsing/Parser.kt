package dev.apollointhehouse.parsing

import dev.apollointhehouse.asm.ASM
import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Data
import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.asm.OpCode
import kotlin.collections.map

class Parser {
    private val symbolTable = mutableMapOf<String, Address.Raw>()

    fun parseASM(asm: String): ASM {
        val lines = asm
            .trimIndent()
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val split = lines.indexOfFirst { "HALT" in it } + 1

        val instructions = parseInstructions(lines.subList(0, split))
        val data = parseData(lines.subList(split, lines.size), instructions.size)

        val resolved = instructions.map { (opCode, addr) ->
            Instruction(opCode, addr.resolve(symbolTable))
        }

        return ASM(resolved, data, symbolTable)
    }

    private fun parseInstructions(
        instructions: List<String>
    ): List<Instruction<Address>> {
        val result = instructions.mapIndexed { index, line ->
            val values = line
                .split(" ", ":")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (values.isEmpty()) throw IllegalStateException("No instructions found")

            var start = 0

            if (line.contains(":")) {
                symbolTable[values[start++]] = Address.Raw(index)
            }

            val opCode = OpCode.from(values[start++])

            if (values.size > start) {
                val addr: Address = values[start]
                    .toIntOrNull()
                    ?.let { Address.Raw(it) }
                    ?: Address.Named(values[start])

                return@mapIndexed Instruction(opCode, addr)
            }

            Instruction(opCode, Address.Raw(0))
        }

        return result
    }

    private fun parseData(data: List<String>, memStart: Int): List<Data> = data.mapIndexed { index, line ->
        val (name, value) = line
            .split(":")
            .map { it.trim() }

        val addr =  Address.Raw(memStart + index)
        symbolTable[name] = addr

        Data(name, value.toInt(), addr)
    }
}