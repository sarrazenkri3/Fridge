package edu.polytech.fridge.ui.fridge.findfoods;

import androidx.annotation.NonNull;

/**
 * Represent a food item fetched from database
 * Used by the user to add a new item on the fridge
 */
public class FindFoodViewModel {
    private String foodName;
    private int foodImage;

    public FindFoodViewModel(@NonNull final String foodName, final int foodImage) {
        setFoodName(foodName);
        setFoodImage(foodImage);
    }

    public int getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(int foodImage) {
        this.foodImage = foodImage;
    }

    @NonNull
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(@NonNull final String foodName) {
        this.foodName = foodName;
    }
}