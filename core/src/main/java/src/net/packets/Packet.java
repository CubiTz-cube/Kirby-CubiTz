package src.net.packets;

import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.player.Player;

public class Packet {
    public static enum Types{
    CONNECT, DISCONNECTPLAYER, POSITION, NEWPLAYER, GAMESTART, NEWENTITY, ACTENEMY, ACTOTHERPLAYER, REMOVEENTITY
    }

    public static Object[] connect(String name){
        return new Object[]{Types.CONNECT, name};
    }

    public static Object[] disconnectPlayer(Integer id){
        return new Object[]{Types.DISCONNECTPLAYER, id};
    }

    public static Object[] position(Integer id,Float x, Float y){
        return new Object[]{Types.POSITION, id, x, y};
    }

    public static Object[] newPlayer(Integer id,String name){
        return new Object[]{Types.NEWPLAYER, id, name};
    }

    public static Object[] gameStart(){
        return new Object[]{Types.GAMESTART};
    }

    public static Object[] newEntity(Integer id, Entity.Type type, Float x, Float y){
        return new Object[]{Types.NEWENTITY, id, type, x, y};
    }

    public static Object[] actEnemy(Integer id, Enemy.StateType state, Float cronno, Boolean flipX){
        return new Object[]{Types.ACTENEMY, id, state, cronno, flipX};
    }

    public static Object[] actOtherPlayer(Integer id, Player.AnimationType animationType, Boolean flipX){
        return new Object[]{Types.ACTOTHERPLAYER, id, animationType, flipX};
    }

    public static Object[] removeEntity(Integer id){
        return new Object[]{Types.REMOVEENTITY, id};
    }

}
