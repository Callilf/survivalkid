package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.calliltbn.GameScreen;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.util.Mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class RenderingSystem extends IteratingSystem {

    public static final boolean DISPLAY_HITBOX = true;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Collection<Entity> renderQueue;
    private OrthographicCamera cam;
    private Sprite background;

    private float lastLog = 0;

    public RenderingSystem(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super(Family.one(SpriteComponent.class).get());

        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        shapeRenderer.setAutoShapeType(true);
        cam = new OrthographicCamera(GameScreen.SCREEN_W, GameScreen.SCREEN_H);
        cam.position.set(GameScreen.SCREEN_W/2, GameScreen.SCREEN_H/2, 0);

        Comparator<Entity> comparator = new Comparator<Entity>() {
            @Override
            public int compare(Entity t1, Entity t2) {
                SpriteComponent spriteCompo1 = Mappers.getComponent(SpriteComponent.class, t1);
                SpriteComponent spriteCompo2 = Mappers.getComponent(SpriteComponent.class, t2);
                int zIndexDiff = spriteCompo1.getZindex() - spriteCompo2.getZindex();
                // if 0, return 1 (last has priority), else return zIndexDiff
                return (zIndexDiff == 0)? 1 : zIndexDiff;
            }
        };

        renderQueue = new TreeSet<>(comparator);
        //renderQueue = new ArrayList<>();

        // init background
        Texture img = new Texture("images/ground.png");
        background = new Sprite(img);
        //background.setPosition(GameScreen.SCREEN_W - background.getWidth(), GameScreen.SCREEN_H - background.getHeight());
        background.setPosition(0, 0);

    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // log each second number of sprite in the render queue
        if ((lastLog += deltaTime) > 1) {
            lastLog = 0;
            Gdx.app.log("GLOBAL", "Nb Sprite to draw : " + renderQueue.size());
        }

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        background.draw(batch);


        // contained decoration animation when they are over
        List<Entity> deadEntities = new ArrayList<>();

        // contained hitbox to draw
        List<RectangleMapObject> listHitbox = new ArrayList<>();

        for (Entity entity : renderQueue) {
            SpriteComponent spriteCompo = Mappers.getComponent(SpriteComponent.class, entity);
            if (spriteCompo != null) {
                if (spriteCompo.isFinished()) {
                    deadEntities.add(entity);
                    continue;
                }

                spriteCompo.increaseAnimTime(deltaTime);
                if (!spriteCompo.isHide()) {
                    spriteCompo.getSprite().draw(batch);
                }
            }
            if (DISPLAY_HITBOX) {
                CollideComponent collideCompo = Mappers.getComponent(CollideComponent.class, entity);
                if (collideCompo != null) {
                    listHitbox.add(collideCompo.getHitbox());
                }
            }
        }

        // remove dead decoration entity
        for (Entity entity: deadEntities) {
            SystemUtils.removeEntity(getEngine(), entity, false, "dead decoration");
        }

        batch.end();
        renderQueue.clear();
        
        if (!listHitbox.isEmpty()) {
            shapeRenderer.begin(); // ShapeType.Filled
            for (RectangleMapObject hitbox : listHitbox) {
                Rectangle rect = hitbox.getRectangle();
                shapeRenderer.setColor(0, 0, 1, 0.5f);
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            shapeRenderer.end();
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

}
