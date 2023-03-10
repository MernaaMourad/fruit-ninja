package gameObjects.commands;

import gameActions.GameAction;

public class IncrementCommand implements ICommand {

    GameAction gameAction;

    public IncrementCommand(GameAction gameAction) {
        this.gameAction = gameAction;
    }

    @Override
    public void execute() {
        gameAction.getGameMemento().setCurrentScore(gameAction.getGameMemento().getCurrentScore() + 1);
    }

}
