package com.example.openglbook;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class GLRenderer implements Renderer {

    private final float modelMatrix[] = new float[16];

    private static final String U_MATRIX = "u_Matrix";
    private final float projectionMatrix[] = new float[16];
    private int uMatrixLocation;

    private static final String A_COLOR = "a_Color";
    private int aColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;

    private static final int BYTES_PER_FLOAT = 4;

    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final FloatBuffer vertexData;

    private final Context context;

    private int programObjectId;

    public GLRenderer(Context context) {
        this.context = context;

/*        float tableVerticicesWithTriangles[] = {
                0.0f,   0.0f,   1.0f,   1.0f,   1.0f,
               -1.0f,  -1.0f,   0.7f,   0.7f,   0.7f,
                1.0f,  -1.0f,   0.7f,   0.7f,   0.7f,
                1.0f,   1.0f,   0.7f,   0.7f,   0.7f,
               -1.0f,   1.0f,   0.7f,   0.7f,   0.7f,
               -1.0f,  -1.0f,   0.7f,   0.7f,   0.7f,

               -1.0f,   0.0f,   1.0f,   0.0f,   0.0f,
                1.0f,   0.0f,   1.0f,   0.0f,   0.0f,

                0.0f,   0.5f,   0.0f,   0.0f,   1.0f,
                0.0f,  -0.5f,   1.0f,   0.0f,   0.0f,
        };*/

        float tableVerticicesWithTriangles[] = {
                0.0f,   0.0f,   0f,   1.5f,   1.0f,   1.0f,   1.0f,
               -0.5f,  -0.8f,   0f,   1f,     0.7f,   0.7f,   0.7f,
                0.5f,  -0.8f,   0f,   1f,     0.7f,   0.7f,   0.7f,
                0.5f,   0.8f,   0f,   2f,     0.7f,   0.7f,   0.7f,
               -0.5f,   0.8f,   0f,   2f,     0.7f,   0.7f,   0.7f,
               -0.5f,  -0.8f,   0f,   1f,     0.7f,   0.7f,   0.7f,

               -0.5f,   0.0f,   0f,   1.5f,   1.0f,   0.0f,   0.0f,
                0.5f,   0.0f,   0f,   1.5f,   1.0f,   0.0f,   0.0f,

                0.0f,  -0.4f,   0f,   1.25f,   0.0f,   0.0f,   1.0f,
                0.0f,   0.4f,   0f,   1.75f,   1.0f,   0.0f,   0.0f,
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticicesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticicesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        GLES20.glClearColor(0.3f, 0.0f, 0.4f, 0.0f);

        String vertexShaderSource = readTextFile(context, R.raw.vertex_shader);
        String fragmentShaderSource = readTextFile(context, R.raw.fragment_shader);

//        Log.w("ShaderHelper", fragmentShaderSource);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        programObjectId = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        GLES20.glUseProgram(programObjectId);

        aPositionLocation = GLES20.glGetAttribLocation(programObjectId, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(programObjectId, A_COLOR);
        uMatrixLocation = GLES20.glGetUniformLocation(programObjectId, U_MATRIX);

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {

        GLES20.glViewport(0, 0, width, height);

/*        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;

        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1.0f, 1.0f, -1.0f, 1.0f);
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1.0f, 1.0f, -aspectRatio, aspectRatio, -1.0f, 1.0f);
        }
*/
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);


        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0.0f, 0.0f);

        final float temp[] = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
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
