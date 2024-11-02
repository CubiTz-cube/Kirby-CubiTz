package src.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import src.net.packets.Packet;
import src.screens.worldScreens.GameScreen;
import src.world.entities.enemies.Enemy;
import src.world.entities.otherPlayer.OtherPlayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TransferQueue;

public class Client implements Runnable{
    private final GameScreen game;
    private final String ip;
    private final Integer port;

    private final String name;
    private final HashMap<Integer, String> playersConnected;
    public Boolean gameStart = false;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Boolean running = false;

    public Client(GameScreen game, String ip, int port, String name){
        this.game = game;
        this.ip = ip;
        this.port = port;
        this.name = name;
        playersConnected = new HashMap<>();
        playersConnected.put(-1, name);
    }

    public HashMap<Integer, String> getPlayersConnected() {
        return this.playersConnected;
    }

    public String getName() {
        return name;
    }

    public Boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        try {
            SocketHints hints = new SocketHints();
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, hints);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            running = true;
        } catch (IOException e){
            System.out.println("Error al conectar cliente: " + e.getMessage());
        }

        int packId;
        float x,y;
        send(Packet.connect(name));
        try {
            while (running) {

                Object[] pack = (Object[])in.readObject();
                Packet.Types type = (Packet.Types) pack[0];
                if (!type.equals(Packet.Types.POSITION)) System.out.println("[Client] Recibido: " + type);
                switch (type){
                    case NEWPLAYER:
                        packId = (Integer) pack[1];
                        String name = (String) pack[2];
                        playersConnected.put(packId, name);
                        game.addActor(new OtherPlayer(game.getWorld(), game.main.getAssetManager(), new Rectangle(0, 10, 1.5f, 1.5f), packId, name));
                        break;

                    case NEWENTITY:
                        packId = (Integer) pack[1];
                        Enemy.Type packType = (Enemy.Type) pack[2];
                        x = (Float) pack[3];
                        y = (Float) pack[4];
                        game.addEnemy(packType, new Vector2(x,y), packId);
                        break;

                    case DISCONNECTPLAYER:
                        packId = (Integer) pack[1];
                        playersConnected.remove(packId);
                        game.removeEntity(packId);
                        break;

                    case GAMESTART:
                        gameStart = true;
                        break;

                    case POSITION:
                        packId = (Integer) pack[1];
                        x = (Float) pack[2];
                        y= (Float) pack[3];
                        game.actEntity(packId, x, y);
                        break;
                }
            }
        } catch (SocketException | EOFException e) {
            Gdx.app.log("Client", "Socket cerrado");
        }catch (IOException e) {
            Gdx.app.log("Client", "Error al recibir mensaje", e);
        } catch (ClassNotFoundException e){
            Gdx.app.log("Client", "Error al procesar mensaje", e);
        } finally {
            close();
        }
    }

    public void send(Object[] data){
        try {
            out.writeObject(data);
        }catch (IOException e){
            Gdx.app.log("Client", "Error al enviar mensaje", e);
        }
    }

    public void close(){
        if (!running) return;
        running = false;
        socket.dispose();
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            Gdx.app.log("Client", "Error al cerrar socket: ", e);
        }

    }
}
