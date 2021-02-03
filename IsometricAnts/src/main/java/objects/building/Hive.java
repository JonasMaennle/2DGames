package objects.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import core.GameScreen;
import objects.ants.AntWorker;
import objects.ants.task.WalkToTask;
import pathfinding.Node;

import static helper.Const.TILE_HEIGHT;
import static helper.Const.TILE_WIDTH;
import static helper.Functions.*;

public class Hive extends Building {

    private Texture spawnAntTexture;
    private int buttonWidth, buttonHeight;
    private Rectangle spawnRect;

    public Hive(float x, float y, GameScreen gameScreen) {
        super(x, y, gameScreen);
        this.spawnAntTexture = new Texture("building/plus.png");

        this.buttonWidth = 64;
        this.buttonHeight = 64;
        this.spawnRect = new Rectangle(this.x, this.y + 64, buttonWidth, buttonHeight);
    }

    @Override
    public void selected() {
        gameScreen.getHandler().setSelectedBuilding(this);
    }

    @Override
    public void update() {
        // click spawn button
        if (Gdx.input.isButtonJustPressed(0)) {
            boolean unselect = true;
            // check if still clicked
            Vector2 vec2 = transformTiledMapCoordinatesLeftToTop(gameScreen.getCamera(), gameScreen.getMapWidth(), gameScreen.getMapHeight());
            Vector2 gridPos = transformCoordinatesToGrid(vec2.x, vec2.y, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            if(gridPos == null)
                return;
            Vector2 normalPos = transformGridToCoordinates(gridPos.x, gridPos.y, gameScreen.getMapWidth(), gameScreen.getMapHeight());
            if(x == normalPos.x && y == normalPos.y) {
                unselect = false;
            }


            // select button
            Vector3 mousePos = transformMouseToWorldCoordinates(gameScreen.getCamera());

            if(spawnRect.contains(mousePos.x, mousePos.y)) {
                spawnWorkerAnt();
                unselect = false;
            }

            if(unselect)
                gameScreen.getHandler().unselectBuilding();
        }
    }

    private void spawnWorkerAnt() {
        // find spot for ant
        Node nodeToTest = findEmptyTileForNewAnt();

        // if no node in game is return
        if(nodeToTest == null)
            return;

        // create worker ant
        AntWorker antWorker = new AntWorker(x + (TILE_WIDTH / 2), y + (TILE_HEIGHT / 2), 64, 32, gameScreen);
        gameScreen.getHandler().addEntity(antWorker);

        // add new walkTo task to ant
        if(nodeToTest != null) {
            Vector2 normal = transformGridToCoordinates(nodeToTest.getX(), nodeToTest.getY(), gameScreen.getMapWidth(), gameScreen.getMapHeight());
            Vector2 coords = transformCoordinatesToIso(new Vector2(normal.x, normal.y), gameScreen.getMapWidth(), gameScreen.getMapHeight());

            antWorker.addTask(new WalkToTask(coords.x, coords.y, antWorker, 2f, false));
        }
    }

    private Node findEmptyTileForNewAnt() {
        Node nodeToTest;
        int step = 1;

        while (true) {
            for(int x = step; x >= (step * -1); x--) {
                for(int y = step; y >= (step * -1); y--) {
                    if(!(y == 0 && x == 0)) {
                        nodeToTest = gameScreen.getGraph().getNode(homeNode.getX() + x, homeNode.getY() + y);
                        if(nodeToTest == null)
                            return null;

                        boolean available = testIfTargetNodeAvailable(gameScreen.getHandler().targetNodeList, nodeToTest, gameScreen.getHandler().entities, gameScreen.getMapWidth(), gameScreen.getMapHeight());

                        if(available)
                            return nodeToTest;
                    }
                }
            }
            step++;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // render buttons
        batch.draw(spawnAntTexture, spawnRect.getX(), spawnRect.getY(), spawnRect.getWidth(), spawnRect.getHeight());
    }
}
