package bg.sofia.uni.fmi.myfitnesspal.items;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WaterTest {
    @Test
    void testDrink_Success() {
        Water water = new Water();
        LocalDate date = LocalDate.of(2023, 11, 11);

        water.drink(date, 500);

        assertEquals(500, water.getConsumptionForDateSum(date));
    }

    @Test
    void testDrink_NullDate() {
        Water water = new Water();
        assertThrows(IllegalArgumentException.class, () -> water.drink(null, 500));
    }

    @Test
    void testConsumpt_InvalidQuantity() {
        Water water = new Water();
        LocalDate date = LocalDate.of(2023, 11, 11);
        assertThrows(IllegalArgumentException.class, () -> water.consumpt(date, -1));
    }
}