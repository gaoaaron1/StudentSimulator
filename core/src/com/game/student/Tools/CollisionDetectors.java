package com.game.student.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.student.Const;


public class CollisionDetectors implements ContactListener {

    //-------------------------------- INITIALIZE VARIABLES ----------------------------------//
    boolean it = true;
    boolean addingTailA = false;
    boolean addingTailB = false;

    private boolean player_eats_enemy = false;
    private boolean enemy_eats_player = false;

//----------------------------------- OVERRIDE METHODS --------------------------------------------//

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) return;

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Const.LEFT_BIT | Const.WALL_BIT:
            case Const.LEFT_BIT | Const.SPAWNER_BIT:
                addWallSensors(Const.LEFT_BIT, fixA, fixB);
                break;
            case Const.RIGHT_BIT | Const.WALL_BIT:
            case Const.RIGHT_BIT | Const.SPAWNER_BIT:
                addWallSensors(Const.RIGHT_BIT, fixA, fixB);
                break;


        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            //WALLS
            case Const.LEFT_BIT | Const.WALL_BIT:
            case Const.LEFT_BIT | Const.SPAWNER_BIT:
                removeWallSensors(Const.LEFT_BIT, fixA, fixB);
                break;
            case Const.RIGHT_BIT | Const.WALL_BIT:
            case Const.RIGHT_BIT | Const.SPAWNER_BIT:
                removeWallSensors(Const.RIGHT_BIT, fixA, fixB);
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (cDef == (Const.ENEMY_HEAD_BIT | Const.PLAYER_HEAD_BIT)) {
            contact.setEnabled(false);
        }

        if (cDef == (Const.ENEMY_HEAD_BIT | Const.SPAWNER_BIT)) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
//----------------------------------- METHODS ------------------------------------------------//


    private void addWallSensors(short DIRECTION_BIT, Fixture fixA, Fixture fixB) {

    }

    private void removeWallSensors(short DIRECTION_BIT, Fixture fixA, Fixture fixB) {

    }

}