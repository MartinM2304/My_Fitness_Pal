package bg.sofia.uni.fmi.myfitnesspal.items;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FoodTest {
    @Test
    void testConsumpt_Success() {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();
        LocalDate date = LocalDate.of(2023, 11, 11);

        food.consumpt(date, 150);

        assertEquals(150, food.getConsumptionForDateSum(date));
        assertEquals(Arrays.asList(150), food.getConsumptionForDate(date));
    }

    @Test
    void testConsumpt_InvalidQuantity() {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();
        LocalDate date = LocalDate.of(2023, 11, 11);

        assertThrows(IllegalArgumentException.class, () -> food.consumpt(date, -1));
    }

    @Test
    void testConsumpt_NullDate() {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();

        assertThrows(IllegalArgumentException.class, () -> food.consumpt(null, 150));
    }

    @Test
    void testGetConsumptionForDateSum_NoConsumption() {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();
        LocalDate date = LocalDate.of(2023, 11, 11);

        assertThrows(IllegalArgumentException.class, () -> food.getConsumptionForDateSum(date));
    }

    @Test
    void testGetConsumptionForDateSum_MultipleEntries() {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();
        LocalDate date = LocalDate.of(2023, 11, 11);
        food.consumpt(date, 100);
        food.consumpt(date, 50);

        assertEquals(150, food.getConsumptionForDateSum(date));
    }

    @Test
    void testBuilderAndGetters() {
        Food food = new Food.Builder("Banana")
                .description("Yellow fruit")
                .servingSize(120)
                .servingsPerContainer(1)
                .calories(89)
                .carbs(23.0)
                .fat(0.3)
                .protein(1.1)
                .build();

        assertEquals("Banana", food.getName());
        assertEquals(120, food.getServingSize());
        assertEquals(1, food.getServingsPerContainer());
        assertEquals(89, food.getCalories());
        assertEquals(23.0, food.getCarbs(), 0.001);
        assertEquals(0.3, food.getFat(), 0.001);
        assertEquals(1.1, food.getProtein(), 0.001);
    }

    @Test
    void testToString() {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();
        assertEquals("Apple (100g; 52 kcal; 14.0g, 0.2g, 0.30g)", food.toString());
    }
}