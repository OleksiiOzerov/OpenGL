package com.example.openglbook;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);

        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader.");
            }
            return 0;
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int program = GLES20.glCreateProgram();

        if (program == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new programe.");
            }
            return 0;
        }

        GLES20.glAttachShader(program, vertexShaderId);
        GLES20.glAttachShader(program, fragmentShaderId);
        GLES20.glLinkProgram(program);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(program);

            if (LoggerConfig.ON) {
                Log.w(TAG, "Link programe failed.");
            }
            return 0;
        }

        return program;
    }
}
