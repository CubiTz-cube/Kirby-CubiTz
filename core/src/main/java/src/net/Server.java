package src.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import src.screens.worldScreens.GameScreen;
import java.io.IOException;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    public final GameScreen game;
    private final String ip;
    private final Integer port;
    private Boolean running;
    private final CopyOnWriteArrayList<ClientListener> users;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private ServerSocket serverSocket;

    public Server(GameScreen game, String ip, Integer port){
        this.game = game;
        this.ip = ip;
        this.port = port;
        this.users = new CopyOnWriteArrayList<>();
        this.running = false;

        ServerSocketHints hints = new ServerSocketHints();
        hints.acceptTimeout = 0;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, hints);
        running = true;
    }

    public void sendAll(Object[] data, Integer id){
        synchronized (users) {
            for (ClientListener user : users) {
                if (!user.isRunning() || user.getId().equals(id)) continue;
                user.send(data);
            }
        }
    }

    public void removeUser(ClientListener user){
        users.remove(user);
    }

    @Override
    public void run() {
        System.out.println("Server abierto en " + ip + ":" + port);

        try {
            while (running) {
                Socket clientSocket = serverSocket.accept(null);
                ClientListener newUser = new ClientListener(this,clientSocket, users.size()+1);
                pool.execute(newUser);
                users.add(newUser);
            }
        } catch (GdxRuntimeException e) {
            Gdx.app.log("Server", "socket cerrado");
        }

    }

    public void close(){
        running = false;

        synchronized (users) {
            for (ClientListener user : users) {
                user.close();
                //user.send(Packet.serverClose());
            }
        }
        serverSocket.dispose();

        pool.shutdown();
    }
}
