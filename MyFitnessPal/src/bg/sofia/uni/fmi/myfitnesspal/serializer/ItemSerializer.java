package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ItemSerializer {
    private final String fileName;
    private final Map<String, Consumable> items;

    public ItemSerializer(String fileName, Map<String, Consumable> items) {
        this.fileName = fileName;
        this.items = items;
    }

    public boolean saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter.Serializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter.Deserializer())
                    .registerTypeAdapter(Consumable.class, new ConsumableTypeAdapter())
                    .create();

            String json = gson.toJson(items);
            writer.write(json);
            System.out.println("Data saved: " + json);
            return true;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }

    public boolean readData() {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            System.out.println("No existing data file found or file is empty.");
            return false;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter.Serializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter.Deserializer())
                    .registerTypeAdapter(Consumable.class, new ConsumableTypeAdapter())
                    .create();

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString().trim();
            if (json.isEmpty() || json.equals("{}")) {
                System.out.println("JSON file is empty or contains only {}.");
                return false;
            }

            Type mapType = new TypeToken<Map<String, Consumable>>() {}.getType();
            Map<String, Consumable> loadedItems = gson.fromJson(json, mapType);

            if (loadedItems == null) {
                System.out.println("Failed to parse JSON.");
                return false;
            }
            items.clear();
            items.putAll(loadedItems);
            System.out.println("Loaded items: " + items);
            return true;
        } catch (IOException io) {
            io.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Consumable> getItems() {
        return items;
    }
}