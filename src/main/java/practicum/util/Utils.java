package practicum.util;

import practicum.Models.IngredientModel;
import practicum.Models.Ingredients;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> getIngredientsId(Ingredients ingredients) {
        List<String> ingredientsId = new ArrayList<>();
        for (IngredientModel ingredient : ingredients.getData()) {
            ingredientsId.add(ingredient.get_id());
        }
        return ingredientsId;
    }

    public static List<String> getRandomElements(int amount, List<String> list) {
        List<String> copyList = new ArrayList<>(list);
        List<String> returnList = new ArrayList<>();
        for (int i = 0; (i < amount) && (i < copyList.size()); i++) {
            int r = (int) Math.floor(Math.random() * (copyList.size() - i)) + i;
            String id = copyList.get(r);
            copyList.set(r, copyList.get(i));
            copyList.set(i, id);
            returnList.add(id);
        }
        return returnList;
    }

    public static String getTokenWithoutBearer(String token){
        return token.split("Bearer ")[1];
    }
}

