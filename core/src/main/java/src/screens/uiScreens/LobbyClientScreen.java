package src.screens.uiScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.main.Main;

public class LobbyClientScreen extends UIScreen {
    private Table playersTable;
    private Integer numPlayersConnected;

    public LobbyClientScreen(Main main) {
        super(main);
        Skin skin = main.getSkin();
        stage.setDebugAll(true);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Lobby", skin);

        playersTable = new Table();

        ScrollPane scrollPane = new ScrollPane(playersTable, skin);
        scrollPane.setFillParent(true);

        TextButton backButton = new TextButton("Salirse", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.closeClient();
                main.closeServer();
                main.changeScreen(Main.Screens.MULTIPLAYER);
            }
        });

        table.add(titleLabel).pad(10);
        table.row();
        table.add(scrollPane).expand().fill();
        table.row();
        table.add(backButton).width(200).height(50).pad(10);
    }

    @Override
    public void show() {
        super.show();
        numPlayersConnected = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (numPlayersConnected != main.client.getPlayersConnected().size()) {
            numPlayersConnected = main.client.getPlayersConnected().size();
            updatePlayersTable();
        }
        if (main.client.gameStart){
            main.changeScreen(Main.Screens.GAME);
        }
    }

    private void updatePlayersTable() {
        playersTable.clear();
        for (String player : main.client.getPlayersConnected().values()) {
            playersTable.add(new Label(player, main.getSkin())).pad(25);
            playersTable.row();
        }
    }

    @Override
    public void hide() {
        playersTable.clear();
    }
}
