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

import java.util.Comparator;
import java.util.TreeSet;

public class RenderingSystem extends IteratingSystem {

    private SpriteBatch batch;
    private TreeSet<Entity> renderQueue;
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
                return t1.flags - t2.flags;
            }
        };

        renderQueue = new TreeSet<>(comparator);

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
            spriteCompo.getSprite(deltaTime).draw(batch);
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

}
