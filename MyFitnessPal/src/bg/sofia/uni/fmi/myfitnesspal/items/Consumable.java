package bg.sofia.uni.fmi.myfitnesspal.items;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Consumable implements Item, Serializable {
    protected Map<LocalDate, List<Integer>> consumptionLog = new HashMap<>();

    private void validate(LocalDate date, int quantityConsumed) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }
        if (quantityConsumed < 0) {
            throw new IllegalArgumentException("the quantity consumed is too small");
        }
    }

    public void consumpt(LocalDate date, int quantityConsumed) {
        validate(date, quantityConsumed);
        consumptionLog.computeIfAbsent(date, k -> new ArrayList<>()).add(quantityConsumed);
    }

    public Map<LocalDate, List<Integer>> getConsumptionLog() {
        return consumptionLog;
    }

    public int getConsumptionForDateSum(LocalDate date) {
        if (!consumptionLog.containsKey(date)) {
            throw new IllegalArgumentException("you didnt consume any items then");
        }
        List<Integer> consumptionDate = consumptionLog.get(date);
        int consumption = consumptionDate.stream().mapToInt(Integer::intValue).sum();
        return consumption;
    }

    public List<Integer> getConsumptionForDate(LocalDate date) {
        if (!consumptionLog.containsKey(date)) {
            throw new IllegalArgumentException("you didnt consume any items then");
        }
        return consumptionLog.get(date);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<LocalDate, List<Integer>> entry : consumptionLog.entrySet()) {
            LocalDate date = entry.getKey();
            List<Integer> quantities = entry.getValue();

            sb.append(date).append(quantities);
        }

        return sb.toString();
    }
}
