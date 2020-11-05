package com.mygdx.game;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

class InputHandler extends InputAdapter {
	private boolean shiftHeld = false;

	private SpriteBatch batch;
	private OrthographicCamera cam;
	private Vector3 startCam, startMouse;
	int imgX, imgY;

	public InputHandler(SpriteBatch batch, OrthographicCamera cam, int imgX, int imgY) {
		this.batch = batch;
		this.cam = cam;
		this.imgX = imgX;
		this.imgY = imgY;
	}

	public void setImgX(int x) {
		this.imgX = x;
	}
	public int getImgX() {
		return this.imgX;
	}

	public void setImgY(int y) {
		this.imgY = y;
	}
	public int getImgY() {
		return this.imgY;
	}

	@Override
	public boolean keyDown(int keyCode) {

		if (keyCode == Keys.W) {
			imgY += 20;
		} else if (keyCode == Keys.S) {
			imgY -= 20;
		} else if (keyCode == Keys.A) {
			imgX -= 20;
		} else if (keyCode == Keys.D) {
			imgX += 20;
		}

		if (keyCode == Keys.SHIFT_LEFT || keyCode == Keys.SHIFT_RIGHT) {
			shiftHeld = true;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keyCode) {
		if (keyCode == Keys.SHIFT_LEFT || keyCode == Keys.SHIFT_RIGHT) {
			shiftHeld = false;
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		startCam = new Vector3(cam.position.x, cam.position.y, 0);
		startMouse = new Vector3(screenX, screenY, 0);

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (shiftHeld) {
			double theta = Math.atan2(screenY - startMouse.y, startMouse.x - screenX);
			cam.rotate((float) theta);
            updateCam();
            
		} else {
			if (screenX < 641 && screenX >-1 && screenY < 480 && screenY > -1) {
				System.out.println(screenX);
				float diffX = screenX - startMouse.x;
				float diffY = screenY - startMouse.y;
				cam.position.x = startCam.x + diffX;
				cam.position.y = startCam.y - diffY;
				updateCam();
			}
		}

		return true;
	}

	public void updateCam() {
		cam.update();
		batch.setProjectionMatrix(cam.combined);
	}
}