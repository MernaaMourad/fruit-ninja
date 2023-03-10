package gameObjects.fruits;

import gameObjects.gameObject.ENUM;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Apple extends Fruit {

    public Apple() throws IOException {
        BufferedImage bufferedImage[] = Images.getBufferedImage();
        setObjectType(ENUM.Apple);
        setBufferedImages(bufferedImage[0]);
        setBufferedImages(bufferedImage[1]);
        setImageView(80, 80);
    }

}
