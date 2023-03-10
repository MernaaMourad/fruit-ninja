package gameObjects.gameObject;

public interface IGameObjects {

    public int getXLocation();

    public int getYLocation();

    public ENUM getObjectType();

    public int getInitialVelocity();

    public int getFallingVelocity();

    public int getMaxHeight();

    public Boolean hasMovedOffScreen();

    public void slice();

    public void move();

    public Boolean isSliced();

}

