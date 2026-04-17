package dev.apollointhehouse

import dev.apollointhehouse.asm.Instruction
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

fun Int.toBinString(size: Int = 8): String = this
    .toString(2)
    .padStart(size, '0')

fun readAAEXE(input: Path): List<Instruction.Raw> {
    val instructions = mutableListOf<Instruction.Raw>()
    DataInputStream(input.inputStream()).use { f ->
        val size = f.readUnsignedShort()

        repeat(size) {
            instructions += Instruction.Raw(f.readUnsignedShort())
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
            it.writeShort(bin)
        }
    }
}