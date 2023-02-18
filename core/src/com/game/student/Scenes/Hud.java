package com.game.student.Scenes;

import static com.game.student.Screens.PlayScreen.game;
import static com.game.student.Simulator.manager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.student.Const;
import com.game.student.Screens.PlayScreen;
import com.game.student.Screens.SelectScreen;
import com.game.student.Simulator;

import java.util.Stack;

public class Hud extends InputAdapter implements Disposable {

    /*============================== INITIALIZED VARIABLES ================================*/

    //Button features
    public Stage stage1;
    public static Stage stage2;
    private Skin skin;

    //Menu features
    public Image filterScreen;
    private Image scoreBoard;
    private BitmapFont textFont;

    public static boolean pauseGame = false;
    public static boolean hudHidden = false;

    public ImageButton pauseButton, tutorialButton, homeButton, retryButton;

    public static int score;
    public static int highscore;
    public static int tail_length = 3;
    public static int tail_longest;
    public static Label scoreLabel, scoreLabel2;
    public static Stack<Label> eatEffectLabel;
    private static Label tailLengthLabel, tailLongestLabel;
    private static Label highscoreLabel;
    private static Label highscoreLabel2;
    public static Label gameOverLabel;

    //Begin game variables
    public Label beginGameLabel;
    private float timer;
    ColorAction fadeIn = new ColorAction();
    ColorAction fadeOut = new ColorAction();

    //Buttons
    private TextButton settingButton, resumeButton, exitButton;
    private ImageButton.ImageButtonStyle tutorialStyle;
    private ImageButton leaderboardButton;
    private TextButton leaderboardTextButton;

    //Object
    private Music musics;
    private Sound sounds;
    private Options options = PlayScreen.options;
    private Tutorial tutorial = PlayScreen.tutorial;

    private Table gameover_table;

    //Timer
    private boolean enableCount;
    public float eatEffectTimer;

    public static Preferences prefs = Gdx.app.getPreferences("MemoryCaterpillar");


    /*============================== CONSTRUCTOR ======================================*/

    public Hud() {

        //Declare stages for user input processors
        stage1 = new Stage(new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT));
        stage2 = new Stage(new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT));

        //Declare skin for atlas texture components
        skin = options.getSkin();

        textFont = manager.get("fonts/junegull.ttf", BitmapFont.class);

        //Set stage
        Gdx.input.setInputProcessor(stage1);

        //Declare graphics and format graphics for game over
        setGameOverGraphics();

        //Declare our labels
        setLabels();

        //Declare and format score components
        setScoreParameters();

        //Declare and format buttons
        createButton();
        setButtonParameters();

        //Set timer
        eatEffectTimer = 0;
        enableCount = false;

        //Add actors to stage
        stage1.addActor(pauseButton);
        stage1.addActor(tutorialButton);
        stage1.addActor(scoreLabel);
        stage1.addActor(highscoreLabel);
        stage2.addActor(beginGameLabel);

        //Set musics
        musics = options.getMusic();

    }

//------------------------------------------------------------ METHODS ----------------------------------------------------------------------//

    private void setLabels() {
        //Load our label for game
        beginGameLabel = new Label(String.format("Swipe in any direction\n" + "to begin!"), new Label.LabelStyle(textFont, Color.WHITE));
        beginGameLabel.setFontScale(0.9f);
        beginGameLabel.setWidth(Const.V_WIDTH / 12f);
        beginGameLabel.setHeight(Const.V_HEIGHT / 12f);
        beginGameLabel.setPosition(Const.V_WIDTH / 1.65f, Const.V_HEIGHT / 0.85f);

        beginGameLabel.setColor(Color.CLEAR);
        ColorAction ca = new ColorAction();
        ca.setEndColor(Color.WHITE);
        ca.setDuration(0.8f);
        beginGameLabel.addAction(ca);

        eatEffectLabel = new Stack<Label>();
    }


    private void setScoreParameters() {
        //Retrieve our high score from prefs.
        highscore = prefs.getInteger("highscore" + 0 + ":", 0);
        tail_longest = prefs.getInteger("taillength" + 0 + ":", 0);
        score = 0;

        //Show score label
        scoreLabel = new Label(String.format("" + score), new Label.LabelStyle(textFont, Color.WHITE));

        scoreLabel.setWidth(Const.V_WIDTH / 12f);
        scoreLabel.setHeight(Const.V_HEIGHT / 12f);
        scoreLabel.setPosition(Const.V_WIDTH / 1.7f, Const.V_HEIGHT * 1.37f);

        //Show highscore label
        highscoreLabel = new Label(String.format("BEST: " + highscore), new Label.LabelStyle(textFont, Color.WHITE));
        highscoreLabel.setFontScale(0.8f);

        highscoreLabel.setWidth(Const.V_WIDTH / 12f);
        highscoreLabel.setHeight(Const.V_HEIGHT / 12f);
        highscoreLabel.setPosition(Const.V_WIDTH / 1.7f, Const.V_HEIGHT * 1.41f);
    }


    private void setButtonParameters() {

        //RESUME BUTTON
        resumeButton.setStyle(Simulator.textButtonStyle);
        resumeButton.setColor(new Color(0.1f, 0.4f, 0.1f, 1.0f));
        resumeButton.setText("RESUME");
        resumeButton.getLabel().setFontScale(1.2f);
        resumeButton.setWidth(Const.V_WIDTH / 2f);
        resumeButton.setHeight(Const.V_HEIGHT / 8f);
        resumeButton.setPosition(Const.V_WIDTH / 1.35f, Const.V_HEIGHT / 0.85f);

        //SETTING BUTTON
        settingButton.setStyle(Simulator.textButtonStyle);
        settingButton.setColor(new Color(0.1f, 0.1f, 0.4f, 1.0f));
        settingButton.setText("OPTIONS");
        settingButton.getLabel().setFontScale(1.2f);
        settingButton.setWidth(Const.V_WIDTH / 2f);
        settingButton.setHeight(Const.V_HEIGHT / 8f);
        settingButton.setPosition(Const.V_WIDTH / 1.35f, Const.V_HEIGHT / 1.01f);

        //EXIT BUTTON
        exitButton.setStyle(Simulator.textButtonStyle);
        exitButton.setColor(new Color(0.4f, 0.1f, 0.1f, 1.0f));
        exitButton.setText("EXIT");
        exitButton.getLabel().setFontScale(1.2f);
        exitButton.setWidth(Const.V_WIDTH / 2f);
        exitButton.setHeight(Const.V_HEIGHT / 8f);
        exitButton.setPosition(Const.V_WIDTH / 1.35f, Const.V_HEIGHT / 1.25f);

        //PAUSE BUTTON
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        buttonStyle.imageUp = Simulator.buttonSkin.getDrawable("pause");
        buttonStyle.imageDown = Simulator.buttonSkin.getDrawable("pause");
        pauseButton.setStyle(buttonStyle);
        pauseButton.setSize(180, 180);
        pauseButton.setPosition(1650, 2680);
        pauseButton.getStyle().imageUp = Simulator.buttonSkin.getDrawable("pause");
        pauseButton.getStyle().imageDown = Simulator.buttonSkin.getDrawable("pause");

        //TUTORIAL BUTTON
        ImageButton.ImageButtonStyle tutorialStyle = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        tutorialStyle.imageUp = Simulator.buttonSkin.getDrawable("tutorial_up");
        tutorialStyle.imageDown = Simulator.buttonSkin.getDrawable("tutorial_down");

        tutorialButton.setPosition(1450, 2700);


        //HOME BUTTON
        ImageButton.ImageButtonStyle buttonStyle2 = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        buttonStyle2.imageUp = Simulator.buttonSkin.getDrawable("home");
        buttonStyle2.imageDown = Simulator.buttonSkin.getDrawable("home");
        homeButton = new ImageButton(Simulator.buttonSkin);
        homeButton.setStyle(buttonStyle2);
        homeButton.setWidth(Const.V_HEIGHT / 8f);
        homeButton.setHeight(Const.V_HEIGHT / 8f);
        homeButton.setPosition(Const.V_WIDTH / 0.85f, Const.V_HEIGHT / 1.5f);

        //RETRY BUTTON
        ImageButton.ImageButtonStyle buttonStyle3 = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        buttonStyle3.imageUp = Simulator.buttonSkin.getDrawable("play");
        buttonStyle3.imageDown = Simulator.buttonSkin.getDrawable("play");
        retryButton = new ImageButton(Simulator.buttonSkin);
        retryButton.setStyle(buttonStyle3);
        retryButton.setWidth(Const.V_WIDTH / 2f);
        retryButton.setHeight(Const.V_HEIGHT / 8f);
        retryButton.setPosition(Const.V_WIDTH / 1.51f, Const.V_HEIGHT / 1.5f);

        //Pause Button Listener
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                options.playButtonClick();

                if (pauseGame == false) {
                    stage2.addActor(filterScreen);
                    stage2.addActor(resumeButton);
                    stage2.addActor(settingButton);
                    stage2.addActor(exitButton);
                    pauseGame = true;
                } else {
                    resumeButton.remove();
                    settingButton.remove();
                    pauseGame = false;
                }
            }
        });

        //Tutorial Button Listener
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                options.playButtonClick();
                tutorial.tutorialHandler();

            }
        });

        //Home Button Listener
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    game.androidController.showInterstitialAds();
                }

                options.playButtonClick();
                options.musics.stop();
                dispose();
                pauseGame = false;
                Gdx.input.setInputProcessor(stage1);
                game.setScreen(new SelectScreen(game));
                options.musics = manager.get("audio/music/village.mp3", Music.class);

                if (options.enableMusic == true) {
                    options.musics.setLooping(true);
                    options.musics.setVolume(options.slider.getValue() / 100);
                    options.musics.play();
                }
            }
        });


        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    game.androidController.showInterstitialAds();
                }

                dispose();
                pauseGame = false;
                Gdx.input.setInputProcessor(stage1);
                game.setScreen(new PlayScreen(game));

                options.musics.play();

            }
        });
    }


    private void createButton() {

        //OPTIONS HANDLER
        //settingStyle = new TextButton.TextButtonStyle();
        //options.createButtonStyle(settingStyle, "setting_button", "setting_button_clicked");
        //settingButton = new TextButton("", settingStyle);

        settingButton = new TextButton("", Simulator.textButtonStyle);

        resumeButton = new TextButton("", Simulator.textButtonStyle);

        exitButton = new TextButton("", Simulator.textButtonStyle);

        pauseButton = new ImageButton(Simulator.buttonSkin);

        tutorialStyle = new ImageButton.ImageButtonStyle();
        tutorialStyle.up = Simulator.buttonSkin.getDrawable("tutorial_up");
        tutorialStyle.down = Simulator.buttonSkin.getDrawable("tutorial_down");

        tutorialButton = new ImageButton(tutorialStyle);

        //------------- resume and exit -----------------//
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                resumeButton.remove();
                settingButton.remove();
                exitButton.remove();
                filterScreen.remove();
                pauseGame = false;

            }
        });

        //Exit Button Listener
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                options.musics.stop();
                dispose();
                pauseGame = false;
                Gdx.input.setInputProcessor(stage1);
                game.setScreen(new SelectScreen(game));
                options.musics = manager.get("audio/music/village.mp3", Music.class);

                if (options.enableMusic == true) {
                    options.musics.setLooping(true);
                    options.musics.setVolume(options.slider.getValue() / 100);
                    options.musics.play();
                }
            }
        });


        //------------- settings -----------------//
        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                Gdx.input.setInputProcessor(options.stage);
                options.optionsHandler();
            }
        });


    }


    public static void updateScore(int value) {
        score += value;
        scoreLabel.setText(String.format("" + score));
        if (score > highscore) {
            Hud.prefs.putInteger("highscore0:", score);
            highscoreLabel.setText(String.format("BEST: " + score));
            prefs.flush();
        }


    }

    public static void updateTail(int value) {
        if (value > tail_length)
            tail_length = value;

        if (tail_length > tail_longest) {
            tail_longest = tail_length;
            Hud.prefs.putInteger("taillength0:", tail_length);
            prefs.flush();
        }
    }

    public int getScore() {
        return score;
    }

    public Stage getStage1() {
        return stage1;
    }

    public Stage getStage2() {
        return stage2;
    }

    public void update(float dt) {

        if (!PlayScreen.beginGame) {
            beginGameLabel.remove();

            timer += dt;
            float scale = 0.05f * (float) Math.cos(5 * timer) + 0.95f;
            beginGameLabel.setFontScale(scale);
            stage2.addActor(beginGameLabel);
        }

        //timer for handling eating
        if (enableCount) {
            eatEffectTimer += dt;

            if (eatEffectTimer >= 1.5f) {
                //eatEffectLabel.remove();
                while (!eatEffectLabel.isEmpty()) {
                    eatEffectLabel.peek().remove();
                    eatEffectLabel.pop();
                }
                eatEffectTimer = 0;
                enableCount = false;
            }
        }
    }

    public void handleEatLabels(String label, String entity, float xPosition, float yPosition) {
        eatEffectTimer = 0;
        enableCount = true;

        //Set up our eating effect label
        eatEffectLabel.add(new Label(String.format(label), new Label.LabelStyle(textFont, Color.WHITE)));
        eatEffectLabel.peek().setFontScale(0.75f);


        //Handles eating
        if (xPosition > 607) {
            eatEffectLabel.peek().setX(xPosition + 475f);
        } else {
            eatEffectLabel.peek().setX(xPosition + 500f);
        }

        eatEffectLabel.peek().setY(yPosition + 900f);

        eatEffectLabel.peek().setColor(Color.CLEAR);
        fadeIn = new ColorAction();
        //fadeOut = new ColorAction();

        if (entity == "PLAYER") {
            fadeIn.setEndColor(Color.GREEN);
        } else if (entity == "ENEMY") {
            fadeIn.setEndColor(Color.RED);
        }

        stage2.addActor(eatEffectLabel.peek());

        fadeIn.setDuration(0.7f);
        eatEffectLabel.peek().addAction(fadeIn);


    }

    private void setGameOverGraphics() {
        //Filter screen
        filterScreen = new Image(new Texture(Gdx.files.internal("filter.png")));
        filterScreen.setWidth(Const.V_WIDTH * 1.75f);
        filterScreen.setHeight(Const.V_HEIGHT * 2);
        filterScreen.setPosition(Const.V_WIDTH / 8f, Const.V_HEIGHT / 8f);

        scoreBoard = new Image(Simulator.buttonSkin.getDrawable("button_rounded_up"));
        scoreBoard.setColor(0.7f, 0.6f, 0.25f, 0.9f);
        scoreBoard.setWidth(Const.V_WIDTH / 1.45f);
        scoreBoard.setHeight(Const.V_HEIGHT / 3f);
        scoreBoard.setPosition(Const.V_WIDTH / 1.5f, Const.V_HEIGHT / 1.21f);

        ImageButton.ImageButtonStyle leaderboardButtonStyle = new ImageButton.ImageButtonStyle();
        leaderboardButtonStyle.up = Simulator.buttonSkin.getDrawable("leaderboard_up");
        leaderboardButtonStyle.down = Simulator.buttonSkin.getDrawable("leaderboard_down");

        leaderboardButton = new ImageButton(leaderboardButtonStyle);
        leaderboardButton.setHeight(Const.V_WIDTH / 10f);
        leaderboardButton.setWidth(Const.V_HEIGHT / 11.8f);
        leaderboardButton.setPosition(Const.V_WIDTH / 1.9f, Const.V_HEIGHT / 0.71f);
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.app.getType() == Application.ApplicationType.Android)
                    Simulator.androidController.showLeaderboard();
            }
        });

        leaderboardTextButton = new TextButton("LEADERBOARDS", Simulator.textButtonStyle2);
        leaderboardTextButton.setWidth(Const.V_WIDTH / 3f);
        leaderboardTextButton.setHeight(Const.V_HEIGHT / 16f);
        leaderboardTextButton.getLabel().setColor(1f, 0.55f, 0, 1.0f);
        leaderboardTextButton.getLabel().setFontScale(0.7f);
        leaderboardTextButton.setPosition(Const.V_WIDTH / 1.48f, Const.V_HEIGHT / 0.719f);
        leaderboardTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.app.getType() == Application.ApplicationType.Android)
                    Simulator.androidController.showLeaderboard();
            }
        });
    }

    public void scoreBoard() {

        gameover_table = new Table();
        gameover_table.setFillParent(false);
        gameover_table.setBounds(0, 0, Const.V_WIDTH / 1.3f, Const.V_HEIGHT);
        gameover_table.setPosition(Const.V_WIDTH / 1.625f, Const.V_HEIGHT / 1.7f);

        scoreLabel2 = new Label(String.format("SCORE: " + score), new Label.LabelStyle(textFont, Color.WHITE));
        highscoreLabel2 = new Label(String.format("BEST: " + highscore), new Label.LabelStyle(textFont, Color.WHITE));
        tailLengthLabel = new Label(String.format("TAIL: " + tail_length), new Label.LabelStyle(textFont, Color.WHITE));
        tailLengthLabel.setFontScale(0.7f);
        tailLongestLabel = new Label(String.format("BEST TAIL: " + tail_longest), new Label.LabelStyle(textFont, Color.WHITE));
        tailLongestLabel.setFontScale(0.7f);

        gameOverLabel = new Label(String.format("GAME OVER!"), new Label.LabelStyle(textFont, Color.WHITE));
        gameOverLabel.setColor(1.0f, 0.2f, 0.0f, 1);
        gameOverLabel.setFontScaleX(1.75f);
        gameOverLabel.setFontScaleY(1.75f);

        gameover_table.add(gameOverLabel).center();
        gameover_table.row();
        gameover_table.add(scoreLabel2).left().padLeft(200).padBottom(10).padTop(120);
        gameover_table.row();
        gameover_table.add(highscoreLabel2).left().padLeft(200).padBottom(120);
        gameover_table.row();
        gameover_table.add(tailLengthLabel).left().padLeft(200).padBottom(10);
        gameover_table.row();
        gameover_table.add(tailLongestLabel).left().padLeft(200).padBottom(10);


        stage2.addActor(scoreBoard);
        stage2.addActor(gameover_table);
        stage2.addActor(leaderboardButton);
        stage2.addActor(leaderboardTextButton);
    }

    @Override
    public void dispose() {
        stage1.dispose();
        stage2.dispose();
        //options.musics.stop();
        //atlas.dispose();
        //options.dispose();

    }

    public static void hideHUD(boolean isHidden) {
        hudHidden = isHidden;
        scoreLabel.setVisible(!isHidden);
        highscoreLabel.setVisible(!isHidden);
    }
}