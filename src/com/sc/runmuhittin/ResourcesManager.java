package com.sc.runmuhittin;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.ActivityUtils;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
 
public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public GameActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;
    
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;
        
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    
    public Font font;
    
    public ITextureRegion game_background_region;
    private BuildableBitmapTextureAtlas game_texture_atlas;
        
    // Game Texture Regions
    public ITextureRegion book_region;
    public ITextureRegion cactus_region, car_region, bus_region;
    public ITextureRegion pipe_big_region;
    public ITextureRegion coin_region;
    public ITextureRegion grass_region;
    public ITextureRegion complete_window_region;
    
    public ITiledTextureRegion player_region;
    
    
    public Music gameMusic;
    public Music painMusic;
    public Music coinMusic1;
    public Music coinMusic2;
    public Music coinMusic3;
    public Music coinMusic4;
    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }
    

    
    private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu.png");
    	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
    	options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
    	       
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    }
    
    private void loadMenuAudio()
    {
        
    }
    
    private void loadGameGraphics()
    {    	    	
    	
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        game_texture_atlas= new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,  TextureOptions.BILINEAR);
        game_background_region=BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "wall.png");
        book_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "book.png");
        cactus_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "kaktus.png");
        pipe_big_region= BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "pipeBig.png");
        coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "coin.png");
        grass_region=BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "grass.png");
        player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_texture_atlas, activity, "muhittin.png", 7, 1);
        car_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "car.png");
        bus_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "bus.png");
        //complete_window_region=BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_texture_atlas, activity, "close_window.png");
        
        try 
    	{
    	    this.game_texture_atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.game_texture_atlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    }
    
    private void loadGameFonts()
    {
        
    }
    
    private void loadGameAudio()
    {
    	
        try {
        	
			gameMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager() ,this.activity ,"mfx/gamemusic.ogg");
			coinMusic1= MusicFactory.createMusicFromAsset(engine.getMusicManager() ,this.activity ,"mfx/coin.ogg");
			coinMusic2= MusicFactory.createMusicFromAsset(engine.getMusicManager() ,this.activity ,"mfx/coin.ogg");
			coinMusic3= MusicFactory.createMusicFromAsset(engine.getMusicManager() ,this.activity ,"mfx/coin.ogg");
			coinMusic4= MusicFactory.createMusicFromAsset(engine.getMusicManager() ,this.activity ,"mfx/coin.ogg");
			painMusic= MusicFactory.createMusicFromAsset(engine.getMusicManager() ,this.activity ,"mfx/pain.ogg");
			
			gameMusic.setLooping(true);
			gameMusic.setVolume(0.5f);
			
        } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void loadSplashScreen()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
    	splashTextureAtlas.load();
    }
    
    public void unloadSplashScreen()
    {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
    
    private void loadMenuFonts()
    {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "skyhawk.ttf", 30, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }
    
    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }
    
    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    }
    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}