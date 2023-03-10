package gameObjects.factories;

import gameObjects.bombs.BombFactory;
import gameObjects.fruits.FruitFactory;

public class FactoryProvider {

    private static FactoryProvider factoryProvider = null;

    private FactoryProvider() {

    }

    public static FactoryProvider getInstance() {
        if (factoryProvider == null) {
            return factoryProvider = new FactoryProvider();
        } else {
            return factoryProvider;
        }
    }

    public IAbstractFactory create(String factoryType) {

        IAbstractFactory factoryOfFactories;
        if (factoryType.equalsIgnoreCase("fruit")) {
            factoryOfFactories = new FruitFactory();
        } else if (factoryType.equalsIgnoreCase("bomb")) {
            factoryOfFactories = new BombFactory();
        } else {
            return null;
        }
        return factoryOfFactories;
    }

}
