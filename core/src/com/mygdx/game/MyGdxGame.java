package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture map;

	OrthographicCamera cam;
	int WIDTH, HEIGHT;
	float WORLDWIDTH, WORLDHEIGHT;

	int imgWidth, imgHeight;
	int imgX, imgY;

	InputHandler handler;
	CameraLook lookAround;

	boolean isJailed = false;
	int jailLeft;
	int jailRight;
	int jailBottom;
	int jailTop;

	LabelStyle labelStyle;
	Label label;

	public void setUpLabelStyle() {
		labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont(Gdx.files.internal("fonts/myfont.fnt"));
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogicchar.jpg");
		map = new Texture("Bigmap.jpg");
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();

		//Starting off the character not at the very borders
		imgX = 40;
		imgY = 40;

		//Setting up label
		setUpLabelStyle();
		label = new Label("Position will go here.", labelStyle);

		//Getting the world dimensions
		WORLDWIDTH = map.getWidth();
		WORLDHEIGHT = map.getHeight();

		//Setting up input handler
		handler = new InputHandler(batch, cam, imgX, imgY);
		Gdx.input.setInputProcessor(handler);

		//Set up camera
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(WIDTH,HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		//Lastly sets up camera effect
		lookAround = new CameraLook(cam, 200, batch, null, 10, 95);
	}

	public Vector2 getViewPortOrigin() {
		return new Vector2(cam.position.x-WIDTH/2, cam.position.y - HEIGHT/2);
	}
	public Vector2 getScreenCoordinates() {
		Vector2 viewportOrigin = getViewPortOrigin();
		return new Vector2(handler.getImgX()-viewportOrigin.x, handler.getImgY()-viewportOrigin.y);
	}

	public void panCoordinates(float border) {
		Vector2 screenPos = getScreenCoordinates();

		//Right side
		if (screenPos.x > WIDTH - imgWidth - border) { //about to go off vieport
			if (handler.getImgX() + imgWidth > WORLDWIDTH - border) { //about to go off the world
				wrapCoordinates(WORLDWIDTH, WORLDHEIGHT);
				cam.position.x = 320;
				cam.update();
                batch.setProjectionMatrix(cam.combined);
            } else { //pan the camera
				handler.setImgX(handler.getImgX() + 100);
				cam.position.x = cam.position.x + 640;
                System.out.println(cam.position.x);
                cam.update();
                batch.setProjectionMatrix(cam.combined);
            }
		} 
		
		//Left side
		if (screenPos.x < border) { // about to go off vieport
			if (handler.getImgX() <= 0) { // about to go off the world
				wrapCoordinates(WORLDWIDTH, WORLDHEIGHT);
				cam.position.x = WORLDWIDTH - 320;
				cam.update();
                batch.setProjectionMatrix(cam.combined);
			} else { //pan the camera
				
				cam.position.x = cam.position.x - 640;
				handler.setImgX(handler.getImgX() - 80);
				System.out.println(cam.position.x);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
			}
		}
		
		//Top side
        if (screenPos.y > HEIGHT - imgHeight - border) { //go off viewport vertically
            if (handler.getImgY() + imgHeight > WORLDHEIGHT - border) { //out of space in y direction
                lockCoordinates(WORLDWIDTH, WORLDHEIGHT);
            } else { //pan the camera
				handler.setImgY(handler.getImgY() + 80);
				cam.position.y = cam.position.y + 480;
                System.out.println(cam.position.y);
                cam.update();
                batch.setProjectionMatrix(cam.combined);
			}
			
		//Bottom side
		} if (screenPos.y < border) { //go off viewport vertically
			if (handler.getImgY() <= 0) {  //out of space in y direction
				lockCoordinates(WORLDWIDTH, WORLDHEIGHT);
			} else { //pan the camera
				handler.setImgY(handler.getImgY() - 80);
				cam.position.y = cam.position.y - 480;
				System.out.println(cam.position.y);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
			}
		}
	}
	
	public void lockCoordinates(float targetWidth, float targetHeight) {
		//Unused locking in x direction
        /*if (handler.getImgX() > targetWidth - imgWidth) {
            handler.setImgX((int) targetWidth - imgWidth);
        } else if (handler.getImgX() < 0) {
            handler.imgX = 0;
        }*/
        if (handler.getImgY() > targetHeight - imgHeight) {
            handler.setImgY((int) targetHeight - imgHeight);
        } else if (handler.getImgY() < 0) {
            handler.setImgY(0);
			}   
		}

	public void wrapCoordinates(float targetWidth, float targetHeight) {
        if (handler.getImgX() >= targetWidth - imgWidth) {
            handler.setImgX(40);
        } else if (handler.getImgX() <= 0) {
            handler.setImgX((int) targetWidth - (imgWidth*2));
		}
		
		//Unused wrapping in y direction
        /*if (handler.getImgY() > targetHeight) {
            handler.setImgY(-imgHeight);
        } else if (handler.getImgY() < -imgHeight) {
            handler.setImgY((int) targetHeight);
        }*/
	}

	//Sets the jail cells around the character
	public void setJail() {
		jailLeft = handler.getImgX() - 20;
		jailRight = handler.getImgX() + imgWidth + 20;
		jailBottom = handler.getImgY() - 20;
		jailTop = handler.getImgY() + imgHeight + 20;
	}
	//Locked in jail function
	public void JLock() {
        if (handler.getImgX() + imgWidth > jailRight) {
            handler.setImgX(handler.getImgX() - 20);
        } else if (handler.getImgX() < jailLeft) {
            handler.setImgX(handler.getImgX() + 20);
		}
		
        if (handler.getImgY() + imgHeight> jailTop) {
            handler.setImgY(handler.getImgY() - 20);
        } else if (handler.getImgY() < jailBottom) {
            handler.setImgY(handler.getImgY() + 20);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (lookAround.isActive()) {
			lookAround.play();
		} else {
			//Seeing if to pan
			panCoordinates(10);

			//Check if we're looking around
			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				lookAround.start(0);
			} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
				lookAround.start(1);
			} else if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
				lookAround.start(2);
			} else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
				lookAround.start(3);
			}
		}

		//Setting up position text
		label.setPosition(450+(cam.position.x-WIDTH/2),40+cam.position.y-HEIGHT/2);
		label.setText(handler.getImgX() + ", " + handler.getImgY());

		//Jail options
		if (Gdx.input.isKeyJustPressed(Keys.J)) {
			setJail();
			isJailed = true;
		}
		if (Gdx.input.isKeyJustPressed(Keys.U)) {
			isJailed = false;
		}
		if (isJailed) {
			JLock();
		}

		batch.begin();
		batch.draw(map, 0, 0);
		batch.draw(img, handler.getImgX(), handler.getImgY(), imgWidth, imgHeight);
		label.draw(batch, 1);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		map.dispose();
	}
}
