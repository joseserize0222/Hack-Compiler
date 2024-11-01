package com.github.joseserize0222.compiler

/**
 * Base interface for all assembly instructions.
 */
internal sealed interface Instruction

/**
 * Represents an A-instruction, holding an address or a symbol.
 *
 * @property address The address or symbol for the A-instruction.
 */
internal data class AInstruction(val address: String) : Instruction

/**
 * Represents a C-instruction, consisting of comp, dest, and jump parts.
 *
 * @property comp The computation part of the C-instruction.
 * @property dest The destination part of the C-instruction.
 * @property jump The jump condition of the C-instruction.
 */
internal data class CInstruction(val comp: String, val dest: String, val jump: String) : Instruction