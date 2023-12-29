package com.art.prototype;

import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.editor.LevelData;
import com.art.prototype.input.WorldInteraction;
import com.art.prototype.resources.ResourceManager;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
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

		ResourceManager resourceManager = API.get(ResourceManager.class);
		resourceManager.startLoading();
		while (!resourceManager.updateLoading()) {
			resourceManager.updateLoading();
		}

		//viewport
		extendViewport = new ExtendViewport(100, 50);
		camera = extendViewport.getCamera();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); // Center the camera

		world = API.get(World.class);

		///ui
		ScreenViewport uiViewport = new ScreenViewport();
		uiViewport.setUnitsPerPixel(2f);
		gameUI = new GameUI(uiViewport, spriteBatch);
		API.register(gameUI);

		player = new Player();
		world.setPlayer(player);
		ground = Platform.MAKE_GROUND_PLATFORM();

		WorldInteraction worldInteraction = new WorldInteraction();
		worldInteraction.setWorldRef(world);
		worldInteraction.setExtendViewport(extendViewport);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(worldInteraction);
		multiplexer.addProcessor(gameUI.getStage());

		Gdx.input.setInputProcessor(multiplexer);




		System.out.println("LOAD FINISHED");


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
			String directoryPath = "savedLevels";

			// Get the directory as a FileHandle
			FileHandle dirHandle = Gdx.files.internal(directoryPath);

			// List all files in the directory
			FileHandle[] files = dirHandle.list();

			// Iterate through the files and print their names
			String name = "";
			if (files.length > 0) {
				name = files[0].file().getName();
			}

			String finalName = directoryPath + "/" + name;

			final LevelData levelData = API.get(Editor.class).loadLevelData(finalName);
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
