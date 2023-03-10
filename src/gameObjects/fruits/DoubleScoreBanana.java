package gameObjects.fruits;

import gameObjects.gameObject.ENUM;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DoubleScoreBanana extends Fruit {

    public DoubleScoreBanana() throws IOException {
        BufferedImage bufferedImage[] = Images.getBufferedImage();
        setObjectType(ENUM.DoubleScoreBanana);
        setBufferedImages(bufferedImage[10]);
        setBufferedImages(bufferedImage[11]);
        setImageView(100, 100);
    }

}
