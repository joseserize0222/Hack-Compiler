package com.github.joseserize0222.compiler

/**
 * Parser class that iterates through assembly code and converts each line to an instruction.
 *
 * @property program The list of preprocessed assembly code lines to parse.
 */
internal class Parser(private val program: List<String>) {
    private val iterator = program.iterator()

    /**
     * Retrieves the next instruction from the program.
     *
     * @return An instance of Instruction, either AInstruction or CInstruction.
     */
    fun processNext() : Instruction {
        val line = iterator.next()
        return if (line.startsWith("@")) {
                AInstruction(line.drop(1))
        } else {
            val comp: String
            val dest: String
            val jump: String

            val destCompSplit = line.split("=")
            if (destCompSplit.size == 2) {
                dest = destCompSplit[0].trim()
                val compJumpSplit = destCompSplit[1].split(";")
                comp = compJumpSplit[0].trim()
                jump = if (compJumpSplit.size == 2) compJumpSplit[1].trim() else "null"
            } else {
                dest = "null"
                val compJumpSplit = line.split(";")
                comp = compJumpSplit[0].trim()
                jump = if (compJumpSplit.size == 2) compJumpSplit[1].trim() else "null"
            }
            CInstruction(comp, dest, jump)
        }
    }

    /**
     * Checks if there are more instructions to parse.
     *
     * @return True if there are remaining instructions, false otherwise.
     */
    fun hasNext() = iterator.hasNext()
}