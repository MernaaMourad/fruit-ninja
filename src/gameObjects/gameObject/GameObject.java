package gameObjects.gameObject;

import gameActions.GameAction;
import gameObjects.commands.ICommand;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameObject implements IGameObjects {

    private ENUM e;
    private int x;
    private int y;
    private int maxHeight;
    private int vInitial;
    private int vFalling;
    private Boolean sliced;
    private Boolean dropOff;

    private BufferedImage images[] = new BufferedImage[3];
    private ArrayList<Image> image = new ArrayList<Image>();

    private ImageView iv;
    private int index;

    private ICommand command;

    @Override
    public void slice() {
        setSliced(true);
    }

    @Override
    public void move() {
        getImageView().setLayoutX(getImageView().getLayoutX());
        if (getFallingVelocity() == 0) {
            GameAction gameAction= GameAction.getInstance();
            gameAction.updateObjectsLocations(this);
            if (getImageView().getLayoutY() <= getMaxHeight()) {
                setFallingVelocity();
            }
        } else {
            getImageView().setLayoutY(getImageView().getLayoutY() + getFallingVelocity());
            setYLocation((int) getImageView().getLayoutY());
        }

    }

    public void setCommand(ICommand command) {
        this.command = command;
    }

    public void swipe() {
        command.execute();
    }

    public void setXLocation(int x) {
        this.x = x;
    }

    @Override
    public int getXLocation() {
        return x;
    }

    public void setYLocation(int y) {
        this.y = y;
    }

    @Override
    public int getYLocation() {
        return y;
    }

    @Override
    public ENUM getObjectType() {
        switch (e) {
            case Apple:
                return ENUM.Apple;
            case Watermelon:
                return ENUM.Watermelon;
            case Orange:
                return ENUM.Orange;
            case DangerousBomb:
                return ENUM.DangerousBomb;
            case FatalBomb:
                return ENUM.FatalBomb;
            case DoubleScoreBanana:
                return ENUM.DoubleScoreBanana;
            case MagicBean:
                return ENUM.MagicBean;
        }
        return e;
    }

    public void setObjectType(ENUM e) {
        this.e = e;
    }

    @Override
    public int getInitialVelocity() {
        return vInitial;
    }

    public void setInitialVelocity() {
        Random random = new Random();
        GameAction gameActions=GameAction.getInstance();
        if (gameActions.getGameMemento().getCurrentScore() < 15) {
            this.vInitial = random.nextInt(3) + 3;
        } else if (gameActions.getGameMemento().getCurrentScore() >= 15 && gameActions.getGameMemento().getCurrentScore() < 25) {
            this.vInitial = random.nextInt(3) + 5;
        } else if (gameActions.getGameMemento().getCurrentScore() >= 25) {
            this.vInitial = random.nextInt(3) + 7;
        }

    }

    public void setInitialVelocity(int vInitial) {
        this.vInitial = vInitial;
    }

    @Override
    public int getFallingVelocity() {
        return vFalling;
    }

    public void setFallingVelocity() {
        Random random = new Random();
        GameAction gameActions=GameAction.getInstance();
        if (gameActions.getGameMemento().getCurrentScore() < 15) {
            this.vFalling = random.nextInt(4) + 4;
        } else if (gameActions.getGameMemento().getCurrentScore() >= 15 && gameActions.getGameMemento().getCurrentScore() < 25) {
            this.vFalling = random.nextInt(4) + 6;
        } else if (gameActions.getGameMemento().getCurrentScore() >= 25) {
            this.vFalling = random.nextInt(4) + 8;
        }

    }

    public void setFallingVelocity(int vFalling) {
        this.vFalling = vFalling;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    public void setSliced(Boolean sliced) {
        this.sliced = sliced;
    }

    public void setDropOff(Boolean dropOff) {
        this.dropOff = dropOff;
    }

    @Override
    public Boolean isSliced() {
        return sliced;
    }

    @Override
    public Boolean hasMovedOffScreen() {
        return dropOff;
    }

    public void setBufferedImages(BufferedImage bufferedImage) {
        image.add(SwingFXUtils.toFXImage(bufferedImage, null));
        images[index++] = bufferedImage;
    }

    public Image getSlicedImage() {
        return image.get(1);
    }

    public void setImageView(int x, int y) {
        iv = new ImageView();
        iv.setPreserveRatio(true);
        iv.setFitWidth(x);
        iv.setFitHeight(y);
        iv.setImage(image.get(0));
    }

    public void setImageViewLocation(int x, int y) {
        iv.setLayoutX(x);
        iv.setLayoutY(y);
    }

    public ImageView getImageView() {
        return iv;
    }

    public void setImageView(Image slicedImage) {
        iv.setImage(slicedImage);
    }

}
