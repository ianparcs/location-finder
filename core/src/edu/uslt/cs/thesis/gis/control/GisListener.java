package edu.uslt.cs.thesis.gis.control;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.uslt.cs.thesis.gis.algorithm.AStarPathFinder;
import edu.uslt.cs.thesis.gis.algorithm.DiagonalHeuristic;
import edu.uslt.cs.thesis.gis.algorithm.PathFinder;
import edu.uslt.cs.thesis.gis.core.GIS;
import edu.uslt.cs.thesis.gis.gui.HUD;
import edu.uslt.cs.thesis.gis.manager.BuildingManager;
import edu.uslt.cs.thesis.gis.map.TiledMapStage;
import edu.uslt.cs.thesis.gis.object.Building;
import edu.uslt.cs.thesis.gis.object.GisObject;

public class GisListener extends ClickListener {

    private BuildingManager buildingManager;
    private PathFinder pathFinder;
    private TiledMapStage mapStage;
    private List<String> list;
    private GisObject marker;
    private GIS gis;

    private static int startX;
    private static int startY;

    private static int goalX;
    private static int goalY;

    public GisListener(GIS gis, TiledMapStage mapStage, HUD hud) {
        this.mapStage = mapStage;
        this.gis = gis;
        pathFinder = new AStarPathFinder(mapStage, new DiagonalHeuristic());
        list = hud.getScrollPanel().list;
        buildingManager = gis.buildingManager;
        marker = gis.getMarker();
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {

        float dx = x - marker.getWidth() / 2;
        float dy = y - marker.getHeight() / 2;

        if (event.getTarget() == marker.getObject()) {
            System.out.println("X: " + dx);
            if (dx > 0 - marker.getWidth() / 2 && dx < mapStage.getMap().getWidth() - marker.getWidth() / 2 && dy > 0 && dy < 900) {
                marker.setPosition(dx, dy);

                if (gis.getUslMap().isValidLocation(startX, startY)) {
                    startX = (int) (event.getTarget().getX() / 16 + marker.getWidth() / 2 / 16);
                    startY = (int) (event.getTarget().getY() / 16 + marker.getHeight() / 2 / 16);

                }
            }
        }

        if (list.getSelected() != null) {
            Building building = buildingManager.get(list.getSelected());
            goalX = (int) building.getX() / 16;
            goalY = (int) building.getY() / 16;
        }

        if (gis.getUslMap().isValidLocation(startX, startY)) {
            pathFinder.findPath(startX, startY, goalX, goalY);
        }
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {


    }

    public void update() {

    }
}
