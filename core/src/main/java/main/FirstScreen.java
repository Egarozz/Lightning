package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
	private MapLoader loader;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	FrameBuffer lightBuffer;
	TextureRegion lightBufferRegion;
	
	FrameBuffer lightBuffer2;
	TextureRegion lightBufferRegion2;
	public FirstScreen() {
		loader = new MapLoader();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 320, 320);
		
	}
	
	@Override
	public void show() {
		// Prepare your screen here.
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		lightBuffer.begin();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ZERO);
		Gdx.gl.glClearColor(0.3f,0.38f,0.4f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		loader.render(batch);
		batch.end();
		lightBuffer.end();
		
		Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA);
		
		batch.begin();
		batch.draw(lightBufferRegion, 0, 0);  
		
		batch.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		if(Gdx.input.isKeyPressed(Keys.Z)) {
			camera.zoom -= 0.1f;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			camera.position.x -= 1;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			camera.position.x += 1;
		}
		if(Gdx.input.isKeyPressed(Keys.W)) {
			camera.position.y += 1;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			camera.position.y -= 1;
		}
	}

	@Override
	public void resize(int width, int height) {
		if (lightBuffer!=null) lightBuffer.dispose();
		lightBuffer = new FrameBuffer(Format.RGBA8888, 1000, 1000, false);
		lightBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture(),0,0,1000,1000);
		lightBufferRegion.flip(false, false);
		
	}

	@Override
	public void pause() {
		// Invoked when your application is paused.
	}

	@Override
	public void resume() {
		// Invoked when your application is resumed after pause.
	}

	@Override
	public void hide() {
		// This method is called when another screen replaces this one.
	}

	@Override
	public void dispose() {
		// Destroy screen's assets here.
	}
}