package io.liney.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientPojo implements Parcelable {
    private double quantity;
    private String measure;
    private String ingredient;

    protected IngredientPojo(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<IngredientPojo> CREATOR = new Creator<IngredientPojo>() {
        @Override
        public IngredientPojo createFromParcel(Parcel in) {
            return new IngredientPojo(in);
        }

        @Override
        public IngredientPojo[] newArray(int size) {
            return new IngredientPojo[size];
        }
    };

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}
