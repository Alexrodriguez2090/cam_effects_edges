package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

class CameraShake extends CameraEffect {
	private float baseIntensity;
    private float intensity;
	private int speed;
	private String direction;

	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		if (!direction.equalsIgnoreCase("h") && !direction.equalsIgnoreCase("v")) {
			this.direction = "h";
		} else {
			this.direction = direction;
		}
	}

    public float getIntensity() {
        return intensity;
    }
    public void setIntensity(float intensity) {
        if (intensity < 0) {
            this.intensity = 0;
        } else {
            this.intensity = intensity;
		}
		this.baseIntensity = intensity;
	}
	
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        if (speed < 0) {
            speed = 0;
        } else {
            if (speed > duration) {
                speed = duration / 2;
            } else {
                this.speed = Math.abs(speed-99);
            }
        }
	}
	
    @Override
    public boolean isActive() {
        return super.isActive() && speed > 0;
	}
	
    public CameraShake(OrthographicCamera cam, int duration, SpriteBatch batch,
    ShapeRenderer renderer, float intensity, int speed, String direction) {
        super(cam,duration,batch,renderer);
        setIntensity(intensity);
		setSpeed(speed);
		setDirection(direction);
	}
	
    @Override
    public void play() {
        if (isActive()) {
            if (progress % speed == 0) {
                intensity = (float) (-intensity / 1.1);
                
                if (this.direction.equalsIgnoreCase("h")) {
                    cam.translate(intensity,0);
                } else {
                    cam.translate(0, intensity);
                }
            }
            progress++;

            if (!isActive()) {
                if (this.direction.equalsIgnoreCase("h")) {
                cam.translate(-intensity,0);
                } else {
                    cam.translate(0, -intensity);
                }
            }
            updateCamera();
        }
	}
	
	
    @Override
    public void start() {
		intensity = baseIntensity;
		super.start();
		if (this.direction.equalsIgnoreCase("h")) {
			cam.translate(intensity, 0);
		} else {
			cam.translate(0, intensity);
		}
        updateCamera();
    }
}