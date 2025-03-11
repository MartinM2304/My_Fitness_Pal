package bg.sofia.uni.fmi.myfitnesspal.items;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ItemSerializer {
    private final String fileName;
    private Set<Consumable> items;

    public ItemSerializer(String fileName, Set<Consumable> items){
        this.fileName=fileName;
        this.items=items;
    }

    public void saveData(){
        try(BufferedWriter writer= new BufferedWriter(new FileWriter(fileName))){
            Gson gson = new Gson();
            String json = gson.toJson(items);

            writer.write(json);
        }catch (IOException ioException){

        }
    }

    public void readData(){
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(new File(fileName)))){
            Gson gson = new Gson();
            String json = bufferedReader.readLine();

            Type setType = new TypeToken<Set<Consumable>>(){}.getType();
            items = gson.fromJson(json, setType);
        }catch (IOException io){

        }
    }
}
