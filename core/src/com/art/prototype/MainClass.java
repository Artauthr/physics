package com.art.prototype;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MainClass extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private ExtendViewport extendViewport;
	private Camera camera;
	private World world;
	private Player player;
	private Platform platform;
	private Platform platform1;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		world = new World();

		player = new Player();
		platform = Platform.MAKE_GROUND_PLATFORM();
		platform1 = Platform.MAKE_RANDOM();

		MovementProcessor movementProcessor = new MovementProcessor();
		movementProcessor.setPlayer(player);
		Gdx.input.setInputProcessor(movementProcessor);

		world.addDynamicBody(player);
		world.addStaticBody(platform);
		world.addStaticBody(platform1);

		//viewport
		extendViewport = new ExtendViewport(100, 50);
		camera = extendViewport.getCamera();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.3f, 0.3f, 0.3f, 1);

		extendViewport.apply();
		shapeRenderer.setProjectionMatrix(camera.combined);
		final float deltaTime = Gdx.graphics.getDeltaTime();
		world.tick(deltaTime);
		player.update(deltaTime);

		shapeRenderer.begin();
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.circle(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 1f, 100);
		player.draw(shapeRenderer);
		platform.draw(shapeRenderer);
		platform1.draw(shapeRenderer);
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		extendViewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
	}
}
