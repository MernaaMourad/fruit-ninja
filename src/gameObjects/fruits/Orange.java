package gameObjects.fruits;

import gameObjects.gameObject.ENUM;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Orange extends Fruit {

    public Orange() throws IOException {
        BufferedImage bufferedImage[] = Images.getBufferedImage();
        setObjectType(ENUM.Orange);

        setBufferedImages(bufferedImage[2]);
        setBufferedImages(bufferedImage[3]);

        setImageView(80, 80);
    }

}
