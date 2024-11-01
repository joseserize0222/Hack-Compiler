
# Hack Assembler Compiler

This project is a compiler for the Hack Computer assembly language. It converts assembly code files (.asm) into binary files (.hack) that can be executed on the Hack Computer, as described in the [Nand2Tetris](https://www.nand2tetris.org/) course.

## Requirements

- **Java** 8 or higher
- **Kotlin** 1.8 or higher
- **Gradle** (the `gradlew` wrapper is included in the project)

## Project Structure

- `src/main/kotlin`: Contains the compiler source code.
    - `com/github/joseserize0222/cli/CompilerCli.kt`: Defines the command-line interface (CLI) for the compiler.
    - `com/github/joseserize0222/main/Main.kt`: Main entry point of the program.
- `src/test/kotlin`: Contains unit tests for the compiler.
- `build.gradle.kts`: Gradle configuration file for building and running the project.

## Compiling and Running the Project

### Option 1: Run the Compiler with Gradle

You can run the compiler directly from Gradle using the `gradlew` wrapper.

```bash
./gradlew run --args='-i input.asm -o output'
```

Where:

- `-i input.asm`: Specifies the input assembly file (.asm).
- `-o output`: Specifies the name of the output file (it will be saved as `output.hack` in the current directory).

### Option 2: Build an Executable JAR File

To build an executable "all-in-one" JAR file that includes all dependencies, use the following command:

```bash
./gradlew shadowJar
```

This will generate the JAR file at `build/libs/HackAssemblerCompiler-1.0-SNAPSHOT-all.jar`.

To run the JAR:

```bash
java -jar build/libs/HackAssemblerCompiler-1.0-SNAPSHOT-all.jar -i input.asm -o output
```

### Option 3: Run Tests

To run unit tests, use:

```bash
./gradlew test
```

This command will execute all tests defined in `src/test/kotlin`.

## Usage Example

Example of compiling an `Add.asm` file in the current directory:

1. Using Gradle:

   ```bash
   ./gradlew run --args='-i Add.asm -o Add'
   ```

2. Using the JAR file:

   ```bash
   java -jar build/libs/HackAssemblerCompiler-1.0-SNAPSHOT-all.jar -i Add.asm -o Add
   ```

In both cases, the compiler will generate the `Add.hack` file in the current directory.

## Additional Notes

- Ensure that the input file has the `.asm` extension; otherwise, the compiler will show an error.
- The output file will be saved in the current directory with the `.hack` extension.
