package gameObjects.fruits;

import gameObjects.factories.IAbstractFactory;

import java.io.IOException;

public class FruitFactory implements IAbstractFactory<Fruit> {

    public Fruit create(String type) throws IOException {
        Fruit fruit;
        if (type.equalsIgnoreCase("Apple"))
            fruit = new Apple();
        else if (type.equalsIgnoreCase("Watermelon"))
            fruit = new Watermelon();
        else if (type.equalsIgnoreCase("Orange"))
            fruit = new Orange();
        else if (type.equalsIgnoreCase("DoubleScoreBanana"))
            fruit = new DoubleScoreBanana();
        else if (type.equalsIgnoreCase("MagicBean")) {
            fruit = new MagicBean();
        } else
            return null;
        return fruit;
    }
}
