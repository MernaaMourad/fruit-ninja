package gameObjects.fruits;

import gameObjects.gameObject.ENUM;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class MagicBean extends Fruit {

    public MagicBean() throws IOException {
        BufferedImage bufferedImage[] = Images.getBufferedImage();
        setObjectType(ENUM.MagicBean);
        setBufferedImages(bufferedImage[12]);
        setImageView(85, 85);
    }

}
