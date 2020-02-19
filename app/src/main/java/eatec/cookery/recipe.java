package eatec.cookery;

/**
 * Created by Jurdun-PC on 18/02/2020.
 */

class recipe {
    private String recipeID;
    private String recipeName;
    private String recipeDescription;
    private String recipeImage;

    public recipe() {}

    public recipe(String recipeID, String recipeName, String recipeDescription, String recipeImage) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeImage = recipeImage;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public String getRecipeImage() {
        return recipeImage;
    }
}
