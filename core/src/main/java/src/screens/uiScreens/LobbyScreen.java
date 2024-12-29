package src.screens.uiScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import src.main.Main;
import src.net.PacketListener;
import src.net.PlayerInfo;
import src.net.packets.Packet;
import src.screens.components.ColorField;
import src.screens.components.LayersManager;
import src.screens.components.SpriteAsActor;
import src.utils.FontCreator;
import src.utils.constants.MyColors;

public class LobbyScreen extends UIScreen implements PacketListener {
    private final Table playersTable;

    private final ScrollPane scrollPane;
    private final ImageTextButton playButton;
    private final ColorField colorField;

    private final BitmapFont fontBriBorder;
    private final BitmapFont nameInterBorder;

    private final Image mainKirbyImage;
    private final Label mainKirbyName;

    public LobbyScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();

        FreeTypeFontGenerator generatorBri = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 4;
        parameter.borderColor = MyColors.BLUE;
        parameter.shadowColor = MyColors.BLUE;
        parameter.shadowOffsetX = -2;
        parameter.shadowOffsetY = 2;
        fontBriBorder = FontCreator.createFont(48, MyColors.YELLOW, generatorBri, parameter);

        FreeTypeFontGenerator generatorInter = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Inter/Inter_28pt-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 3;
        parameter.borderColor = Color.BLACK;
        nameInterBorder = FontCreator.createFont(32, MyColors.PINK, generatorInter, parameter);

        Label titleLabel = new Label("Partida", new Label.LabelStyle(fontBriBorder, Color.WHITE));
        titleLabel.setAlignment(Align.center);

        Texture pinkLineTexture = main.getAssetManager().get("ui/bg/pinkLineBg.png", Texture.class);
        pinkLineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image pinkLineImage = new Image(pinkLineTexture);

        Texture lineTexture = main.getAssetManager().get("ui/bg/lineBg.png", Texture.class);
        lineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image lineImage = new Image(lineTexture);

        Texture aroColorTexture = main.getAssetManager().get("ui/bg/aroColorPlayerBg.png", Texture.class);
        lineTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image aroColorImage = new Image(aroColorTexture);
        aroColorImage.setScaling(Scaling.fit);
        aroColorImage.setAlign(Align.bottomLeft);

        mainKirbyName = new Label("Sin nombre", new Label.LabelStyle(nameInterBorder, MyColors.PINK));
        mainKirbyName.setAlignment(Align.center);
        mainKirbyImage = new Image(main.getAssetManager().get("ui/bg/kirbyIdleBg.png", Texture.class));
        mainKirbyImage.setScaling(Scaling.fit);
        mainKirbyImage.setAlign(Align.bottomLeft);

        playersTable = new Table();
        scrollPane = new ScrollPane(playersTable, skin);
        scrollPane.setFillParent(true);

        playButton = new ImageTextButton("Empezar", myImageTextbuttonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.client.send(Packet.gameStart());
            }
        });

        TextureRegionDrawable drawableUp = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exit.png", Texture.class));
        TextureRegionDrawable drawableHover = new TextureRegionDrawable(main.getAssetManager().get("ui/buttons/exitHover.png", Texture.class));
        drawableHover.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        drawableUp.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = drawableUp;
        style.imageOver = drawableHover;

        ImageButton backButton = new ImageButton(style);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.closeClient();
                main.closeServer();
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });

        colorField = new ColorField(skin);
        colorField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.playerColor.set(colorField.getColor());
                mainKirbyImage.setColor(colorField.getColor());
                main.client.send(Packet.actEntityColor(-1, main.playerColor.r, main.playerColor.g, main.playerColor.b, main.playerColor.a));
            }
        });

        LayersManager layersManager = new LayersManager(stageUI, 9);

        layersManager.setZindex(0);
        layersManager.getLayer().setDebug(true);
        layersManager.getLayer().add().expand(0,8);
        layersManager.getLayer().row();
        layersManager.getLayer().bottom().padLeft(20);
        layersManager.getLayer().add(mainKirbyName).left().growX().padBottom(10);
        layersManager.getLayer().add().expand(18,0);
        layersManager.getLayer().row();
        layersManager.getLayer().add(mainKirbyImage).grow();

        layersManager.setZindex(1);
        layersManager.getLayer().bottom();
        layersManager.getLayer().add().expand(0,6);
        layersManager.getLayer().row();
        layersManager.getLayer().add(aroColorImage).grow();
        layersManager.getLayer().add(colorField).grow().pad(10).padBottom(120).padTop(120);
        layersManager.getLayer().add(playButton).grow().pad(10).padBottom(120).padTop(120);

        layersManager.setZindex(2);
        layersManager.getLayer().top();
        layersManager.getLayer().add().expandX();
        layersManager.getLayer().add(backButton).size(64).pad(25);

        layersManager.setZindex(3);
        scrollPane.setForceScroll(true, false);
        playersTable.bottom();
        layersManager.getLayer().add(scrollPane).expand().fill();
        layersManager.getLayer().row();
        layersManager.getLayer().add().expand(0,2);

        layersManager.setZindex(4);
        layersManager.getLayer().top();
        layersManager.getLayer().add(titleLabel).padTop(25).width(398).center();
        layersManager.getLayer().add().expandX();

        layersManager.setZindex(5);
        layersManager.getLayer().top();
        layersManager.getLayer().add(pinkLineImage).padTop(50).expandX().left();

        layersManager.setZindex(6);
        layersManager.getLayer().top();
        layersManager.getLayer().add(lineImage).expandX().fillX().padTop(76);

    }

    @Override
    public void show() {
        super.show();
        playButton.setVisible(main.server != null);
        mainKirbyName.setText(main.getName());
        main.client.addListener(this);
        updatePlayersTable();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void updatePlayersTable() {
        playersTable.clear();
        for (PlayerInfo player : main.client.getPlayersConnected().values()) {
            Label nameLabel = new Label(player.getName(), new Label.LabelStyle(nameInterBorder, MyColors.PINK));
            nameLabel.setAlignment(Align.center);
            playersTable.add(nameLabel).pad(25).expandX().fillX().center();
        }
        playersTable.row();

        for (PlayerInfo player : main.client.getPlayersConnected().values()) {
            Image kirbyImage = new Image(main.getAssetManager().get("ui/bg/kirbyIdleBg.png", Texture.class));
            kirbyImage.setColor(player.getColor());
            kirbyImage.setScaling(Scaling.fit);
            playersTable.add(kirbyImage).pad(10).expandX().fillX().center();
        }
    }

    @Override
    public void hide() {
        playersTable.clear();
    }

    @Override
    public void receivedPacket(Packet.Types type) {
        if (type.equals(Packet.Types.GAMESTART)) {
            main.changeScreen(Main.Screens.GAME);
        }
        if (main.client.gameStart) return;
        if (type.equals(Packet.Types.NEWPLAYER) || type.equals(Packet.Types.DISCONNECTPLAYER) || type.equals(Packet.Types.ACTENTITYCOLOR)) {
            updatePlayersTable();
        }
    }

    @Override
    public void closeClient() {
        if (main.client.gameStart) return;
        Gdx.app.postRunnable(() -> main.changeScreen(Main.Screens.MULTIPLAYER));
    }

    @Override
    public void dispose() {
        super.dispose();
        fontBriBorder.dispose();
        nameInterBorder.dispose();
    }
}
