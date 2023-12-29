package com.art.prototype;

import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.editor.LevelData;
import com.art.prototype.input.MovementProcessor;
import com.art.prototype.input.WorldInteraction;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainClass extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	private ExtendViewport extendViewport;
	private Camera camera;
	private World world;
	private Player player;
	private Platform ground;
	private GameUI gameUI;
	private SpriteBatch spriteBatch;

	
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		final API instance = API.getInstance();

		//viewport
		extendViewport = new ExtendViewport(100, 50);
		camera = extendViewport.getCamera();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera

		world = API.get(World.class);

		///ui
		ScreenViewport uiViewport = new ScreenViewport();
		gameUI = new GameUI(uiViewport, spriteBatch);
		API.register(gameUI);

		player = new Player();
		world.setPlayer(player);
		ground = Platform.MAKE_GROUND_PLATFORM();

		MovementProcessor movementProcessor = new MovementProcessor();
		WorldInteraction worldInteraction = new WorldInteraction();
		worldInteraction.setWorldRef(world);
		worldInteraction.setExtendViewport(extendViewport);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(movementProcessor);
		multiplexer.addProcessor(worldInteraction);

 		movementProcessor.setPlayer(player);
		Gdx.input.setInputProcessor(multiplexer);



		world.addDynamicBody(player);
		world.addStaticBody(ground);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.3f, 0.3f, 0.3f, 1);


		extendViewport.apply();
		shapeRenderer.setProjectionMatrix(camera.combined);
		final float deltaTime = Gdx.graphics.getDeltaTime();
		world.doPhysicsStep(deltaTime);
		player.update(deltaTime);

		shapeRenderer.begin();
		shapeRenderer.setColor(Color.WHITE);
		world.draw(shapeRenderer);

		if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			API.get(Editor.class).saveToFile();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			final LevelData levelData = API.get(Editor.class).loadLevelData("levelData.json");
			API.get(World.class).loadFromLevelData(levelData);
		}
		shapeRenderer.end();

		gameUI.act();
		gameUI.draw();
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
		spriteBatch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		extendViewport.update(width, height);
		final Viewport viewport = gameUI.getStage().getViewport();
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera
	}
}
