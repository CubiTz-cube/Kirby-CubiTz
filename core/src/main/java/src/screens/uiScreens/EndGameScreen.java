package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import src.main.Main;
import src.screens.game.GameScreen;
import src.screens.components.LayersManager;
import src.utils.ScorePlayer;
import java.util.ArrayList;
import java.util.Collections;

public class EndGameScreen extends UIScreen{
    private final LayersManager layersManager;
    private final GameScreen game;

    private final Label.LabelStyle labelStyle;
    private final Label.LabelStyle labelTitleStyle;

    private final Label titleLabel;
    private final Image pinkLineImage;
    private final ImageTextButton backButton;
    private final ScrollPane scrollPane;
    private final Table scoresTable;

    public EndGameScreen(Main main, GameScreen game) {
        super(main);
        this.game = game;

        labelTitleStyle = new Label.LabelStyle(main.fonts.briBorderFont, Color.WHITE);
        labelStyle = new Label.LabelStyle(main.fonts.interFont, Color.WHITE);

        titleLabel = new Label("Puntuación", new Label.LabelStyle(main.fonts.briTitleFont, Color.WHITE));
        titleLabel.setAlignment(Align.center);

        Texture pinkLineTexture = main.getAssetManager().get("ui/bg/pinkLineBg.png", Texture.class);
        pinkLineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pinkLineImage = new Image(pinkLineTexture);

        backButton = new ImageTextButton("Menu de Inicio", myImageTextbuttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.changeScreen(Main.Screens.MENU);
            }
        });
        backButton.addListener(hoverListener);

        scoresTable = new Table();
        scrollPane = new ScrollPane(scoresTable, main.getSkin());
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);

        layersManager = new LayersManager(stageUI, 5);
    }

    @Override
    public void show() {
        super.show();
        layersManager.setZindex(0);
        layersManager.getLayer().bottom().pad(20);
        layersManager.getLayer().add().expand(5,0);
        layersManager.getLayer().add(backButton).growX();

        layersManager.setZindex(1);
        layersManager.getLayer().top();
        layersManager.getLayer().add(titleLabel).padTop(15).width(398).center();
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(2);
        layersManager.getLayer().top();
        layersManager.getLayer().add(pinkLineImage).padTop(50).expandX().left();

        layersManager.setZindex(3);
        layersManager.getLayer().add(scrollPane).grow();
        scoresTable.top().pad(20).padTop(100);
        scoresTable.add(new Label("Nombre", labelTitleStyle)).expandX().center();
        scoresTable.add(new Label("Monedas", labelTitleStyle)).expandX().center();
        scoresTable.row();
        ArrayList<ScorePlayer> scores = new ArrayList<>(game.getScorePlayers().values());
        Collections.sort(scores);
        for (ScorePlayer score : scores){
            addScoreEntry(score.name, score.score);
        }
    }

    @Override
    public void hide() {
        super.hide();
        layersManager.clear();
        scoresTable.clear();
        game.getScorePlayers().clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.97f,0.55f,0.72f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stageUI.act(delta);
        stageUI.draw();
    }

    private void addScoreEntry(String name, Integer score) {
        scoresTable.add(new Label(name, labelStyle)).expandX().center();
        scoresTable.add(new Label(String.valueOf(score), labelStyle)).expandX().center();
        scoresTable.row();
    }
}
