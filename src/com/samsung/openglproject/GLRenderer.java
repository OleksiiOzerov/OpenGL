package com.samsung.openglproject;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class GLRenderer implements Renderer {

    long lastTime = 0;
    final int FPS = 1;
    final float period = 1000/FPS;
    Random randomColor = new Random();
    
    @Override
    public void onDrawFrame(GL10 gl) {
        if (System.currentTimeMillis() - lastTime >= period) {
            lastTime = System.currentTimeMillis();
            GLES20.glClearColor(randomColor.nextFloat(), randomColor.nextFloat(), randomColor.nextFloat(), 1.0f);
        }
        
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
    }
    
}
