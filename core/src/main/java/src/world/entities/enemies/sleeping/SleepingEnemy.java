package src.world.entities.enemies.sleeping;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.stateMachine.StateMachine;
import src.world.entities.Entity;
import src.world.entities.enemies.sleeping.states.SleepingState;
import src.world.entities.enemies.sleeping.states.WalkingState;

import static src.utils.Constants.PIXELS_IN_METER;

public class SleepingEnemy extends Entity
{
    private Float timeAct = 0f;
    private final Sprite sprite;
    private final StateMachine stateMachine;
    private final SleepingState sleepingState;
    private final WalkingState walkingState;

    public SleepingEnemy(World world, Texture texture, Rectangle shape, Integer id)
    {
        super(world, id);

        this.sprite = new Sprite(texture);
        sprite.setSize(shape.width * PIXELS_IN_METER, shape.height * PIXELS_IN_METER);

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 2, shape.height / 2);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("enemy");
        box.dispose();
        body.setFixedRotation(true);

        setSize(PIXELS_IN_METER * shape.width, PIXELS_IN_METER * shape.height);

        stateMachine = new StateMachine();
        sleepingState = new SleepingState(stateMachine, this);
        walkingState = new WalkingState(stateMachine, this);
        stateMachine.setState(sleepingState);
    }

    public StateMachine getStateMachine()
    {
        return stateMachine;
    }

    public SleepingState getSleepingState()
    {
        return sleepingState;
    }

    public WalkingState getWalkingState()
    {
        return walkingState;
    }

    @Override
    public void act(float delta)
    {
        stateMachine.update(delta);
    }

    public void walk(float delta)
    {
        timeAct += delta;

        if (timeAct < 3)
        {
            body.setLinearVelocity(-3,body.getLinearVelocity().y);
            setFlipX(true);
        }
        else if(timeAct < 6)
        {
            body.setLinearVelocity(3,body.getLinearVelocity().y);
            setFlipX(false);
        }
        else if (timeAct > 9)
        {
            timeAct = 0f;
        }
    }

    public boolean shouldWakeUp()
    {
        //Despierta tras unos segundos

        return false;
    }

    public boolean shouldSleep()
    {
        //Duerme tras unos segundos

        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        setPosition(
            body.getPosition().x * PIXELS_IN_METER,
            body.getPosition().y * PIXELS_IN_METER
        );
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
        sprite.setOriginCenter();
        sprite.draw(batch);
    }

    @Override
    public void detach()
    {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
