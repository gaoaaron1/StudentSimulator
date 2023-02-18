package com.game.student.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.game.student.Sprites.Entity.Player;


public abstract class Item {
    public boolean destroyed;

    // Box2d
    protected Body body;

    // Sprites & Textures
    protected Sprite sprite;
    protected TextureRegion texture;
    protected float x, y;
    protected int width, height;

    public Item() {

    }

    public abstract void defineItem(World world);

    public abstract void use(Player player);

    public void update(float dt) {

    }

    public void draw(Batch batch) {
        sprite.draw(batch);
        //batch.draw(sprite, body.getPosition().x * Const.PPM - width / 2f, body.getPosition().y * Const.PPM - height / 2f, width, height);
    }

    public Body getBody() {
        return body;
    }
}
