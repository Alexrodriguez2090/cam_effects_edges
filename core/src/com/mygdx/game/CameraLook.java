package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.concurrent.TimeUnit;

class CameraLook extends CameraEffect {
    private float baseIntensity;
    private float intensity;
    private int speed;
    private int direction;
    private float baseX;
    private float baseY;

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        if (intensity < 1) {
            this.intensity = 1;
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
                this.speed = Math.abs(speed - 99);
            }
        }
    }

    @Override
    public boolean isActive() {
        return super.isActive() && speed > 0;
    }

    public CameraLook(OrthographicCamera cam, int duration, SpriteBatch batch, ShapeRenderer renderer, float intensity,
            int speed) {
        super(cam, duration, batch, renderer);
        setIntensity(intensity);
        setSpeed(speed);
    }

    @Override
    public void play() {
        if (isActive()) {
            if (progress % speed == 0) {
                if (direction == 0) {
                    cam.translate(0, intensity);
                } else if (direction == 1) {
                    cam.translate(0, -intensity);
                } else if (direction == 2) {
                    cam.translate(-intensity, 0);
                } else {
                    cam.translate(intensity, 0);
                }
            }
            progress++;

            if (!isActive()) {
                //Wait looking in that direction
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cam.position.x = baseX;
                cam.position.y = baseY;
            }
            updateCamera();
        }
	}

    public void start(int direction) {
        this.direction = direction;
        System.out.println(intensity);
        intensity = baseIntensity;
        baseX = cam.position.x;
        baseY = cam.position.y;
		super.start();
    }
}