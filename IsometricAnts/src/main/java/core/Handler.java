package core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import objects.GameEntity;
import objects.building.Building;
import objects.building.Hive;
import objects.resource.Resource;
import pathfinding.Node;

import java.util.concurrent.CopyOnWriteArrayList;

public class Handler {

    public CopyOnWriteArrayList<GameEntity> entities;
    public CopyOnWriteArrayList<Node> targetNodeList;
    public CopyOnWriteArrayList<Building> buildings;
    public CopyOnWriteArrayList<Resource> resources;

    private Building selectedBuilding;

    public Handler() {
        this.entities = new CopyOnWriteArrayList<>();
        this.targetNodeList = new CopyOnWriteArrayList<>();
        this.buildings = new CopyOnWriteArrayList<>();
        this.resources = new CopyOnWriteArrayList<>();
    }

    public void update() {
        entities.forEach(e -> e.update());

        if(selectedBuilding != null) selectedBuilding.update();
    }

    public void render(SpriteBatch batch) {
        entities.forEach(e -> e.render(batch));

        if(selectedBuilding != null) selectedBuilding.render(batch);
    }

    public void addEntity(GameEntity entity) {
        this.entities.add(entity);
    }

    public void unselectBuilding() {
        this.selectedBuilding = null;
    }

    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    public void setSelectedBuilding(Building selectedBuilding) {
        this.selectedBuilding = selectedBuilding;
    }

    public Hive getHive() {
        for(Building building : buildings) {
            if(building instanceof Hive)
                return (Hive) building;
        }
        return null;
    }
}
