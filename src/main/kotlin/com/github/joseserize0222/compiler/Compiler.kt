package com.github.joseserize0222.compiler

import java.io.File

/**
 * Compiler class that translates Hack assembly code into Hack machine code.
 *
 * @property codifier An instance of Codifier responsible for generating binary code from instructions.
 */
class Compiler {
    private val codifier: Codifier = Codifier()
    /**
     * Preprocesses the input code to remove labels and produces a clean list of instructions.
     * Comments are ignored, and labels are added to the symbol table.
     *
     * @param lines A list of assembly code lines to process.
     * @return A list of instructions without labels or comments.
     */
    private fun preprocess(lines: List<String>) : List<String> {
        val program = mutableListOf<String>()
        val regexLabel = Regex("""\((?<name>[_]?[\w][_\w\d.$]*)\)""")
        var lineCounter = 0

        for (line in lines) {
            val newLine = line.trim()
            if (newLine.isEmpty() or newLine.startsWith("//")) // skip comments
                continue
            // Check if it is a Label
            val match = regexLabel.matchEntire(newLine)
            if (match != null) {
                match.groups["name"]?.value?.let { name ->
                    codifier.addLabel(name, lineCounter)
                }
                continue
            }
            // Add the line of code, since it is not empty and, it is not a label
            program.add(newLine)
            lineCounter++
        }
        return program
    }

    /**
     * Compiles the assembly code from the input file to a binary output file.
     *
     * @param output The file path where the machine code will be written.
     * @param input The file path containing the assembly code to be compiled.
     * @throws IllegalArgumentException if an invalid instruction is encountered.
     */
    fun compile(output: String, input: String) {
        val fileInput = File(input)
        require (fileInput.exists()) {
            "The input file does not exist."
        }
        val fileOutput = File(output)
        fileOutput.writeText("")  // cleaning the output file
        val lines = fileInput.readLines()

        val program = preprocess(lines)
        val parser = Parser(program)
        var iterator = program.iterator()
        while (parser.hasNext()) {
            val instruction = parser.processNext()
            val code = codifier.processInstruction(instruction)
            val string = iterator.next()
            require (code != "Invalid instruction") {
                "Error compiling instruction $string, illegal instruction"
            }
            fileOutput.appendText("$code\n", Charsets.UTF_8)
        }
    }
}