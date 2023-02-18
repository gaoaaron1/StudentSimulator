package com.game.student;

public class Const {
    // DEBUG MODE
    public static final boolean DEBUG_MODE = true;

    //Virtual Screen size and Box2D Scale(Pixels Per Meter)
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1280;
    public static final float PPM = 100;
    public static final float STEP_TIME = 1f / 300f;

    //Box2D Collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short WALL_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short ITEM_BIT = 4;
    public static final short ENEMY_BIT = 8;
    public static final short LEFT_BIT = 16;
    public static final short RIGHT_BIT = 32;
    public static final short TAIL_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short SPAWNER_BIT = 256;
    public static final short PLAYER_HEAD_BIT = 512;
    public static final short PLAYER_MOUTH_BIT = 1024;
    public static final short ENEMY_MOUTH_BIT = 2048;

    // Player
    public static final int PLAYER_TAIL_SIZE = 3;
    public static final float PLAYER_SPEED = 2.4f;
    public static final float TAIL_GAP = 48.0f;
    public static final float SPEED_BOOST_TIME = 0.6f;
    public static final float SPEED_BOOST = 1.35f;

    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;
    public static final int MAP_WIDTH = 25;
    public static final int MAP_HEIGHT = 17;

    public static final float EAT_DURATION = 0.3f;

    // Enemy
    public static final float ENEMY_START_SPEED = 1.0f;
    public static final float ENEMY_MAX_SPEED = 1.7f;
    public static final int MAX_ENEMIES = 5;

    // Camera
    public static final int offset_x = 32;
    public static final int offset_y = 96;

    // Options values
    public static int gameScreen = 0;
    public static float defaultVolume = 50;
}