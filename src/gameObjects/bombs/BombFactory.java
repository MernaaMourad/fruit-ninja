package gameObjects.bombs;

import gameObjects.factories.IAbstractFactory;

import java.io.IOException;

public class BombFactory implements IAbstractFactory<Bomb> {

    public Bomb create(String type) throws IOException {
        Bomb bomb;
        if (type.equalsIgnoreCase("Fatal")) {
            bomb = new FatalBomb();
        } else if (type.equalsIgnoreCase("Dangerous")) {
            bomb = new DangerousBomb();
        } else {
            return null;
        }
        return bomb;
    }

}
