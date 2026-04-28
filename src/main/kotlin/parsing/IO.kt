package dev.apollointhehouse.parsing

import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Instruction
import dev.apollointhehouse.asm.OpCode
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.collections.plusAssign
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.outputStream
import kotlin.io.path.writeText

object IO {
    fun readAAEXE(input: Path): List<Instruction.Raw> {
        val instructions = mutableListOf<Instruction.Raw>()
        DataInputStream(input.inputStream()).use { f ->
            val size = f.readUnsignedShort()

            repeat(size) {
                instructions += Instruction.Raw(f.readUnsignedShort().toShort())
            }
        }

        return instructions
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun writeAAEXE(
        instructions: List<Instruction.Raw>,
        output: Path,
    ) {
        val parent = output.parent
        val name = output.nameWithoutExtension

        val file = parent / Path("$name.aaexe")
        if (!file.exists()) file.createFile()

        DataOutputStream(file.outputStream(StandardOpenOption.TRUNCATE_EXISTING)).use {
            it.writeShort(instructions.size)
            for ((bin) in instructions) {
                it.writeShort(bin.toInt())
            }
        }
    }

    fun writeAASM(
        instructions: List<Instruction.Raw>,
        output: Path
    ) {
        val parent = output.parent
        val name = output.nameWithoutExtension

        val file = parent / Path("$name.aasm")
        if (!file.exists()) file.createFile()

        println("Decoding Instructions:")
        println()
        val decoded = instructions
            .map { decode(it.bin.toInt()) }
            .joinToString("\n") { (opcode, addr) -> "${opcode.name} ${addr.value}" }

        println(decoded)
        file.writeText(decoded)
    }

    private fun decode(data: Int): Pair<OpCode, Address.Raw> {
        val opCodeValue = data and 0xF000
        val opCode = OpCode.entries.find { it.code == opCodeValue } ?: throw IllegalStateException("Unknown OpCode: $opCodeValue")
        val addr   = Address.Raw((data and 0x0FFF).toShort())

        return opCode to addr
    }
}