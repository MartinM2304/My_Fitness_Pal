package bg.sofia.uni.fmi.myfitnesspal.commands;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ExitCommandTest {
    @Test
    void testExecute_CallsExit() {
        ExitCommand exitCommand = Mockito.spy(new ExitCommand());

        Mockito.doNothing().when(exitCommand).exitApplication();
        Command result = exitCommand.execute();

        assertEquals(exitCommand, result);
        Mockito.verify(exitCommand).exitApplication();
    }

    @Test
    void testIsExitCommand() {
        ExitCommand exitCommand = new ExitCommand();
        assertTrue(exitCommand.isExitCommand());
    }

    @Test
    void testToString() {
        ExitCommand exitCommand = new ExitCommand();
        assertEquals("exit", exitCommand.toString());
    }
}