package com.github.joseserize0222.compiler

/**
 * Codifier class that manages symbol tables and converts assembly instructions to binary machine code.
 *
 * This class handles predefined symbols, variable assignments, and manages A and C instruction processing.
 */
internal class Codifier {
    private val symbolTable = mutableMapOf<String, String>()
    private val compTable = mutableMapOf<String, String>()
    private val destTable = mutableMapOf<String, String>()
    private val jumpTable = mutableMapOf<String, String>()
    private var cursor = 16

    /**
     * Converts an integer value to a 15-bit binary string, padded with leading zeros.
     *
     * @param value The integer value to convert.
     * @return A binary string representing the 15-bit address.
     */
    private fun convertAddress(value: Int): String {
        return value.toString(radix = 2).padStart(15, '0')
    }

    /**
     * Checks if a given string can be interpreted as an integer.
     *
     * @param value The string to check.
     * @return True if the string is a valid integer, false otherwise.
     */
    fun isInteger(value: String): Boolean {
        return value.toIntOrNull() != null
    }

    init {
        // Initialize the predefined symbols
        symbolTable["R0"] = convertAddress(0)
        symbolTable["R1"] = convertAddress(1)
        symbolTable["R2"] = convertAddress(2)
        symbolTable["R3"] = convertAddress(3)
        symbolTable["R4"] = convertAddress(4)
        symbolTable["R5"] = convertAddress(5)
        symbolTable["R6"] = convertAddress(6)
        symbolTable["R7"] = convertAddress(7)
        symbolTable["R8"] = convertAddress(8)
        symbolTable["R9"] = convertAddress(9)
        symbolTable["R10"] = convertAddress(10)
        symbolTable["R11"] = convertAddress(11)
        symbolTable["R12"] = convertAddress(12)
        symbolTable["R13"] = convertAddress(13)
        symbolTable["R14"] = convertAddress(14)
        symbolTable["R15"] = convertAddress(15)
        symbolTable["SCREEN"] = convertAddress(16384)
        symbolTable["KBD"] = convertAddress(24576)
        symbolTable["SP"] = convertAddress(0)
        symbolTable["LCL"] = convertAddress(1)
        symbolTable["ARG"] = convertAddress(2)
        symbolTable["THIS"] = convertAddress(3)
        symbolTable["THAT"] = convertAddress(4)

        // Initialize the comp symbols
        compTable["0"] = "0101010"
        compTable["1"] = "0111111"
        compTable["-1"] = "0111010"
        compTable["D"] = "0001100"
        compTable["A"] = "0110000"
        compTable["!D"] = "0001101"
        compTable["!A"] = "0110001"
        compTable["-D"] = "0001111"
        compTable["-A"] = "0110011"
        compTable["D+1"] = "0011111"
        compTable["A+1"] = "0110111"
        compTable["D-1"] = "0001110"
        compTable["A-1"] = "0110010"
        compTable["D+A"] = "0000010"
        compTable["D-A"] = "0010011"
        compTable["A-D"] = "0000111"
        compTable["D&A"] = "0000000"
        compTable["D|A"] = "0010101"
        compTable["M"] = "1110000"
        compTable["!M"] = "1110001"
        compTable["-M"] = "1110011"
        compTable["M+1"] = "1110111"
        compTable["M-1"] = "1110010"
        compTable["D+M"] = "1000010"
        compTable["D-M"] = "1010011"
        compTable["M-D"] = "1000111"
        compTable["D&M"] = "1000000"
        compTable["D|M"] = "1010101"

        // Initialize the dest symbols
        destTable["null"] = "000"
        destTable["M"] = "001"
        destTable["D"] = "010"
        destTable["MD"] = "011"
        destTable["A"] = "100"
        destTable["AM"] = "101"
        destTable["AD"] = "110"
        destTable["AMD"] = "111"

        // Initialize the jump symbols
        jumpTable["null"] = "000"
        jumpTable["JGT"] = "001"
        jumpTable["JEQ"] = "010"
        jumpTable["JGE"] = "011"
        jumpTable["JLT"] = "100"
        jumpTable["JNE"] = "101"
        jumpTable["JLE"] = "110"
        jumpTable["JMP"] = "111"
    }

    /**
     * Processes an A-instruction by converting an address or a variable to binary.
     *
     * @param instruction The A-instruction to process.
     * @return A 16-bit binary string representing the A-instruction, or null if invalid.
     */
    private fun processAInstruction(instruction: AInstruction) : String? {
        val address =
            if (isInteger(instruction.address)) {
                convertAddress(instruction.address.toInt())
            } else {
                if (!symbolTable.contains(instruction.address)) {
                    symbolTable[instruction.address] = convertAddress(cursor++)
                }
                symbolTable[instruction.address]
            } ?: return null
        return "0$address"
    }

    /**
     * Processes a C-instruction by converting comp, dest, and jump parts to binary.
     *
     * @param instruction The C-instruction to process.
     * @return A 16-bit binary string representing the C-instruction, or null if invalid.
     */
    private fun processCInstruction(instruction: CInstruction) : String? {
        val comp = compTable[instruction.comp] ?: return null
        val dest = destTable[instruction.dest] ?: return null
        val jump = jumpTable[instruction.jump] ?: return null
        return "111$comp$dest$jump"
    }

    /**
     * Adds a label to the symbol table with a given line address if it is not already present.
     *
     * @param labelName The label name to add.
     * @param line The address to associate with the label.
     */
    fun addLabel(labelName: String, line: Int) {
        if (!symbolTable.contains(labelName)) {
            symbolTable[labelName] = convertAddress(line)
        }
    }

    /**
     * Processes an instruction and generates its corresponding binary code.
     *
     * @param instruction The instruction to process.
     * @return A binary string for the instruction, or "Invalid instruction" if unrecognized.
     */
    fun processInstruction(instruction: Instruction) : String {
        return when (instruction) {
            is AInstruction -> processAInstruction(instruction) ?: "Invalid instruction"
            is CInstruction -> processCInstruction(instruction) ?: "Invalid instruction"
        }
    }
}