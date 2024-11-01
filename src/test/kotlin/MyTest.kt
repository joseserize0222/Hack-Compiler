import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import com.github.joseserize0222.compiler.Compiler

class CompilerTest {
    private val compiler = Compiler()

    @ParameterizedTest
    @ValueSource(strings = ["Add", "Max", "MaxL", "Pong", "PongL", "Rect", "RectL"])  // Añade aquí los nombres de archivo
    fun `test assembly code with different files`(inputFileName: String) {
        val inputFilePath = "src/test/resources/$inputFileName.asm"
        val expectedOutputPath = "src/test/resources/$inputFileName.txt"
        val actualOutputFile = File("src/test/resources/$inputFileName.hack")

        compiler.compile(actualOutputFile.absolutePath, inputFilePath)

        val expectedLines = File(expectedOutputPath).readLines()
        val actualLines = actualOutputFile.readLines()

        assertEquals(expectedLines.size, actualLines.size, "The number of lines doesn't coincide")

        for (i in expectedLines.indices) {
            assertEquals(expectedLines[i], actualLines[i], "Difference in line ${i + 1}")
        }
    }
}
