package src.world.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import src.utils.stateMachine.StateMachine;
import src.world.player.Player;

public class FallState extends CanMoveState{
    public FallState(StateMachine stateMachine, Player player){
        super(stateMachine, player);
    }

    @Override
    public void start() {
        player.getSprite().setColor(Color.PURPLE);
    }

    @Override
    public void update(Float delta) {
        super.update(delta);
        Vector2 velocity = player.getBody().getLinearVelocity();
        if (velocity.y == 0){
            if (velocity.x == 0)  stateMachine.setState(player.getIdleState());
            else stateMachine.setState(player.getWalkState());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            stateMachine.setState(player.getFlyState());
        }
    }

    @Override
    public void end() {

    }
}
