package edu.polytech.fridge.ui.fridge;

import java.util.ArrayList;
import java.util.List;

import edu.polytech.fridge.R;
import edu.polytech.fridge.ui.fridge.view.FoodViewModel;

public class FridgeContent {
    private static FridgeContent instance;
    private List<FoodViewModel> foodList = new ArrayList<>();

    private FridgeContent() {

    }

    public static synchronized FridgeContent getInstance() {
        if (instance == null) {
            instance = new FridgeContent();
        }
        return instance;
    }

    public List<FoodViewModel> getFoodList() {
        return foodList;
    }

    public void setFoodList(final List<FoodViewModel> foodList) {
        this.foodList = foodList;
    }

    /**
     * stack same food item
     * @param food Food to add
     * @param quantity Quantity of this food
     */
    public void addFoodOnFridge(final FoodViewModel food, final int quantity) {
        if (isAlreadyHere(food)) {
            foodList.forEach(alreadyIn -> {
                if (isSameFood(food, alreadyIn)) {
                    alreadyIn.setCurrentQuantity(alreadyIn.getCurrentQuantity() + quantity);
                }
                foodList.remove(food); // if present remove it
            });
        } else {
            foodList.add(food);
        }
    }

    public void removeFoodOnFridge(final FoodViewModel food) {
        foodList.remove(food);
    }

    private void clearFridge() {
        foodList.clear();
    }

    /**
     * If a food is already here in the fridge, and have the same expiration date, then stack it
     *
     * @param foodToAdd
     * @return The potential duplicated food
     */
    private boolean isAlreadyHere(FoodViewModel foodToAdd) {
        return foodList.stream().anyMatch(f -> isSameFood(f, foodToAdd));
    }

    public void changeFoodQuantity(FoodViewModel food, int increment) {
        if (food == null || increment == 0) return;
        if (food.getCurrentQuantity() <= 0 && increment < 0) return;
        foodList.forEach(f -> {
            if (f == food) {
                f.setCurrentQuantity(f.getCurrentQuantity() + increment);
            }
        });
        if (food.getCurrentQuantity() == 0) removeFoodOnFridge(food);
    }

    private boolean isSameFood(FoodViewModel f1, FoodViewModel f2) {
        if (f1 == f2) return true;
        return f1.getFoodName().equals(f2.getFoodName())
                && f1.getExpirationDate().equals(f2.getExpirationDate())
                && f1.getFoodImage() == f2.getFoodImage();
    }

    public static void generateFridgeTemplateWithFakeFoods() {
        List<FoodViewModel> foodViewModelList = new ArrayList<>();

        // fetch data from FireBase
        FoodViewModel aliment = new FoodViewModel("Carrot", R.drawable.ic_carrot, "27/04/2022", 4);
        FoodViewModel aliment2 = new FoodViewModel("Pear", R.drawable.ic_pear, "24/04/2022", 2);
        FoodViewModel aliment3 = new FoodViewModel("Pasta", R.drawable.ic_spaghetti, "29/04/2022", 1);
        FoodViewModel aliment4 = new FoodViewModel("Toxic Pasta", R.drawable.ic_spaghetti, "09/04/2017", 1);

        foodViewModelList.add(aliment);
        foodViewModelList.add(aliment2);
        foodViewModelList.add(aliment3);
        foodViewModelList.add(aliment4);
        // return foodViewModelList;
        FridgeContent.getInstance().setFoodList(foodViewModelList);
    }
}
