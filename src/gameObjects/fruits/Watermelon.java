package gameObjects.fruits;

import gameObjects.gameObject.ENUM;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Watermelon extends Fruit {


    public Watermelon() throws IOException {
        BufferedImage bufferedImage[] = Images.getBufferedImage();
        setObjectType(ENUM.Watermelon);

        setBufferedImages(bufferedImage[4]);
        setBufferedImages(bufferedImage[5]);
        setImageView(100, 100);
    }

}
