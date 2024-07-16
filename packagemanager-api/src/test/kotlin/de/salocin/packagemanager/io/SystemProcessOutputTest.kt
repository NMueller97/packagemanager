package de.salocin.packagemanager.io

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import java.io.InputStream
import java.io.PrintStream
import kotlin.test.Test

class SystemProcessOutputTest {

    private lateinit var inputStream: InputStream
    private lateinit var outputParser: OutputParser<String>
    private lateinit var outputPipe: PrintStream
    private lateinit var output: SystemProcessOutput<String>

    @BeforeEach
    fun setUp() {
        inputStream = mockk()
        outputParser = mockk()
        outputPipe = mockk()
        output = SystemProcessOutput(inputStream, outputParser, outputPipe)
    }

    @Test
    fun `tryRead should return empty list when no bytes are available`() {
        every { inputStream.available() } returns 0
        val result = output.tryRead()
        assert(result.isEmpty())
    }

    @Test
    fun `tryRead should process available bytes correctly`() {
        val testBytes = "line1\nline2\n".toByteArray()
        every { inputStream.available() } returnsMany listOf(testBytes.size, 0)
        every { inputStream.readNBytes(any()) } returns testBytes
        every { outputParser.parseLine("line1") } returns "parsedLine1"
        every { outputParser.parseLine("line2") } returns "parsedLine2"
        every { outputPipe.print(any<Char>()) } returns Unit

        val result = output.tryRead()

        assertEquals(listOf("parsedLine1", "parsedLine2"), result)
    }

    @Test
    fun `tryRead with multiple read operations should process available bytes correctly`() {
        val testBytes1 = "line1\nline2\n".toByteArray()
        val testBytes2 = "line3\nline4\n".toByteArray()
        every { inputStream.available() } returns testBytes1.size andThen testBytes2.size andThen 0
        every { inputStream.readNBytes(any()) } returns testBytes1 andThen testBytes2
        every { outputParser.parseLine("line1") } returns "parsedLine1"
        every { outputParser.parseLine("line2") } returns "parsedLine2"
        every { outputParser.parseLine("line3") } returns "parsedLine3"
        every { outputParser.parseLine("line4") } returns "parsedLine4"
        every { outputPipe.print(any<Char>()) } returns Unit

        val result = output.tryRead()

        assertEquals(listOf("parsedLine1", "parsedLine2", "parsedLine3", "parsedLine4"), result)
    }


    @Test
    fun `readRemaining should return parsed lines after end of stream`() {
        val testBytes = "line1\nline2\n".toByteArray()
        every { outputParser.parseLine("line1") } returns "parsedLine1"
        every { outputParser.parseLine("line2") } returns "parsedLine2"
        every { inputStream.readAllBytes() } returns testBytes
        every { outputPipe.print(any<Char>()) } returns Unit

        val result = output.readRemaining()

        assertEquals(listOf("parsedLine1", "parsedLine2"), result)
    }

    @Test
    fun `outputPipe should receive correct output`() {
        val testBytes = "line1\n".toByteArray()
        every { inputStream.available() } returnsMany listOf(testBytes.size, 0)
        every { inputStream.readNBytes(any()) } returns testBytes
        every { outputParser.parseLine("line1") } returns "parsedLine1"
        every { outputPipe.print(any<Char>()) } returns Unit

        output.tryRead()

        verify { outputPipe.print('l') }
        verify { outputPipe.print('i') }
        verify { outputPipe.print('n') }
        verify { outputPipe.print('e') }
        verify { outputPipe.print('1') }
        verify { outputPipe.print('\n') }
    }

}