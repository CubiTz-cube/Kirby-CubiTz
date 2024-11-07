package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.world.player.Player;
import src.utils.stateMachine.StateMachine;

public class IdleState extends CanMoveState{
    public IdleState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.setAnimation(Player.AnimationType.IDLE);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.setState(Player.StateType.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.setState(Player.StateType.JUMP);
        }
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.x != 0) player.setState(Player.StateType.WALK);
        if(velocity.y < -1f) player.setState(Player.StateType.FALL);
    }

    @Override
    public void end() {

    }
}
