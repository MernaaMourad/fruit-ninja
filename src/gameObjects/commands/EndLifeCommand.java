package gameObjects.commands;

import gameActions.GameAction;

public class EndLifeCommand implements ICommand {

    GameAction gameAction;

    public EndLifeCommand(GameAction gameAction) {
        this.gameAction = gameAction;
    }

    @Override
    public void execute() {
        gameAction.getGameMemento().setLives(0);
    }

}
