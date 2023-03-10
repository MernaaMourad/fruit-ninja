package gameObjects.commands;

import gameActions.GameAction;

public class BonusCommand implements ICommand {

    GameAction gameAction;

    public BonusCommand(GameAction gameAction) {
        this.gameAction = gameAction;
    }

    @Override
    public void execute() {
        if (gameAction.getGameMemento().getLives() < 3) {
            gameAction.getGameMemento().setLives(gameAction.getGameMemento().getLives() + 1);
        } else {
            gameAction.getGameMemento().setCurrentScore(gameAction.getGameMemento().getCurrentScore() + 25);
            if (gameAction.getGameMemento().getCurrentScore() >= gameAction.getGameMemento().getHighestScore()) {
                gameAction.getGameMemento().setHighestScore(gameAction.getGameMemento().getCurrentScore());
            }
        }
    }

}
