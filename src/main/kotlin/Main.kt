package dev.apollointhehouse

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.path
import dev.apollointhehouse.execution.Utils
import dev.apollointhehouse.parsing.IO.readAAEXE
import dev.apollointhehouse.parsing.IO.writeAAEXE
import dev.apollointhehouse.parsing.IO.writeAASM
import dev.apollointhehouse.parsing.Linker
import dev.apollointhehouse.parsing.Parser
import kotlin.io.path.name
import kotlin.io.path.readText

class Main : CliktCommand() {
    override fun run() = Unit
}

class Execute: CliktCommand() {
    val input by argument(help = "Input Path of .aaexe file").path(mustExist = true)
    val debug by option().boolean().default(false)

    override fun run() {
        val instructions = readAAEXE(input)
        Utils.execute(instructions, debug)
    }
}

class Assemble: CliktCommand() {
    val output by argument(help = "Output path").path(mustExist = false)
    val files by argument(help = "AASM Files").path(mustExist = true).multiple()

    override fun run() {
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
}

class Disassemble : CliktCommand() {
    val input by argument(help = "AASM File Input").path(mustExist = true)

    override fun run() {
        val instructions = readAAEXE(input)

        writeAASM(instructions, input)
    }
}

fun main(args: Array<String>) =
    Main().subcommands(Execute(), Assemble(), Disassemble()).main(args)