package io.liney.bakingapp;

import java.util.List;

public class RecipePojo {
    private int id;
    private String name;
    private List<IngredientPojo> ingredients;
    private List<StepPojo> steps;
    private int servings;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientPojo> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientPojo> ingredient) {
        this.ingredients = ingredient;
    }

    public List<StepPojo> getSteps() {
        return steps;
    }

    public void setSteps(List<StepPojo> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
