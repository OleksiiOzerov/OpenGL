package com.samsung.openglproject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

public class Main extends ActionBarActivity {

    GLSurfaceView surfaceView;

    GLRenderer renderer = new GLRenderer(this);

/*    static int number = 0;

    TextView textView;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surfaceView = new GLSurfaceView(this);

        surfaceView.setEGLContextClientVersion(2);

        surfaceView.setRenderer(renderer);

        setContentView(surfaceView);

/*        Button increaseButton = new Button(this);
        textView = new TextView(this);
        LinearLayout linLayout = new LinearLayout(this);

        increaseButton.setText("Increase Button");
        textView.setText("Somsing to increase");

        linLayout.addView(increaseButton);
        linLayout.addView(textView);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        addContentView(linLayout, layoutParams);

        increaseButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                number++;
                textView.setText(String.valueOf(number));
            }

        });*/


        Log.i("Vilix", readTextFile(this, R.raw.simple_vertex_shader));
        Log.i("Vilix", readTextFile(this, R.raw.simple_fragment_shader));

    }

    public static String readTextFile(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while ((nextLine = bufferReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }

        } catch(Exception e) {

        }

        return body.toString();
    }
}
