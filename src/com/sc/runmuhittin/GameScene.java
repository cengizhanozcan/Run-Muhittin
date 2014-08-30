package com.sc.runmuhittin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.align.HorizontalAlign;

import android.R.integer;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sc.runmuhittin.SceneManager.SceneType;

public class GameScene extends BaseScene implements  IOnSceneTouchListener
{
	//private CompleteWindow completeWindow;
	//private Sprite spriteCloseWindow;


	private HUD gameHUD;
	private Text scoreText;
	private int score = 0;
	private PhysicsWorld physicsWorld, physicsWorldCoins ;
	private Sprite spriteGrass;
	private Sprite spriteBook;
	private Sprite spritePipe;
	private Sprite spriteCoin,spriteCoin1,spriteCoin2,spriteCoin3;
	private Sprite spriteBus;
	private Sprite spriteCar;
	private Sprite spriteCactus;
	
	private Body bodyGrass;
	private Body bodyBook;
	private Body bodyPipe;
	private Body bodyCoin,bodyCoin1,bodyCoin2,bodyCoin3;
	private Body bodyCar;
	private Body bodyBus;
	private Body bodyCactus;
	
	public int distance=0; 
	public int i =0;
	public Random rnd=new Random();
	private Text gameOverText;
	private boolean gameOverDisplayed = false;
	private Text textDistance;
	private Text textGold;

	 
	AutoParallaxBackground parallaxBackground;
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";    
	private Player player;
	
	private boolean firstTouch = false; //----------->>>>>>---------\\
	

	public ArrayList<Integer> arr= new ArrayList<Integer>();
	public ArrayList<Integer> coinArray=new ArrayList<Integer>();
	
	private void createPhysics()
	{
	    physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
	    physicsWorldCoins = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
	    physicsWorld.setContactListener(contactListener());
	    registerUpdateHandler(physicsWorld);	
	    registerUpdateHandler(physicsWorldCoins);
	}
	
	private void addToScore(int gld)
	{
	    score += gld;
	    scoreText.setText("Score: " + score);
	}
	
	private void createHUD()
	{
	    gameHUD = new HUD();
	    
	    // CREATE SCORE TEXT
	    scoreText = new Text(650, 440, resourcesManager.font, "Score: 1234567890", new TextOptions(HorizontalAlign.LEFT), vbom);
	    scoreText.setSkewCenter(650, 440);    
	    scoreText.setText("Score: 0");
	    gameHUD.attachChild(scoreText);
	    
	    camera.setHUD(gameHUD);
	}
	
	@Override
	public void createScene()
	{		
		
	    createPhysics();
	    createBackground();
	    createHUD();	    
	    createPlayer();
	    createBookObject(400);
	    createPipeObject(800);
	    createCactus(1200);
	    createCar(1600);
	    createBus(1900);
	    createCoins(400);
	    //completeWindow=new CompleteWindow(vbom, distance, score);createCompleteWindow();
	    createObject();
	    
	    createGameOverText();
	    setOnSceneTouchListener(this);
	    
	}

	@Override
	public void onBackKeyPressed()
	{
	    SceneManager.getInstance().loadMenuScene(engine);
	    resourcesManager.gameMusic.stop();
	    resourcesManager.coinMusic1.stop();
	    resourcesManager.coinMusic2.stop();
	    resourcesManager.coinMusic3.stop();
	    resourcesManager.coinMusic4.stop();
	    resourcesManager.painMusic.stop();
	}

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
        camera.setCenter(400, 240);
        
        camera.setChaseEntity(null);	// TODO code responsible for disposing scene
        								// removing all game scene objects.

        
    }
   
    private void createBackground()
    {
    	parallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
    	parallaxBackground.attachParallaxEntity(new ParallaxEntity(-1, new Sprite(400, 240, resourcesManager.game_background_region,vbom)));
    	parallaxBackground.attachParallaxEntity(new ParallaxEntity(-1, new Sprite(390, 0, resourcesManager.grass_region,vbom)));
    	
    	setBackground(parallaxBackground);
    	
    	spriteGrass=new Sprite(100, 0, resourcesManager.grass_region, vbom)
    	{

    	    @Override
    	    protected void preDraw(GLState pGLState, Camera pCamera) 
    	    {
    	       super.preDraw(pGLState, pCamera);
    	       pGLState.enableDither();
    	    }
    	    @Override
    	    protected void onUpdateTextureCoordinates() {
    	    	// TODO Auto-generated method stub
    	    	super.onUpdateTextureCoordinates();
    	    }
    	};
    	spriteGrass.setVisible(false);
    	
    	bodyGrass = PhysicsFactory.createBoxBody(physicsWorld, spriteGrass, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyGrass.setUserData("bodyGrass");
    	bodyGrass.setFixedRotation(true);
		
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, bodyGrass, true, false)
        { @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                parallaxBackground.setParallaxValue(camera.getXMin());
                if(firstTouch)
                {
                	distance++;
                }                
            }
        });
    	attachChild(spriteGrass);
    	resourcesManager.getInstance().gameMusic.play();
    	
    }
    public int k=0,l=0;
    
    public void createObject()
    {
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteBook, bodyBook,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                spriteBook.onUpdate(pSecondsElapsed);
                spritePipe.onUpdate(pSecondsElapsed);
                spriteCactus.onUpdate(pSecondsElapsed);
                spriteCar.onUpdate(pSecondsElapsed);
                spriteBus.onUpdate(pSecondsElapsed);
                
                if(spriteBook.getX()<player.getX()-400)
                {   
                	i++;
                	arr.add(2500+(i)*(335+rnd.nextInt(50)));
                	bodyBook.setTransform(arr.get(k)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 60/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	k++;
                }  
                if(spritePipe.getX()<player.getX()-400)
                {
                	i++;
                	arr.add(2500+(i)*(335+rnd.nextInt(50)));
                	bodyPipe.setTransform(arr.get(k)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 60/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	k++;
                }  
                if(spriteCactus.getX()<player.getX()-400)
                {
                	i++;
                	arr.add(2500+(i)*(335+rnd.nextInt(50)));
                	bodyCactus.setTransform(arr.get(k)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 60/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	k++;
                }  
                if(spriteCar.getX()<player.getX()-450)
                {
                	i++;
                	arr.add(2500+(i)*(335+rnd.nextInt(50)));
                	bodyCar.setTransform(arr.get(k)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 60/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	k++;
                }  
                if(spriteBus.getX()<player.getX()-450)
                {
                	i++;
                	arr.add(2500+(i)*(335+rnd.nextInt(50)));
                	bodyBus.setTransform(arr.get(k)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 60/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	k++;
                }
               
                //-----------------------------------------------------------------------
                
                if(spriteCoin.getX() < player.getX()-400)
                { 	
                	
                	arr.add(2500+(i)*(335+rnd.nextInt(50)));
                	spriteCoin.setVisible(true);
                	bodyCoin.setTransform(arr.get(l)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 330/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	if(player.collidesWith(spriteCoin))
                	{
                		addToScore(10);
                	}
                	
                }
                if(spriteCoin1.getX() < player.getX()-400)
                {
                	spriteCoin1.setVisible(true);
                	bodyCoin1.setTransform((arr.get(l)+20)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 345/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	if(player.collidesWith(spriteCoin1))
                	{
                		addToScore(10);
                	}
                }
                if(spriteCoin2.getX() < player.getX()-400)
                {
                	spriteCoin2.setVisible(true);
                	bodyCoin2.setTransform((arr.get(l)+40)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 345/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	if(player.collidesWith(spriteCoin2))
                	{
                		addToScore(10);
                	}
                }
                if(spriteCoin3.getX() < player.getX()-400)
                {
                	spriteCoin3.setVisible(true);
                	bodyCoin3.setTransform((arr.get(l)+60)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 330/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
                	if(player.collidesWith(spriteCoin3))
                	{
                		addToScore(10);
                	}
                	
                	l++;
                }
            }
    	
        });
    }
    
    public void createBookObject(int i)
    {
    	spriteBook=new Sprite(i, 60, ResourcesManager.getInstance().book_region, vbom);
    	bodyBook = PhysicsFactory.createBoxBody(physicsWorld, spriteBook, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        bodyBook.setUserData("bodyBook");
        	
    	bodyBook.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteBook, bodyBook,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteBook.onUpdate(pSecondsElapsed);
                if(player.collidesWith(spriteBook))
                {
                	player.onDie();
                }
               
            }
        });
    	attachChild(spriteBook);
    	
    }
    
    public void createCar(int i)
    {
    	spriteCar=new Sprite(i, 60, ResourcesManager.getInstance().car_region, vbom);
    	bodyCar = PhysicsFactory.createBoxBody(physicsWorld, spriteCar, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyCar.setUserData("bodyCar");
        	
    	bodyCar.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteCar, bodyCar,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteCar.onUpdate(pSecondsElapsed);
                if(player.collidesWith(spriteCar))
                {
                	player.onDie();
                }
            }
        });
    	attachChild(spriteCar);
    }
    
    public void createBus(int i)
    {
    	spriteBus=new Sprite(i, 60, ResourcesManager.getInstance().bus_region, vbom);
    	bodyBus = PhysicsFactory.createBoxBody(physicsWorld, spriteBus, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyBus.setUserData("bodyBus");
        	
    	bodyBus.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteBus, bodyBus,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteBus.onUpdate(pSecondsElapsed);
                if(player.collidesWith(spriteBus))
                {
                	player.onDie();
                }
            }
        });
    	attachChild(spriteBus);
    }
    
    public void createCactus(int i)
    {
    	spriteCactus=new Sprite(i, 60, ResourcesManager.getInstance().cactus_region, vbom);
    	bodyCactus = PhysicsFactory.createBoxBody(physicsWorld, spriteCactus, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyCactus.setUserData("bodyCactus");
        	
    	bodyCactus.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteCactus, bodyCactus,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteCactus.onUpdate(pSecondsElapsed);
                if(player.collidesWith(spriteCactus))
                {
                	player.onDie();
                }
            }
        });
    	attachChild(spriteCactus);
    }
    
    public void createPipeObject(final int coord)
    {
    	spritePipe=new Sprite(600, 60, ResourcesManager.getInstance().pipe_big_region, vbom);
    	bodyPipe = PhysicsFactory.createBoxBody(physicsWorld, spritePipe, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        bodyPipe.setUserData("bodyPipe");
        	
    	bodyPipe.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spritePipe, bodyPipe,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {    	
                super.onUpdate(pSecondsElapsed);
                spritePipe.onUpdate(pSecondsElapsed);
                if(player.collidesWith(spritePipe))
                {
                	player.onDie();
                }
            }
        });
    	attachChild(spritePipe);
    }
    
    public void createCoins(int x)
    {
    	spriteCoin=new Sprite(x, 250, ResourcesManager.getInstance().coin_region, vbom);
    	bodyCoin = PhysicsFactory.createBoxBody(physicsWorldCoins, spriteCoin, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyCoin.setUserData("bodyCoin");
        	
    	bodyCoin.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteCoin, bodyCoin,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteCoin.onUpdate(pSecondsElapsed);
                
                if (player.collidesWith(spriteCoin))
                {
                    addToScore(10);
                    spriteCoin.setVisible(false);
                    spriteCoin.setIgnoreUpdate(true);
                    resourcesManager.getInstance().coinMusic1.play();

                }
                
            }
        });
    	
    	spriteCoin1=new Sprite(x+30, 250, ResourcesManager.getInstance().coin_region, vbom);
    	bodyCoin1 = PhysicsFactory.createBoxBody(physicsWorldCoins, spriteCoin1, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyCoin1.setUserData("bodyCoin1");
        	
    	bodyCoin1.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteCoin1, bodyCoin1,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteCoin1.onUpdate(pSecondsElapsed);
                
                if (player.collidesWith(spriteCoin1))
                {
                    addToScore(10);
                    spriteCoin1.setVisible(false);
                    spriteCoin1.setIgnoreUpdate(true);
                    resourcesManager.getInstance().coinMusic2.play();
                }
                
            }
        });
    	
    	spriteCoin2=new Sprite(x+60, 250, ResourcesManager.getInstance().coin_region, vbom);
    	bodyCoin2 = PhysicsFactory.createBoxBody(physicsWorldCoins, spriteCoin2, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyCoin2.setUserData("bodyCoin2");
        	
    	bodyCoin2.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteCoin2, bodyCoin2,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteCoin2.onUpdate(pSecondsElapsed);
                
                if (player.collidesWith(spriteCoin2))
                {
                    addToScore(10);
                    spriteCoin2.setVisible(false);
                    spriteCoin2.setIgnoreUpdate(true);
                    resourcesManager.getInstance().coinMusic3.play();
                }
                
            }
        });
    	
    	spriteCoin3=new Sprite(x+90, 250, ResourcesManager.getInstance().coin_region, vbom);
    	bodyCoin3 = PhysicsFactory.createBoxBody(physicsWorldCoins, spriteCoin3, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
    	bodyCoin3.setUserData("bodyCoin3");
        	
    	bodyCoin3.setFixedRotation(true);
    	physicsWorld.registerPhysicsConnector(new PhysicsConnector(spriteCoin3, bodyCoin3,true,false)
    	{ @Override
            public void onUpdate(float pSecondsElapsed)
            {
    	
                super.onUpdate(pSecondsElapsed);
                spriteCoin3.onUpdate(pSecondsElapsed);
                
                if (player.collidesWith(spriteCoin3))
                {
                    addToScore(10);
                    spriteCoin3.setVisible(false);
                    spriteCoin3.setIgnoreUpdate(true);
                    resourcesManager.getInstance().coinMusic4.play();
                }
                
            }
        });
    	attachChild(spriteCoin);
    	attachChild(spriteCoin1);
    	attachChild(spriteCoin2);
    	attachChild(spriteCoin3);
   
    }
    
    /*public void createCompleteWindow()
    {
    	spriteCloseWindow = new Sprite(0, 0, resourcesManager.complete_window_region, vbom)
        {
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) 
            {
                super.onManagedUpdate(pSecondsElapsed);
                    this.setVisible(false);
                    this.setIgnoreUpdate(true);
               
            }
        };
    }*/
    
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		if (pSceneTouchEvent.isActionDown())
	    {
	        if (!firstTouch)
	        {
	            player.setRunning();
	            firstTouch = true;
	            
	            bodyGrass.setLinearVelocity(new Vector2(5,0));
		        bodyBook.setLinearVelocity(new Vector2(-5,0));
		        bodyPipe.setLinearVelocity(new Vector2(-5,0));
		        bodyCactus.setLinearVelocity(new Vector2(-5,0));
		        bodyCar.setLinearVelocity(new Vector2(-5,0));
		        bodyBus.setLinearVelocity(new Vector2(-5,0));
		        bodyCoin.setLinearVelocity(new Vector2(-5,0));
		        bodyCoin1.setLinearVelocity(new Vector2(-5,0));
		        bodyCoin2.setLinearVelocity(new Vector2(-5,0));
		        bodyCoin3.setLinearVelocity(new Vector2(-5,0));
		       
	        }
	        else
	        {
	        	player.increaseFootContacts();
	            player.jump();
	           
	        }
	        
	    }
	    return false;
	}
	
	public void createPlayer()
	{
		player = new Player(0, 15, vbom, camera, physicsWorld)
	    {
	        @Override
	        public void onDie()
	        {
	        	 if (!gameOverDisplayed)
	        	    {
	        	        displayGameOverText();
	        	        player.stopAnimation();
		            	player.setStop();
		            	resourcesManager.getInstance().gameMusic.stop();
	                    resourcesManager.getInstance().painMusic.play();
	                    stop();
	        	    }
	        }
	   
	    };
	    attachChild(player);
	}
	
	public void stop()
	{
			bodyGrass.setLinearVelocity(new Vector2(0,0));
		    bodyBook.setLinearVelocity(new Vector2(0,0));
	        bodyPipe.setLinearVelocity(new Vector2(0,0));
	        bodyCactus.setLinearVelocity(new Vector2(0,0));
	        bodyCar.setLinearVelocity(new Vector2(0,0));
	        bodyBus.setLinearVelocity(new Vector2(0,0));
	        bodyCoin.setLinearVelocity(new Vector2(0,0));
	        bodyCoin1.setLinearVelocity(new Vector2(0,0));
	        bodyCoin2.setLinearVelocity(new Vector2(0,0));
	        bodyCoin3.setLinearVelocity(new Vector2(0,0));
	}
	
	private ContactListener contactListener()
	{
	    ContactListener contactListener = new ContactListener()
	    {
	        public void beginContact(Contact contact)
	        {
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	            
	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("player") )
	                {           
	                    player.footContacts=1;	                    
	                }
	             
	            }
	        }

	        public void endContact(Contact contact)
	        {
	        }

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
				final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	            
	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x1.getBody().getUserData().equals("bodyGrass")||x1.getBody().getUserData().equals("bodyPipe"))
	                {           
	                    player.footContacts=0;	                    
	                }         
	            }

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
	    };
	    return contactListener;
	}

	
	
	private void createGameOverText()
	{
		
 
	}

	private void displayGameOverText()
	{
	    gameOverDisplayed = true;
	    camera.setChaseEntity(null);
	       	
	    gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
	    textDistance=new Text(0,0, ResourcesManager.getInstance().font, "Distance = "+distance +" m", vbom);
    	textGold=new Text(0,0, ResourcesManager.getInstance().font, "Score = " +score, vbom);
    	
	    gameOverText.setPosition(player.getX()-200, camera.getCenterY()+100);
	    textDistance.setPosition(player.getX()-200, camera.getCenterY());
	    textGold.setPosition(player.getX()-200, camera.getCenterY()-50);
	    
	    
	    attachChild(gameOverText);
	    attachChild(textDistance);
        attachChild(textGold);
	}
	
    
}