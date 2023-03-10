package gameObjects.bombs;

import gameObjects.gameObject.ENUM;
import gameObjects.fruits.Images;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class FatalBomb extends Bomb {

    public FatalBomb() throws IOException {
        BufferedImage bufferedImage[] = Images.getBufferedImage();
        setObjectType(ENUM.FatalBomb);
        setBufferedImages(bufferedImage[8]);
        setBufferedImages(bufferedImage[9]);
        setImageView(100, 100);
    }

}
