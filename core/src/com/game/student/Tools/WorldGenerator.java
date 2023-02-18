package com.game.student.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.student.Const;
import com.game.student.Screens.PlayScreen;


public class WorldGenerator {

//------------------------------------ INITIALIZED VARIABLES ------------------------------------//

    //------------------------------------ CONSTRUCTOR  ------------------------------------//

    public WorldGenerator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        int path_arr_size = 0;

//------------------------------------- GENERATE THE WALLS FROM TILED -------------------------------------//

        //create ground bodies/fixtures
        for (MapObject object : map.getLayers().get("Wall").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2 + Const.offset_x) / Const.PPM, (rect.getY() + rect.getHeight() / 2 + Const.offset_y) / Const.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Const.PPM, rect.getHeight() / 2 / Const.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Const.WALL_BIT;
            body.createFixture(fdef);
        }

        for (MapObject object : map.getLayers().get("Path").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            screen.path_arr[path_arr_size++] = new Vector2(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight() + 64);
        }

        // Portals which the enemy can only pass through head first
        for (MapObject object : map.getLayers().get("Portal").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2 + Const.offset_x) / Const.PPM, (rect.getY() + rect.getHeight() / 2 + Const.offset_y) / Const.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Const.PPM, rect.getHeight() / 2 / Const.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Const.SPAWNER_BIT;
            body.createFixture(fdef);
        }

//------------------------------------- GENERATE THE ENEMY SPAWNERS FROM TILED --------------------------------------//


        shape.dispose();

    }

}