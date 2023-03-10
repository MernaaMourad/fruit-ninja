package gameObjects.commands;

import gameActions.GameAction;

public class DecrementCommand implements ICommand {

    GameAction gameAction;

    public DecrementCommand(GameAction gameAction) {
        this.gameAction = gameAction;
    }

    @Override
    public void execute() {
        gameAction.getGameMemento().setLives(gameAction.getGameMemento().getLives() - 1);
    }

}
