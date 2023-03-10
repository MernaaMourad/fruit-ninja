package gameObjects.factories;

import java.io.IOException;

public interface IAbstractFactory<T> {
    T create(String type) throws IOException;
}
