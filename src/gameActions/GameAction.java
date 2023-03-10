package gameActions;

import gameObjects.gameObject.ENUM;
import gameObjects.gameObject.GameObject;
import gameObjects.factories.FactoryProvider;
import gameObjects.factories.IAbstractFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameAction implements GameActions {

    private static GameAction instance;

    private GameMemento gameMemento;

    private GameAction() {
    }

    public static GameAction getInstance() {
        if (instance == null)
            instance = new GameAction();
        return instance;
    }

    public void setGameMemento(int difficulty, int currentScore, int highestScore, int lives) {
        this.gameMemento = new GameMemento(difficulty, currentScore, highestScore, lives);

    }

    public GameMemento getGameMemento() {
        return gameMemento;
    }

    @Override
    public GameObject createGameObject() throws IOException {
        Random random = new Random();
        FactoryProvider factoryProvider = FactoryProvider.getInstance();
        IAbstractFactory factory;
        GameObject gameObject;
        int r1 = random.nextInt(13);
        switch (r1) {
            case 0:
            case 1:
            case 2:
                factory = factoryProvider.create("fruit");
                gameObject = (GameObject) factory.create("apple");
                break;
            case 3:
            case 4:
            case 5:
                factory = factoryProvider.create("fruit");
                gameObject = (GameObject) factory.create("orange");

                break;
            case 6:
            case 7:
            case 8:
                factory = factoryProvider.create("fruit");
                gameObject = (GameObject) factory.create("watermelon");
                break;
            case 9:
                factory = factoryProvider.create("bomb");
                gameObject = (GameObject) factory.create("dangerous");
                break;
            case 10:
                factory = factoryProvider.create("bomb");
                gameObject = (GameObject) factory.create("fatal");
                break;
            case 11:
                if (gameMemento.getCurrentScore() > 10) {
                    factory = factoryProvider.create("fruit");
                    gameObject = (GameObject) factory.create("DoubleScoreBanana");
                    break;
                } else {
                    factory = factoryProvider.create("fruit");
                    gameObject = (GameObject) factory.create("apple");
                    break;
                }
            case 12:
                if (gameMemento.getCurrentScore() > 10) {
                    factory = factoryProvider.create("fruit");
                    gameObject = (GameObject) factory.create("MagicBean");
                    break;
                } else {
                    factory = factoryProvider.create("fruit");
                    gameObject = (GameObject) factory.create("orange");
                    break;
                }
            default:
                gameObject = null;
        }
        int xPos = random.nextInt(600) + 50;
        int yPos = random.nextInt(100) + 20;
        gameObject.setMaxHeight(yPos);
        gameObject.setImageViewLocation(xPos, 500);
        gameObject.setInitialVelocity();
        return gameObject;
    }

    @Override
    public void updateObjectsLocations(GameObject gameObject) {
        gameObject.setXLocation((int) gameObject.getImageView().getLayoutX());
        gameObject.getImageView().setLayoutY(gameObject.getImageView().getLayoutY() - gameObject.getInitialVelocity());
        gameObject.setYLocation((int) (gameObject.getImageView().getLayoutY()));
    }

    @Override
    public void saveGame() throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("fruit_ninja");

        Element state = doc.createElement("state");

        Element difficulty = doc.createElement("difficulty");
        Text nDifficulty = doc.createTextNode(String.valueOf(gameMemento.getDifficulty()));
        difficulty.appendChild(nDifficulty);

        Element lives = doc.createElement("lives");
        Text nLives = doc.createTextNode(String.valueOf(gameMemento.getLives()));
        lives.appendChild(nLives);

        Element currentScore = doc.createElement("current_score");
        Text nCurrentScore = doc.createTextNode(String.valueOf(gameMemento.getCurrentScore()));
        currentScore.appendChild(nCurrentScore);

        Element highestScore = doc.createElement("highest_score");
        Text nHighestScore = doc.createTextNode(String.valueOf(gameMemento.getHighestScore()));
        highestScore.appendChild(nHighestScore);

        state.appendChild(difficulty);
        state.appendChild(lives);
        state.appendChild(currentScore);
        state.appendChild(highestScore);
        if (gameMemento.getGameObjects().size() > 0) {

            Element objects = doc.createElement("game_objects");

            for (int i = 0; i < gameMemento.getGameObjects().size(); i++) {

                Element object = doc.createElement("game_object");

                Element type = doc.createElement("type");
                Text nType = doc.createTextNode(String.valueOf(gameMemento.getGameObjects().get(i).getObjectType()));
                type.appendChild(nType);
                Element xLocation = doc.createElement("x");
                Text nXLocation = doc.createTextNode(String.valueOf(gameMemento.getGameObjects().get(i).getXLocation()));
                xLocation.appendChild(nXLocation);

                Element yLocation = doc.createElement("y");
                Text nYLocation = doc.createTextNode(String.valueOf(gameMemento.getGameObjects().get(i).getYLocation()));
                yLocation.appendChild(nYLocation);

                Element vInitial = doc.createElement("initial_velocity");
                Text nVInitial = doc.createTextNode(String.valueOf(gameMemento.getGameObjects().get(i).getInitialVelocity()));
                vInitial.appendChild(nVInitial);

                Element vFinal = doc.createElement("falling_velocity");
                Text nVFinal = doc.createTextNode(String.valueOf(gameMemento.getGameObjects().get(i).getFallingVelocity()));
                vFinal.appendChild(nVFinal);

                Element maxHeight = doc.createElement("max_height");
                Text nMaxHeight = doc.createTextNode(String.valueOf(gameMemento.getGameObjects().get(i).getMaxHeight()));
                maxHeight.appendChild(nMaxHeight);

                object.appendChild(type);
                object.appendChild(xLocation);
                object.appendChild(yLocation);
                object.appendChild(maxHeight);
                object.appendChild(vInitial);
                object.appendChild(vFinal);
                objects.appendChild(object);
            }
            state.appendChild(objects);
        }
        root.appendChild(state);
        doc.appendChild(root);

        File fp = new File("data.xml");
        if (!fp.exists()) {
            JOptionPane.showMessageDialog(null, "File doesn't exist !");
            System.exit(-1);
        }

        DOMSource source = new DOMSource(doc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        StreamResult result = new StreamResult(fp);
        transformer.transform(source, result);

    }

    @Override
    public void loadGame() throws IOException, ParserConfigurationException, SAXException {

        ArrayList<GameObject> gameObjects = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File fp = new File("data.xml");
        if (fp.length() > 60) {
            Document doc = builder.parse(fp);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("state");

            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                int difficulty, lives, currentScore, highScore;
                difficulty = Integer.parseInt(eElement.getElementsByTagName("difficulty").item(0).getTextContent());
                lives = Integer.parseInt(eElement.getElementsByTagName("lives").item(0).getTextContent());
                currentScore = Integer.parseInt(eElement.getElementsByTagName("current_score").item(0).getTextContent());
                highScore = Integer.parseInt(eElement.getElementsByTagName("highest_score").item(0).getTextContent());
                setGameMemento(difficulty, currentScore, highScore, lives);
                NodeList nodeList1 = doc.getElementsByTagName("game_object");
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node1 = nodeList1.item(j);
                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement1 = (Element) node1;
                        GameObject gameObject = new GameObject();
                        gameObject.setObjectType(ENUM.valueOf(eElement1.getElementsByTagName("type").item(0).getTextContent()));
                        gameObject.setXLocation(Integer.parseInt(eElement1.getElementsByTagName("x").item(0).getTextContent()));
                        gameObject.setYLocation(Integer.parseInt(eElement1.getElementsByTagName("y").item(0).getTextContent()));
                        gameObject.setMaxHeight(Integer.parseInt(eElement1.getElementsByTagName("max_height").item(0).getTextContent()));
                        gameObject.setInitialVelocity(Integer.parseInt(eElement1.getElementsByTagName("initial_velocity").item(0).getTextContent()));
                        gameObject.setFallingVelocity(Integer.parseInt(eElement1.getElementsByTagName("falling_velocity").item(0).getTextContent()));
                        gameObjects.add(gameObject);
                    }
                }
            }
            gameMemento.setGameObjects(gameObjects);
        }

    }

    @Override
    public void resetGame() throws IOException {
        File fp = new File("data.xml");
        fp.delete();
        fp.createNewFile();
    }

}
