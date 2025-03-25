package bg.sofia.uni.fmi.myfitnesspal.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateParserTest {
    @Test
    void testParse_ValidFormats() {
        assertEquals(LocalDate.of(2023, 11, 11), DateParser.parse("2023/11/11"));
        assertEquals(LocalDate.of(2023, 11, 11), DateParser.parse("11-11-2023"));
        assertEquals(LocalDate.of(2023, 11, 11), DateParser.parse("11.11.2023"));
        assertEquals(LocalDate.of(2023, 11, 11), DateParser.parse("November 11, 2023"));
        assertEquals(LocalDate.of(2023, 11, 11), DateParser.parse("11 Nov 2023"));
    }

    @Test
    void testParse_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> DateParser.parse("invalid-date"));
    }
}