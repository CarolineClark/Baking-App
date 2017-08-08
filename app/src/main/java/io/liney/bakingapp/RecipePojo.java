package io.liney.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RecipePojo implements Parcelable {
    private int id;
    private String name;
    private List<IngredientPojo> ingredients;
    private List<StepPojo> steps;
    private int servings;
    private String image;

    protected RecipePojo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(IngredientPojo.CREATOR);
        steps = in.createTypedArrayList(StepPojo.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    public static final Creator<RecipePojo> CREATOR = new Creator<RecipePojo>() {
        @Override
        public RecipePojo createFromParcel(Parcel in) {
            return new RecipePojo(in);
        }

        @Override
        public RecipePojo[] newArray(int size) {
            return new RecipePojo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }
}
