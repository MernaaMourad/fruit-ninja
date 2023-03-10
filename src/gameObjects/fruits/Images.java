package gameObjects.fruits;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Images {

    private static BufferedImage bufferedImage[];

    private Images() {

    }

    public static BufferedImage[] getBufferedImage() throws IOException {
        if (bufferedImage == null) {
            //todo remove
//            File fp=new File("src//testImages.txt");
//            try {
//                fp.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            bufferedImage = new BufferedImage[22];
            bufferedImage[0] = ImageIO.read(new File("apple.png"));
            bufferedImage[1] = ImageIO.read(new File("SlicedApple.png"));

            bufferedImage[2] = ImageIO.read(new File("orange.png"));
            bufferedImage[3] = ImageIO.read(new File("SlicedOrange.png"));

            bufferedImage[4] = ImageIO.read(new File("watermelon.png"));
            bufferedImage[5] = ImageIO.read(new File("SlicedWatermelon.png"));

            bufferedImage[6] = ImageIO.read(new File("dangerousBomb.png"));
            bufferedImage[7] = ImageIO.read(new File("SlicedDangerousBomb.png"));

            bufferedImage[8] = ImageIO.read(new File("fatalBomb.png"));
            bufferedImage[9] = ImageIO.read(new File("GameOver.png"));

            bufferedImage[10] = ImageIO.read(new File("DoubleScoreBanana.png"));
            bufferedImage[11] = ImageIO.read(new File("DoubleScore.png"));

            bufferedImage[12] = ImageIO.read(new File("MagicBean.png"));
            bufferedImage[13] = ImageIO.read(new File("Heart.jpg"));

            bufferedImage[14] = ImageIO.read(new File("Sensei.png"));
            bufferedImage[15] = ImageIO.read(new File("SliceScoreGui.png"));

            bufferedImage[16] = ImageIO.read(new File("Sound.png"));
            bufferedImage[17] = ImageIO.read(new File("pause.png"));

            bufferedImage[18] = ImageIO.read(new File("GameOver.png"));
            bufferedImage[19] = ImageIO.read(new File("Fruit Ninja.png"));

            bufferedImage[20] = ImageIO.read(new File("background.jpg"));
            bufferedImage[21] = ImageIO.read(new File("Combo.png"));
        }
        return bufferedImage;
    }

}
