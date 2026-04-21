package dev.apollointhehouse.parsing

import dev.apollointhehouse.asm.ASM
import dev.apollointhehouse.asm.Address
import dev.apollointhehouse.asm.Instruction

class Linker(val parsed: List<ASM>) {
    fun link(): List<Instruction.Raw> {
        var p = 0
        val combined = mutableMapOf<String, Address.Raw>()

        for (asm in parsed) {
            val symbolTable = asm.symbolTable.mapValues { (_, address) -> Address.Raw((address.value + p).toShort()) }
            combined.putAll(symbolTable)
            p += asm.instructions.size
        }

        println()
        println("Combined Symbol Table:")
        println(combined)

        val instructions = parsed.flatMap { it.instructions }

        val raw = instructions.map { instr ->
            when (instr) {
                is Instruction.Raw -> instr
                is Instruction.Parsed -> {
                    Instruction.Raw((instr.opCode.code + instr.addr.resolve(combined).value).toShort())
                }
            }
        }

        println()
        println("Raw Instructions:")
        for (intr in raw) {
            println(intr)
        }

        return raw
    }
}