package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.calliltbn.GameScreen;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.util.Mappers;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class RenderingSystem extends IteratingSystem {

    private SpriteBatch batch;
    private Collection<Entity> renderQueue;
    private OrthographicCamera cam;
    private Sprite background;

    public RenderingSystem(SpriteBatch batch) {
        super(Family.one(SpriteComponent.class).get());

        this.batch = batch;
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

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        background.draw(batch);

        for (Entity entity : renderQueue) {
            SpriteComponent spriteCompo = Mappers.getComponent(SpriteComponent.class, entity);
            if (spriteCompo == null) {
                continue;
            }
            // spriteCompo.getSprite().setPosition(... calcul new position ...);
            spriteCompo.increaseAnimTime(deltaTime);
            if (!spriteCompo.isHide()) {
                spriteCompo.getSprite().draw(batch);
            }
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

}
