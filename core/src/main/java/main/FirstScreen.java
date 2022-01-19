package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
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
	
	FrameBuffer m_fbo;
	TextureRegion m_fboRegion=null;
	
	public static int lowDisplayW;
	public static int lowDisplayH;
	public static int displayW;
	public static int displayH;
	public Texture light;
	public Texture light2;
	
	public float xofset = 0;
	public float yofset = 0;
			

	public FirstScreen() {
		loader = new MapLoader();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(320,320);
		
		light = new Texture(Gdx.files.internal("Light.png"));
		light2 = new Texture(Gdx.files.internal("Light.png"));
	}
	
	@Override
	public void show() {
		// Prepare your screen here.
	}

	@Override
	public void render(float delta) {
//		Gdx.gl.glClearColor(0,0,0,0);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		m_fbo.begin();		
      	Gdx.gl.glDisable(GL20.GL_BLEND);
      	Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
      	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      	camera.setToOrtho(false, 320, 320);
      	camera.update();
      	batch.setProjectionMatrix(camera.combined);
      	batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
      	batch.begin();
      	loader.render(batch);
      	batch.end();
      	m_fbo.end();
      	
      	lightBuffer.begin();
        Gdx.gl.glClearColor(0.5f,0.5f,0.5f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        batch.begin();
        batch.setColor(1f,1f,1,0.8f);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, -1);
		batch.draw(light,87, 215);
		
		batch.setColor(0f,0f,1f,1f);
		batch.draw(light,180, 100);
		batch.end();
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
	    lightBuffer.end();
	    
	    if(m_fbo != null) {
			camera.setToOrtho(false, 720, 420);
			camera.update();
			
			batch.begin();        
			batch.setProjectionMatrix(camera.combined);
			batch.disableBlending(); //setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			batch.setColor(1,1,1,1);
			batch.draw(m_fboRegion, xofset, yofset, displayW, displayH);
			batch.end();
			batch.enableBlending();
		 }
	    
	    batch.begin();
        batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		batch.draw(lightBufferRegion, xofset, yofset,displayW,displayH);               
		batch.end();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glDisable(GL20.GL_BLEND);
		camera.setToOrtho(true, displayW, displayH);
		camera.update();

		
		
		if(Gdx.input.isKeyPressed(Keys.Z)) {
			camera.zoom -= 0.1f;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			camera.position.x -= 1;
			xofset +=1;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			camera.position.x += 1;
			xofset -=1;
		}
		if(Gdx.input.isKeyPressed(Keys.W)) {
			camera.position.y += 1;
			yofset -=1;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			camera.position.y -= 1;
			yofset +=1;
		}
	}

	@Override
	public void resize(int width, int height) {
		displayW=width;
		displayH=height;
		
		lowDisplayH=width;
		lowDisplayW=height;
		
		if (m_fbo!=null) m_fbo.dispose();
	    m_fbo = new FrameBuffer(Format.RGB888, lowDisplayW, lowDisplayH, false);
	    m_fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	    m_fboRegion = new TextureRegion(m_fbo.getColorBufferTexture(),0,0,lowDisplayW,lowDisplayH);
	    m_fboRegion.flip(false, true);
		
		if (lightBuffer!=null) lightBuffer.dispose();
	    lightBuffer = new FrameBuffer(Format.RGBA8888, lowDisplayW, lowDisplayH, false);
	    lightBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	    lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture(),0,0,lowDisplayW,lowDisplayH);
	    lightBufferRegion.flip(false, true);
	    
	    camera.setToOrtho(true, lowDisplayW,lowDisplayH);
		
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