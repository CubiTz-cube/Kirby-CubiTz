package src.world.entities.enemies.basic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import src.utils.CollisionFilters;
import src.utils.animation.SheetCutter;
import src.world.entities.enemies.Enemy;
import src.world.entities.enemies.basic.states.*;

import static src.utils.variables.Constants.PIXELS_IN_METER;

public class BasicEnemy extends Enemy {
    private final BitmapFont font;
    private final GlyphLayout layout;

    public enum AnimationType {
        IDLE,
        WALK
    }
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkAnimation;

    public BasicEnemy(World world, Rectangle shape, AssetManager assetManager, Integer id) {
        super(world, shape, assetManager,id);
        type = Type.BASIC;
        this.font = assetManager.get("ui/default.fnt", BitmapFont.class);
        this.layout = new GlyphLayout();

        BodyDef def = new BodyDef();
        def.position.set(shape.x + (shape.width-1) / 2, shape.y + (shape.height-1)/ 2);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(shape.width/4, shape.height/4);
        fixture = body.createFixture(box, 1);
        fixture.setUserData(this);
        box.dispose();
        body.setFixedRotation(true);

        Filter filter = new Filter();
        filter.categoryBits = ~CollisionFilters.CATEGORY_ENEMY;
        filter.maskBits = CollisionFilters.MASK_ENEMY;
        fixture.setFilterData(filter);

        setSpritePosModification(0f, getHeight()/4);

        idleState = new IdleStateBasic(this);
        walkState = new WalkStateBasic(this);
        setState(StateType.IDLE);

        idleAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/basic/basicIdle.png"), 1));

        walkAnimation = new Animation<>(0.12f,
            SheetCutter.cutHorizontal(assetManager.get("world/entities/basic/basicWalk.png"), 8));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        setCurrentAnimation(idleAnimation);
    }

    public void setAnimation(AnimationType type){
        switch (type){
            case IDLE:
                setCurrentAnimation(idleAnimation);
                break;
            case WALK:
                setCurrentAnimation(walkAnimation);
                break;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        layout.setText(font, "ID " + getId() + " TIME " + getActCrono());
        font.draw(batch, layout, getX() + layout.width / 2, getY() + sprite.getHeight() + layout.height);
    }
}
