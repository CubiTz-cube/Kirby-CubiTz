package src.world.entities.projectiles.enemyProyectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import src.screens.GameScreen;
import src.utils.animation.SheetCutter;
import src.utils.constants.CollisionFilters;
import src.world.ActorBox2d;
import src.world.entities.enemies.Enemy;
import src.world.entities.player.Player;
import src.world.entities.player.PlayerCommon;
import src.world.entities.projectiles.Projectil;

public class IceEnemyProyectil extends Projectil {
    private Float time;
    private Sound impactSound;

    public IceEnemyProyectil(World world, Rectangle shape, AssetManager assetManager, Integer id, GameScreen game) {
        super(world, shape, assetManager, id, Type.BOMB, game, 3);
        time = 0f;

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width - 1) / 2, shape.y + (shape.height - 1) / 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width / 4, shape.height / 4);
        fixture = body.createFixture(box, 2);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        setSpritePosModification(0f, getHeight()/4);

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilters.PROJECTIL;
        filter.maskBits = (short)(~CollisionFilters.ITEM & ~CollisionFilters.ENEMY);
        fixture.setFilterData(filter);

        Animation<TextureRegion> animation = new Animation<>(0.5f,
            SheetCutter.cutHorizontal(assetManager.get("world/particles/iceParticle.png"), 6));
        setCurrentAnimation(animation);

        impactSound = assetManager.get("sound/kirby/kirbyIceDamage.wav");
    }

    @Override
    public void act(float delta) {
        time += delta;
        if (time > 3f) {
            despawn();
        }
    }

    @Override
    public synchronized void beginContactWith(ActorBox2d actor, GameScreen game) {
        super.beginContactWith(actor, game);
        if (actor instanceof Enemy enemy) {
            if (enemy.getCurrentStateType() != Enemy.StateType.DAMAGE) game.playProximitySound(impactSound, body.getPosition(), 25);
        }
        if (actor instanceof Player player) {
            if (!player.isInvencible()) game.playProximitySound(impactSound, body.getPosition(), 25);
        }
    }
}
