package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemSerializerTest {

    private ItemSerializer serializer;
    private Map<String, Consumable> items;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        items = new HashMap<>();
        tempFile = Files.createTempFile("test", ".json");
        serializer = new ItemSerializer(tempFile.toString(), items);
    }

    @Test
    void testSaveData_Success() {
        items.put("apple", new Food.Builder("Apple").servingSize(100).calories(52).build());

        boolean result = serializer.saveData();

        assertTrue(result);
        assertTrue(Files.exists(tempFile));
    }

    @Test
    void testSaveData_IOException() throws IOException {
        Files.delete(tempFile);
        Files.createDirectory(tempFile);

        boolean result = serializer.saveData();

        assertFalse(result);
    }

    @Test
    void testReadData_Success() throws IOException {
        items.put("apple", new Food.Builder("Apple").servingSize(100).calories(52).build());
        serializer.saveData();

        items.clear();
        boolean result = serializer.readData();

        assertTrue(result);
        assertEquals(1, items.size());
        assertTrue(items.containsKey("apple"));
        assertTrue(items.get("apple") instanceof Food);
    }

    @Test
    void testReadData_FileNotExists() {
        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
    }

    @Test
    void testReadData_EmptyFile() throws IOException {
        Files.write(tempFile, "".getBytes());

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
    }

    @Test
    void testReadData_EmptyJson() throws IOException {
        Files.write(tempFile, "{}".getBytes());

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
    }

    @Test
    void testReadData_IOException() throws IOException {
        Files.write(tempFile, "not a json".getBytes());
        tempFile.toFile().setReadOnly();

        boolean result = serializer.readData();

        assertFalse(result);
    }

    @Test
    void testReadData_NullLoadedItems() throws IOException {
        Files.write(tempFile, "{invalid json}".getBytes());

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
    }

    @Test
    void testReadData_GenericException() throws IOException {
        Files.write(tempFile, "{\"apple\": {\"type\": \"InvalidType\"}}".getBytes());

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
    }

    @Test
    void testGetItems() {
        assertEquals(items, serializer.getItems());
    }
}