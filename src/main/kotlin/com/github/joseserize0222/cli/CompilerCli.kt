package com.github.joseserize0222.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.*
import com.github.joseserize0222.compiler.Compiler
import java.io.File

class CompilerCli : CliktCommand() {
    private val input by option(
        "-i", "--input", help = "Path to the file to compile."
    ).required()
        .validate { path -> processedInput = resolveInputFilePath(path) }

    private val output by option(
        "-o", "--output", help = "Name for the compiled output file."
    ).default("Assembler")
        .validate { path -> processedOutput = resolveOutputFilePath(path) }

    private lateinit var processedInput: String
    private lateinit var processedOutput: String

    override fun run() {
        val compiler = Compiler()
        try {
            compiler.compile(processedOutput, processedInput)
            echo("Compilation successful. Output written to $output")
        } catch (e: IllegalStateException) {
            echo("Error: ${e.message}", err = true)
        } catch (e: Exception) {
            echo("An unexpected error occurred: ${e.localizedMessage}", err = true)
        }
    }

    override fun help(context: Context) = """
        Hack Assembler Compiler CLI

        This tool compiles Hack assembly (.asm) files into binary machine code (.hack).
        Provide an input .asm file and specify an output name (defaulting to 'Assembler.hack' 
        in the current directory if not provided).
        
        Options:
          -i, --input   Specify the input file with .asm extension
          -o, --output  Specify the output file name (without extension), defaults to 'Assembler.hack'

        Developed by Jose Carlos Serize Portela - Version 1.0
    """.trimIndent()

    private fun resolveInputFilePath(path: String): String {
        require(path.endsWith(".asm")) { "Error: The input file must have a .asm extension." }
        val workingDirectory = System.getProperty("user.dir")
        val fileInWorkingDir = File(workingDirectory, path)
        val fileInAbsolutePath = File(path)

        val file = when {
            fileInWorkingDir.exists() -> fileInWorkingDir
            fileInAbsolutePath.exists() -> fileInAbsolutePath
            else -> throw IllegalArgumentException("Error: The specified input file does not exist.")
        }

        require(file.canRead()) { "Error: Cannot read the input file. Check file permissions." }
        return file.absolutePath
    }

    private fun resolveOutputFilePath(path: String): String {
        val workingDirectory = System.getProperty("user.dir")
        val file = File(path)

        val outputFile = if (file.isAbsolute) {
            println("candela")
            file
        } else {
            File(workingDirectory, "$path.hack")
        }

        return outputFile.absolutePath
    }
}
