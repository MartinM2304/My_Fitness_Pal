package bg.sofia.uni.fmi.myfitnesspal.items;

public class Food extends Consumable {
    private final String name;
    private final String description;
    private final int servingSize;
    private final int servingsPerContainer;
    private final int calories;
    private final double carbs;
    private final double fat;
    private final double protein;

    private Food(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.servingSize = builder.servingSize;
        this.servingsPerContainer = builder.servingsPerContainer;
        this.calories = builder.calories;
        this.carbs = builder.carbs;
        this.fat = builder.fat;
        this.protein = builder.protein;
    }

    public String getName() {
        return name;
    }

    public int getServingSize() {
        return servingSize;
    }

    public int getCalories() {
        return calories;
    }


    @Override
    public String toString() {
        return String.format("%s (%dg; %d kcal; %.1fg, %.1fg, %.2fg)",
                name, servingSize, calories, carbs, fat, protein);
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public int getServingsPerContainer() {
        return servingsPerContainer;
    }

    public static class Builder {
        private final String name;
        private int servingSize;
        private int servingsPerContainer;
        private int calories;
        private double carbs;
        private double fat;
        private double protein;
        private String description = "";

        public Builder(String name) {
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder servingSize(int servingSize) {
            this.servingSize = servingSize;
            return this;
        }

        public Builder servingsPerContainer(int servingsPerContainer) {
            this.servingsPerContainer = servingsPerContainer;
            return this;
        }

        public Builder calories(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder carbs(double carbs) {
            this.carbs = carbs;
            return this;
        }

        public Builder fat(double fat) {
            this.fat = fat;
            return this;
        }

        public Builder protein(double protein) {
            this.protein = protein;
            return this;
        }

        public Food build() {
            return new Food(this);
        }
    }
}
