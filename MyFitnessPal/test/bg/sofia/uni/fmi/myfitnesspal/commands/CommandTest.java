package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandTest {

    @Test
    void testDefaultIsExit(){
        Controller controller=mock();
        Scanner sc = mock();
        Command command = new ViewAllLoggedCommand(controller,sc);
        assertFalse(command.isExitCommand());
    }

}