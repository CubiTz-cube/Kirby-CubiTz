package src.world.entities.enemies.fly.states;

import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.StateEnemy;
import src.world.entities.enemies.fly.FlyEnemy;

public class IdleStateFly extends StateEnemy<FlyEnemy> {

    private boolean flip = false;

    public IdleStateFly(FlyEnemy enemy) {
        super(enemy);
    }

    @Override
    public void start() {
        super.start();
        enemy.setAnimation(FlyEnemy.AnimationType.IDLE);
        //enemy.getBody().setLinearVelocity(0,0);
        enemy.getBody().setGravityScale(0);
        enemy.flyDown = !enemy.flyDown;
    }

    @Override
    public void update(Float delta) {
        if (enemy.getActCrono() > 0.5f) {
            enemy.setState(Enemy.StateType.WALK);
        }
    }

    @Override
    public void end() {
        flip = false;
    }
}
