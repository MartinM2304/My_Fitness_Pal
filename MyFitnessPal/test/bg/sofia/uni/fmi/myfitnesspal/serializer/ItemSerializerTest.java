package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemSerializerTest {
    private ItemSerializer serializer;
    private Map<String, Consumable> items;
    private File fileMock;
    private BufferedWriter writerMock;
    private BufferedReader readerMock;

    @BeforeEach
    void setUp() throws IOException {
        items = new HashMap<>();
        serializer = new ItemSerializer("test.json", items);
        fileMock = mock(File.class);
        writerMock = mock(BufferedWriter.class);
        readerMock = mock(BufferedReader.class);

        serializer = Mockito.spy(new ItemSerializer("test.json", items));
    }

    @Test
    void testSaveData_Success() throws IOException {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).build();
        items.put("apple", food);
        Gson gsonMock = mock(Gson.class);
        when(gsonMock.toJson(items)).thenReturn("{\"apple\":\"mocked\"}");

        try (var bufferedWriter = Mockito.mockStatic(BufferedWriter.class)) {
            bufferedWriter.when(() -> new BufferedWriter(any(FileWriter.class))).thenReturn(writerMock);
            Mockito.doReturn(true).when(serializer).saveData();

            boolean result = serializer.saveData();

            assertTrue(result);
            verify(writerMock).write(anyString());
        }
    }

    @Test
    void testSaveData_IOException() throws IOException {
        doThrow(new IOException("Write failed")).when(writerMock).write(anyString());
        try (var bufferedWriter = Mockito.mockStatic(BufferedWriter.class)) {
            bufferedWriter.when(() -> new BufferedWriter(any(FileWriter.class))).thenReturn(writerMock);

            boolean result = serializer.saveData();

            assertFalse(result);
        }
    }

    @Test
    void testReadData_Success() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.length()).thenReturn(100L);
        when(readerMock.readLine()).thenReturn("{\"apple\":{\"type\":\"Food\",\"name\":\"Apple\"}}").thenReturn(null);
        try (var bufferedReader = Mockito.mockStatic(BufferedReader.class)) {
            bufferedReader.when(() -> new BufferedReader(any(FileReader.class))).thenReturn(readerMock);

            boolean result = serializer.readData();

            assertTrue(result);
            assertEquals(1, items.size());
            assertTrue(items.get("apple") instanceof Food);
        }
    }

    @Test
    void testReadData_FileNotExists() {
        when(fileMock.exists()).thenReturn(false);

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
    }

    @Test
    void testReadData_EmptyFile() {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.length()).thenReturn(0L);

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
    }

    @Test
    void testReadData_EmptyJson() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.length()).thenReturn(100L);
        when(readerMock.readLine()).thenReturn("{}").thenReturn(null);
        try (var bufferedReader = Mockito.mockStatic(BufferedReader.class)) {
            bufferedReader.when(() -> new BufferedReader(any(FileReader.class))).thenReturn(readerMock);

            boolean result = serializer.readData();

            assertFalse(result);
            assertTrue(items.isEmpty());
        }
    }

    @Test
    void testReadData_IOException() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.length()).thenReturn(100L);
        when(readerMock.readLine()).thenThrow(new IOException("Read failed"));
        try (var bufferedReader = Mockito.mockStatic(BufferedReader.class)) {
            bufferedReader.when(() -> new BufferedReader(any(FileReader.class))).thenReturn(readerMock);

            boolean result = serializer.readData();

            assertFalse(result);
        }
    }

    @Test
    void testGetItems() {
        assertEquals(items, serializer.getItems());
    }
}