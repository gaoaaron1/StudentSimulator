package com.game.student.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.student.Const;
import com.game.student.Simulator;
import com.game.student.Sprites.Entity.Player;

public class Apple extends Item {

    private float timer;

    public Apple(World world, Vector2 pos) {
        super();

        TextureAtlas myAtlas = Simulator.manager.get("CaterpillarAtlas.atlas", TextureAtlas.class);

        texture = new TextureRegion(myAtlas.findRegion("apple"));
        sprite = new Sprite(texture);

        width = (int) sprite.getWidth();
        height = (int) sprite.getHeight();
        timer = 0;

        x = pos.x;
        y = pos.y;
        sprite.setPosition(x - width / 2f, y - height / 2f);

        defineItem(world);
    }

    public void update(float dt) {
        timer += dt;
        float scale = 0.05f * (float) Math.cos(5 * timer) + 0.95f;
        sprite.setScale(scale);
    }

    @Override
    public void defineItem(World world) {
        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.StaticBody;
        cdef.position.set(x / Const.PPM, y / Const.PPM);

        body = world.createBody(cdef);
        FixtureDef cfdef = new FixtureDef();
        CircleShape cshape = new CircleShape();
        cshape.setRadius(32 / Const.PPM);
        cfdef.shape = cshape;
        cfdef.isSensor = true;
        cfdef.filter.categoryBits = Const.ITEM_BIT;
        cfdef.filter.maskBits = Const.PLAYER_HEAD_BIT | Const.ENEMY_HEAD_BIT;
        body.createFixture(cfdef).setUserData(this);
        cshape.dispose();
    }

    @Override
    public void use(Player player) {

    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
