package gameActions;

import gameObjects.gameObject.GameObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface GameActions {

    public GameObject createGameObject() throws IOException;

    public void updateObjectsLocations(GameObject gameObject);

    public void saveGame() throws ParserConfigurationException, TransformerException;

    public void loadGame() throws IOException, ParserConfigurationException, SAXException;

    public void resetGame() throws IOException;

}
