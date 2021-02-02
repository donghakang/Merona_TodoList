package com.example.whenyoucomemerona.view;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.lib.RawResourceReader;
import com.example.whenyoucomemerona.lib.ShaderHelper;
import com.example.whenyoucomemerona.lib.TextureHelper;


/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
public class mGLSurfaceRenderer implements GLSurfaceView.Renderer
{
    Context ctx;

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];

    ArrayList<Float> meronaPositionData;
    ArrayList<Float> meronaColorData;
    ArrayList<Float> meronaNormalData;
    ArrayList<Float> meronaTextureCoordinateData;

    private final FloatBuffer mMeronaPositions;
    private final FloatBuffer mMeronaColors;
    private final FloatBuffer mMeronaNormals;
    private final FloatBuffer mMeronaTextureCoordinates;

    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mLightPosHandle;
    private int mTextureUniformHandle;
    private int mPositionHandle;

    private int mColorHandle;
    private int mNormalHandle;
    private int mTextureCoordinateHandle;


    private final int mBytesPerFloat = 4;
    private final int mPositionDataSize = 3;
    private final int mColorDataSize = 4;
    private final int mNormalDataSize = 3;
    private final int mTextureCoordinateDataSize = 2;

    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];

    private int mProgramHandle;
    private int mPointProgramHandle;
    private int mTextureDataHandle;

    public mGLSurfaceRenderer(final Context ctx)
    {
        this.ctx = ctx;

        meronaPositionData = new ArrayList<>();
        meronaColorData = new ArrayList<>();
        meronaNormalData = new ArrayList<>();
        meronaTextureCoordinateData = new ArrayList<>();

        readfile();

        mMeronaPositions = ByteBuffer.allocateDirect(meronaPositionData.size() * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (float pos : meronaPositionData) mMeronaPositions.put(pos);
        mMeronaPositions.position(0);

        mMeronaColors = ByteBuffer.allocateDirect(meronaColorData.size() * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (float color : meronaColorData) mMeronaColors.put(color);
        mMeronaColors.position(0);

        mMeronaNormals = ByteBuffer.allocateDirect(meronaNormalData.size() * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (float normal : meronaNormalData) mMeronaNormals.put(normal);
        mMeronaNormals.position(0);

        mMeronaTextureCoordinates = ByteBuffer.allocateDirect(meronaTextureCoordinateData.size() * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (float tex : meronaTextureCoordinateData) mMeronaTextureCoordinates.put(tex);
        mMeronaTextureCoordinates.position(0);
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        GLES20.glClearColor(1.0f, 254.0f/255.0f, 240.0f / 255.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);




        final String vertexShader = RawResourceReader.readTextFileFromRawResource(ctx, R.raw.pixel_vertex_shader);
        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(ctx, R.raw.pixel_fragment_shader);
        final String pointVertexShader = RawResourceReader.readTextFileFromRawResource(ctx, R.raw.point_vertex_shader);
        final String pointFragmentShader = RawResourceReader.readTextFileFromRawResource(ctx, R.raw.point_fragment_shader);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        final int pointVertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
        final int pointFragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);

        mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});
        mPointProgramHandle = ShaderHelper.createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                new String[] {"a_Position"});

        // TEXTURE
        mTextureDataHandle = TextureHelper.loadTexture(ctx, R.mipmap.wood);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // 10초에 한번 로테이션이 바뀐다.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // 라이팅 프로그램
        GLES20.glUseProgram(mProgramHandle);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_LightPos");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");

        // 텍스쳐
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        // Draw

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 4.0f, 0.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 0.0f, 0.0f);
        draw();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -4.0f, 0.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.3f, 1.0f, 0.0f);
        draw();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 4.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        draw();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -4.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, .3f, .5f, .7f);
        draw();

        // --> Center
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.2f, 1.0f, 0.4f);
        draw();

        GLES20.glUseProgram(mPointProgramHandle);
        drawLight();
    }


    private void draw()
    {
        // Pass in the position information
        mMeronaPositions.position(0);
        mMeronaColors.position(0);
        mMeronaNormals.position(0);
        mMeronaTextureCoordinates.position(0);


        GLES20.glVertexAttribPointer(mPositionHandle,
                mPositionDataSize,
                GLES20.GL_FLOAT,
                false,
                0,
                mMeronaPositions);


        GLES20.glVertexAttribPointer(mColorHandle,
                mColorDataSize,
                GLES20.GL_FLOAT,
                false,
                0,
                mMeronaColors);


        GLES20.glVertexAttribPointer(mNormalHandle,
                mNormalDataSize,
                GLES20.GL_FLOAT,
                false,
                0,
                mMeronaNormals);

        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,
                mTextureCoordinateDataSize,
                GLES20.GL_FLOAT,
                false,
                0,
                mMeronaTextureCoordinates);




        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);


        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, meronaPositionData.size()/3);
    }


    private void drawLight()
    {
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }


    // read file
    private void readfile() {

        List<Float[]> objPositions = new ArrayList<>();
        List<Float[]> objTextures = new ArrayList<>();
        List<Float[]> objNormals = new ArrayList<>();
        List<String> objIndices = new ArrayList<>();


        String ll = "3//3";
        String[] partss = ll.split("/");
        Log.d("dddd", partss[0] + "   " +  partss[1]);


        // 파일을 읽습니다.
        Scanner scanner = null;
        try {
            scanner = new Scanner(ctx.getAssets().open("merona.obj"));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");

                switch (parts[0]) {
                    case "v":
                        // vertices
                        Float[] v = new Float[3];
                        v[0] = Float.valueOf(parts[1]);
                        v[1] = Float.valueOf(parts[2]);
                        v[2] = Float.valueOf(parts[3]);
                        objPositions.add(v);      // v 1.250000 0.000000 0.000000
                        break;
                    case "vt":
                        // textures
                        Float[] t = new Float[2];
                        t[0] = Float.valueOf(parts[1]);
                        t[1] = Float.valueOf(parts[2]);
                        objTextures.add(t);
                        break;
                    case "vn":
                        Float[] n = new Float[3];
                        n[0] = Float.valueOf(parts[1]);
                        n[1] = Float.valueOf(parts[2]);
                        n[2] = Float.valueOf(parts[3]);
                        objNormals.add(n);      // v 1.250000 0.000000 0.000000
                        break;
                    case "f":
                        // faces: vertex/texture/normal
                        if (parts.length < 5) {
                            objIndices.add(parts[1]);
                            objIndices.add(parts[2]);
                            objIndices.add(parts[3]);
                        } else {
                            objIndices.add(parts[1]);                  // f 80/87/80 92/100/80 93/101/80
                            objIndices.add(parts[2]);                  //   80/87/80 93/101/80 81/88/80
                            objIndices.add(parts[3]);

                            objIndices.add(parts[1]);
                            objIndices.add(parts[3]);
                            objIndices.add(parts[4]);
                        }
                        break;
                    case "usemtl":
                        if (parts[1].equals("merona_stick")) {
                            objIndices.add("merona_stick");
                        } else if (parts[1].equals("merona_bar")) {
                            objIndices.add("merona_bar");
                        } else if (parts[1].equals("merona_bottom")) {
                            objIndices.add("merona_bottom");
                        }
                        break;
                }


            }
            scanner.close();
        } catch (IOException e) {
            // IOException
        }


        float MERONA_R = 201.0f / 255.0f;
        float MERONA_G = 1.0f;
        float MERONA_B = 163.0f / 255.0f;

        float STICK_R = 245.0f / 255.0f;
        float STICK_G = 225.0f / 255.0f;
        float STICK_B = 190.0f / 255.0f;

        float r = MERONA_R;
        float g = MERONA_G;
        float b = MERONA_B;
        float a = 1.0f;

        boolean textureTrigger = true;
        for (String f : objIndices) {
            if (f.equals("merona_bar") || f.equals("merona_bottom")) {
                r = MERONA_R;
                g = MERONA_G;
                b = MERONA_B;
                textureTrigger = false;
            } else if (f.equals("merona_stick")) {
                r = STICK_R;
                g = STICK_G;
                b = STICK_B;
                textureTrigger = true;
            }

            else {
                String[] parts = f.split("/");
                short position = Short.parseShort(parts[0]);
                short texture = Short.parseShort(parts[1]);
                short normals = Short.parseShort(parts[2]);

                meronaPositionData.add(objPositions.get((short) (position - 1))[0]);
                meronaPositionData.add(objPositions.get((short) (position - 1))[1]);
                meronaPositionData.add(objPositions.get((short) (position - 1))[2]);
                if (textureTrigger) {
                    meronaTextureCoordinateData.add(objTextures.get((short)(texture-1))[0]);
                    meronaTextureCoordinateData.add(objTextures.get((short)(texture-1))[1]);
                }
                meronaNormalData.add(objNormals.get((short)(normals-1))[0]);
                meronaNormalData.add(objNormals.get((short)(normals-1))[1]);
                meronaNormalData.add(objNormals.get((short)(normals-1))[2]);
                meronaColorData.add(r);
                meronaColorData.add(g);
                meronaColorData.add(b);
                meronaColorData.add(a);
            }
        }
    }




}

