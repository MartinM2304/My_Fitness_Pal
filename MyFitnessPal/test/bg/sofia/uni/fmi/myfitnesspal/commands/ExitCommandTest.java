package bg.sofia.uni.fmi.myfitnesspal.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExitCommandTest {

    private ExitCommand command;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        command = new ExitCommand();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testIsExitCommand() {
        assertTrue(command.isExitCommand());
    }

    @Test
    void testToString() {
        assertEquals("exit", command.toString());
    }
}