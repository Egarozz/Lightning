package main;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class MapLoader {
	private ObjectMap<Integer,Array<TileCell>> mapa;
	private AssetManager manager = new AssetManager();
	
	
	
	public MapLoader() {
		manager.load("Assets.atlas",TextureAtlas.class);
		manager.finishLoading();
		TextureAtlas atlas = manager.get("Assets.atlas");
		
		mapa = new ObjectMap<>();
		TiledMap map = new TmxMapLoader().load("Casa.tmx");
		
		

		//Se itera las capas y cada celda y se coloca en el hashmap con el nombre de layer 
		//y su array con sus respectivos tiles
		//Capa de objetos tirara como null
		for(int k = 0; k < map.getLayers().getCount()-1; k++) {
			Array<TileCell> layer = new Array<>();
			TiledMapTileLayer tileLayer = (TiledMapTileLayer)map.getLayers().get(k);
			
			for(int i = tileLayer.getWidth(); i >= 0; i--) {
				for(int j = 0; j < tileLayer.getHeight(); j++) {
					Cell cell = tileLayer.getCell(j, i);
					if(cell != null) { //Si es null significa que en el tilemap es 0
						int id = cell.getTile().getId();
						layer.add(new TileCell(j*16,i*16,id, atlas.createSprite(String.valueOf(id))));
					}	
				}	
			}			
			mapa.put(k, layer);		
		}
	}
	
	private void renderLayer(SpriteBatch batch, int layer) {
		Array<TileCell> cells = mapa.get(layer);
		for(TileCell cell: cells) {
			batch.draw(cell.sprite, cell.x-100, cell.y-100);
		}
	}
	public void render(SpriteBatch batch) {
		
		renderLayer(batch,0);
		renderLayer(batch,1);
		renderLayer(batch,2);
		
		
	}
				
			
			
		
	
	
	public class TileCell {
		private float x = 0;
		private float y = 0;
		private int id = 0;
		private Sprite sprite;
		public TileCell(float x, float y, int id, Sprite sprite) {
			this.x = x;
			this.y = y;
			this.id = id;
			this.sprite = sprite;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public int getId() {
			return id;
		}
		
	}
}
	