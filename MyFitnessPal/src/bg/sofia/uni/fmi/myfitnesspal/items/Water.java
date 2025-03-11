package bg.sofia.uni.fmi.myfitnesspal.items;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Water extends Consumable{

    public void drink(LocalDate date, int quantity){
        if(date==null){
            throw new IllegalArgumentException("date is null");
        }
        consumpt(date,quantity);
    }
}
