package com.example.whenyoucomemerona.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.example.whenyoucomemerona.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static java.nio.ByteBuffer.allocateDirect;

public class mGLSurfaceRenderer implements GLSurfaceView.Renderer {

    Context ctx;


    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mLightModeMatrix = new float[16];

    ArrayList<Float> VerticesData;
    ArrayList<Float> ColorData;
    ArrayList<Float> NormalData;
    private final FloatBuffer VerticesBuffer;
    private final FloatBuffer ColorBuffer;
    private final FloatBuffer NormalBuffer;

    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mNormalHandle;

    private final int BYTES_PER_FLOAT = 4;


    // offset
    private final int mPositionOffset = 0;
    private final int mColorOffset = 3;

    // data size
    private final int mPositionDataSize = 3;
    private final int mColorDataSize = 4;
    private final int mNormalDataSize = 3;

    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];
    private int mPerVertexProgramHandle;
    private int mLightPosHandle;
    private int mPointProgramHandle;



    private final int mStrideBytes = (mPositionDataSize + mColorDataSize) * BYTES_PER_FLOAT;
    private float[] mLightModelMatrix = new float[16];


    public mGLSurfaceRenderer(Context ctx)
    {
        this.ctx = ctx;
        // Define points for equilateral triangles.


        VerticesData = new ArrayList<>();
        ColorData = new ArrayList<>();
        NormalData = new ArrayList<>();
        readfile("merona.obj");

        // initialize
        VerticesBuffer = allocateDirect(VerticesData.size() * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        ColorBuffer = allocateDirect(ColorData.size() * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        NormalBuffer = allocateDirect(NormalData.size() * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        for (float vertex : VerticesData) VerticesBuffer.put(vertex);
        for (float color : ColorData) ColorBuffer.put(color);
        for (float normal : NormalData) NormalBuffer.put(normal);
        VerticesBuffer.position(0);
        ColorBuffer.position(0);
        NormalBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        GLES20.glClearColor(1.f, 1.f, 1.f, 1.f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        // set up camera
        initCamera();

        String vertexShaderCode = "";
        String fragmentShaderCode = "";
        String pixelVertexShaderCode = "";
        String pixelFragmentShaderCode = "";
        try {
            InputStream vertexShaderStream = ctx.getResources().openRawResource(R.raw.vertex_shader);
            vertexShaderCode = IOUtils.toString(vertexShaderStream, Charset.defaultCharset());
            vertexShaderStream.close();

            // Convert fragment_shader.txt to a string
            InputStream fragmentShaderStream = ctx.getResources().openRawResource(R.raw.fragment_shader);
            fragmentShaderCode = IOUtils.toString(fragmentShaderStream, Charset.defaultCharset());
            fragmentShaderStream.close();

            InputStream pixelVertexShaderStream = ctx.getResources().openRawResource(R.raw.pixel_vertex_shader);
            pixelVertexShaderCode = IOUtils.toString(pixelVertexShaderStream, Charset.defaultCharset());
            pixelVertexShaderStream.close();

            InputStream pixelFragmentShaderStream = ctx.getResources().openRawResource(R.raw.pixel_fragment_shader);
            pixelFragmentShaderCode = IOUtils.toString(pixelFragmentShaderStream, Charset.defaultCharset());
            pixelFragmentShaderStream.close();

        } catch (IOException e) {

        }






        final int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        final int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        final int pixelVertexShader = compileShader(GLES20.GL_VERTEX_SHADER, pixelVertexShaderCode);
        final int pixelFragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, pixelFragmentShaderCode);

        mPerVertexProgramHandle = createAndLinkProgram(vertexShader, fragmentShader,
                new String[] {"a_Position",  "a_Color", "a_Normal"});
        mPointProgramHandle = createAndLinkProgram(pixelVertexShader, pixelFragmentShader,
                new String[] {"a_Position"});


//        GLES20.glUseProgram(programHandle);
    }



    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
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
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        GLES20.glUseProgram(mPerVertexProgramHandle);

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        // Draw some cubes.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 4.0f, 0.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 0.0f, 0.0f);
        draw();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -4.0f, 0.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        draw();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 4.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        draw();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -4.0f, -7.0f);
        draw();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 0.0f);
        draw();

        // Draw a point to indicate the light.
        GLES20.glUseProgram(mPointProgramHandle);
        drawLight();
    }




    private void draw()
    {
        // Pass in the position information
        VerticesBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle,
                mPositionDataSize,
                GLES20.GL_FLOAT,
                false,
                0,
                VerticesBuffer);

        // Pass in the color information
        ColorBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle,
                mColorDataSize,
                GLES20.GL_FLOAT,
                false,
                0,
                ColorBuffer);

        NormalBuffer.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle,
                mNormalDataSize,
                GLES20.GL_FLOAT,
                false,
                0,
                NormalBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VerticesData.size() / 3);
    }

    private void initCamera() {
        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }



    // read file
    private void readfile(String file) {

        List<Float[]> objPositions = new ArrayList<>();
        List<Float[]> objTextures = new ArrayList<>();
        List<Float[]> objNormals = new ArrayList<>();
        List<String> objIndices = new ArrayList<>();

        // 파일을 읽습니다.
        Scanner scanner = null;
        try {
            scanner = new Scanner(ctx.getAssets().open(file));

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
                }
            }
            scanner.close();
        } catch (IOException e) {
            // IOException
        }


        for (String f : objIndices) {
            String[] parts = f.split("/");
            short position = Short.parseShort(parts[0]);
            short texture = Short.parseShort(parts[1]);
            short normals = Short.parseShort(parts[2]);

            VerticesData.add(objPositions.get((short) (position - 1))[0]);
            VerticesData.add(objPositions.get((short) (position - 1))[1]);
            VerticesData.add(objPositions.get((short) (position - 1))[2]);
//            objVertexData.add(objTextures.get((short)(texture-1))[0]);
//            objVertexData.add(objTextures.get((short)(texture-1))[1]);
            NormalData.add(objNormals.get((short)(normals-1))[0]);
            NormalData.add(objNormals.get((short)(normals-1))[1]);
            NormalData.add(objNormals.get((short)(normals-1))[2]);
            ColorData.add(0.5f);
            ColorData.add(1.0f);
            ColorData.add(0.7f);
            ColorData.add(1.0f);

        }
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


    /**
     * Helper function to compile a shader.
     *
     * @param shaderType The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    private int compileShader(final int shaderType, final String shaderSource)
    {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    /**
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     */
    private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                Log.e("Error", "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
}