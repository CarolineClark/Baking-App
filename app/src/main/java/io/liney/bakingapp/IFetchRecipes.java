package io.liney.bakingapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IFetchRecipes {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipePojo>> listRecipes();
}
