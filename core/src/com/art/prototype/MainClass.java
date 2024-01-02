package com.art.prototype;

import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.input.CameraController;
import com.art.prototype.input.InputManager;
import com.art.prototype.input.MainInput;
import com.art.prototype.input.WorldInteraction;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.resources.ResourceManager;
import com.art.prototype.ui.Colors;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
		//init API;
		API.getInstance();

		ResourceManager resourceManager = API.get(ResourceManager.class);
		resourceManager.startLoading();
		while (!resourceManager.updateLoading()) { // TODO: 12/30/2023 this is not working
			resourceManager.updateLoading();
		}

		world = API.get(World.class);

		///ui
		Graphics2D graphics = API.get(Graphics2D.class);
		gameUI = new GameUI(graphics.getUiViewport(), graphics.getBatch());
		API.register(gameUI);

		player = new Player();
		world.setPlayer(player);
		ground = Platform.MAKE_GROUND_PLATFORM();


		API.get(Editor.class).setupHighlighter(); // TODO: 12/30/2023 fix this shit

		InputManager inputManager = API.get(InputManager.class);
		inputManager.addUIProcessor(); // TODO: 12/30/2023 fix this shit too
		inputManager.registerProcessor(WorldInteraction.class);
		inputManager.registerProcessor(MainInput.class);
		inputManager.enableAll();


		world.addDynamicBody(player);
		world.addStaticBody(ground);

		this.extendViewport = graphics.getGameViewport();
		this.shapeRenderer = graphics.getShapeRenderer();
		this.camera = graphics.getGameCamera();
		this.spriteBatch = graphics.getBatch();
	}

	@Override
	public void render () {
		ScreenUtils.clear(Colors.SPACE);
		final float deltaTime = Gdx.graphics.getDeltaTime();
		API.get(CameraController.class).update(deltaTime);

		extendViewport.apply();
		shapeRenderer.setProjectionMatrix(camera.combined);
		player.update(deltaTime);
		world.doPhysicsStep(deltaTime);

		shapeRenderer.begin();
		shapeRenderer.setColor(Color.WHITE);
		world.draw(shapeRenderer);
		world.drawDebug(shapeRenderer);

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
