package com.samsung.openglproject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
//import java.util.Random;

public class GLRenderer implements Renderer {

    private final Context context;

/*    private static final String U_COLOR = "u_Color";
    private int uColorLocation;*/
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final String A_COLOR = "a_Color";
    private int aColorLocation;


    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final FloatBuffer vertexData;

    private int program;

/*    private final long lastTime = 0;
    private final int FPS = 1;
    private final float period = 1000/FPS;
    private final Random randomColor = new Random();*/

    float rectVertices[] = {
            //Triangle 1
            0.0f,   0.9f,     0.0f,    1.0f,   0.0f,
           -0.9f,  -0.9f,     1.0f,    1.0f,   1.0f,
            0.9f,  -0.9f,     1.0f,    1.0f,   1.0f,

/*            //Triangle 2
            0.5f,    0.5f,   1.0f,   0.0f,   0.0f,
            0.5f,   -0.5f,   0.0f,   1.0f,   0.0f,
           -0.5f,   -0.5f,   0.0f,   0.0f,   1.0f,*/
    };

    float rectRandomVertices[] = new float[10];

    public GLRenderer(Context context) {
        this.context = context;

        for (int i = 0; i < rectRandomVertices.length; ++i)
        {
            Random r = new Random();
            r.setSeed((long)r.nextFloat());

            rectRandomVertices[i] = r.nextFloat();
        }

        vertexData = ByteBuffer
                .allocateDirect(rectVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(rectVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.3f, 0.3f, 1.0f);

        String vertexShaderSource = Main.readTextFile(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = Main.readTextFile(context, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        GLES20.glUseProgram(program);

        //uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        vertexData.position(0);

        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);

        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
/*        if (System.currentTimeMillis() - lastTime >= period) {
            lastTime = System.currentTimeMillis();
            GLES20.glClearColor(randomColor.nextFloat(), randomColor.nextFloat(), randomColor.nextFloat(), 1.0f);
        }*/

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

}
