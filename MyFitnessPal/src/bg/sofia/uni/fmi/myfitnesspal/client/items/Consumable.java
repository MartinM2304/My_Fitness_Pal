package bg.sofia.uni.fmi.myfitnesspal.client.items;

import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.ConsumptionEntry;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Consumable implements Item, Serializable {
    protected Map<LocalDate, List<ConsumptionEntry>> consumptionLog =
            new HashMap<>();

    private void validate(LocalDate date, Object... args) {
        boolean hasNegative = Arrays.stream(args)
                .filter(arg -> arg instanceof Number)
                .map(arg -> ((Number) arg).doubleValue())
                .anyMatch(value -> value < 0);
        if (date == null || hasNegative) {
            throw new IllegalArgumentException("date is null");
        }
    }

    public void consumpt(LocalDate date, Object... args) {
        validate(date, args);
        ConsumptionEntry entry = createConsumptionEntry(args);
        consumptionLog.computeIfAbsent(date, k -> new ArrayList<>()).add(entry);
    }

    protected abstract ConsumptionEntry createConsumptionEntry(Object[] args);

    public Map<LocalDate, List<ConsumptionEntry>> getConsumptionLog() {
        return consumptionLog;
    }

    public int getConsumptionForDateSum(LocalDate date) {
        if (!consumptionLog.containsKey(date)) {
            throw new IllegalArgumentException(
                    "you didnt consume any items then");
        }
        List<ConsumptionEntry> consumptionDate = consumptionLog.get(date);
        return consumptionDate.stream().mapToInt(ConsumptionEntry::getQuantity)
                .sum();
    }

    public List<ConsumptionEntry> getConsumptionForDate(LocalDate date) {
        if (!consumptionLog.containsKey(date)) {
            throw new IllegalArgumentException(
                    "you didnt consume any items then");
        }
        return consumptionLog.get(date);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, List<ConsumptionEntry>> entry
                : consumptionLog.entrySet()) {
            LocalDate date = entry.getKey();
            List<ConsumptionEntry> entries = entry.getValue();
            sb.append(date).append(": ").append(entries).append("\n");
        }
        return sb.toString();
    }
}
