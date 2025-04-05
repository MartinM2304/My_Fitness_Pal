package bg.sofia.uni.fmi.myfitnesspal.items;

public enum MealTime {
    BREAKFAST, LUNCH, SNACKS, DINNER;

    public static MealTime fromString(String mealTimeStr) {
        if (mealTimeStr == null) {
            throw new IllegalArgumentException("Meal time cannot be null.");
        }
        return switch (mealTimeStr.trim().toUpperCase()) {
            case "BREAKFAST" -> BREAKFAST;
            case "LUNCH" -> LUNCH;
            case "SNACKS" -> SNACKS;
            case "DINNER" -> DINNER;
            default -> throw new IllegalArgumentException("Invalid meal time: " + mealTimeStr);
        };
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}