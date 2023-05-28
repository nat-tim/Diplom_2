package object_api;

import java.util.ArrayList;
import java.util.List;

public class Order {




    private List<String> ingredients = new ArrayList<>();


    public Order(List<String> ingredients) {

        this.ingredients = ingredients;
    }

    public Order() {

    }
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
