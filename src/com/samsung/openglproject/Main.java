package com.samsung.openglproject;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Main extends ActionBarActivity {

    GLSurfaceView surfaceView;

    GLRenderer renderer = new GLRenderer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surfaceView = new GLSurfaceView(this);

        surfaceView.setRenderer(renderer);
        
        setContentView(surfaceView);
    }
}
