package src.entities.player.states;

import src.entities.player.Player;
import src.utils.stateMachine.State;
import src.utils.stateMachine.StateMachine;

public abstract class StatePlayer extends State {
    protected Player player;

    public StatePlayer (StateMachine stateMachine, Player player){
        super(stateMachine);
        this.player = player;
    }
}
