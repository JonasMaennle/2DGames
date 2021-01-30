package framework.core.service;

import framework.core.Handler;
import object.farming.CarottField;
import object.other.Tile;
import object.trees.Tree_00;
import object.trees.Tree_01;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static framework.helper.Graphics.quickLoaderImage;

public class TilePlacementService {

    private Handler handler;
    private Image field;

    public TilePlacementService(Handler handler){
        this.handler = handler;
        this.field = quickLoaderImage("tiles/field_tile");
    }

    public void update(Vector2f mouseGrid, int x, int y, String activeButtonName){
        switch (activeButtonName){
            case "Field":
                handler.getLayer(0).setTile((int)mouseGrid.x, (int)mouseGrid.y, x, y, field,40);
                break;
            case "Tree00":
                handler.getTreeList().add(new Tree_00(x - 64, y - 165, handler.getGraph(), true));
                break;
            case "Tree01":
                handler.getTreeList().add(new Tree_01(x - 32, y - 105, handler.getGraph(), true));
                break;
            case "Carott":
                Tile t = handler.getLayer(0).getTile((int)mouseGrid.x, (int)mouseGrid.y);
                if(t != null && t.getID() == 40){
                    handler.getFieldList().add(new CarottField(x,y));
                }
                break;
        }
        removeDecorationFromTiles((int)mouseGrid.x, (int)mouseGrid.y);
    }

    private void removeDecorationFromTiles(int x, int y){
        if(handler.getLayer(3).getTile(x, y) != null){
            handler.getLayer(3).removeTile(x - 1, y - 1);
        }
    }
}
