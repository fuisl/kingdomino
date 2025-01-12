package dev.kingdomino.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.TerrainType;

public class GameScreen extends AbstractScreen {
    private Stage stage;
    private GameManager gameManager;
    private ScreenViewport screenViewport;
    private TextureRegion[] crownOverlay;
    private MainBoardActor mainBoardActor;
    private SideBoardManager sideBoardManager;
    private Table rootTable;
    private Skin skin;

    public GameScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
        super(spriteBatch, assetManager);
        screenViewport = new ScreenViewport();
        stage = new Stage(screenViewport);
        gameManager = new GameManager();

        // TODO remove later, just pinging to get it to be alive... I assume
        // why do you need to be pinged twice...?
        gameManager.update(0f);
        gameManager.update(0f);
        
        TextureAtlas atlas = assetManager.get("tileTextures.atlas");
        skin = assetManager.get("skin/uiskin.json");

        for (TerrainType name : TerrainType.values()) {
            name.setTexture(atlas.findRegion(name.name().toLowerCase()));
        }

        crownOverlay = new TextureRegion[4];
        crownOverlay[0] = atlas.findRegion("nocrown");
        crownOverlay[1] = atlas.findRegion("onecrown");
        crownOverlay[2] = atlas.findRegion("twocrown");
        crownOverlay[3] = atlas.findRegion("threecrown");
    }

    @Override
    public void initScreen() {
        rootTable = new Table();
        rootTable.setFillParent(true);
        // TODO remove this line once we are done with layout
        rootTable.setDebug(true);
        stage.addActor(rootTable);

        Table leftInfoLayout = new Table();
        Table rightInfoLayout = new Table();
        Table mainGameLayout = new Table();

        leftInfoLayout.add(new Label("Left Info", skin));
        leftInfoLayout.row();
        mainGameLayout.add(new Label("Main Game Area", skin));
        mainGameLayout.row();
        rightInfoLayout.add(new Label("Right Info", skin));
        rightInfoLayout.row();

        mainBoardActor = new MainBoardActor(crownOverlay, screenViewport);
        sideBoardManager = new SideBoardManager(gameManager, crownOverlay, screenViewport);

        mainGameLayout.add(mainBoardActor).width(500).height(500).fill();

        for (SideBoardActor actor : sideBoardManager.getSideBoardActors()) {
            rightInfoLayout.add(actor).width(300).height(300).fill();
            rightInfoLayout.row();
        }

        rootTable.add(leftInfoLayout).width(150).expandY().fill();
        rootTable.add(mainGameLayout).expand().fill();
        rootTable.add(rightInfoLayout).width(300).expandY().fill();
    }

    @Override
    public void render(float delta) {
        gameManager.update(delta);

        // TODO remove this as we are now rendering from the UI
        // keep for debugging purpose only.
        gameManager.render(spriteBatch);

        // update the actors with new informations
        // TODO remove this once we have a proper screen covering this part
        // in theory we can supplement a default board to deal with
        if (gameManager.getCurrentKing() == null) return;

        sideBoardManager.updateSideBoard();
        mainBoardActor.setBoard(gameManager.getBoard().getLand());
        mainBoardActor.setCurrentDomino(gameManager.getCurrentDomino());

        ScreenUtils.clear(Color.DARK_GRAY);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
