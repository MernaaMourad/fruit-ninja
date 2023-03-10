package gameObjects.bombs;

import gameObjects.gameObject.ENUM;
import gameObjects.fruits.Images;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DangerousBomb extends Bomb {

    public DangerousBomb() throws IOException {
        BufferedImage bufferedImage[] = Images.getBufferedImage();
        setObjectType(ENUM.DangerousBomb);
        setBufferedImages(bufferedImage[6]);
        setBufferedImages(bufferedImage[7]);
        setImageView(100, 100);
    }

}
