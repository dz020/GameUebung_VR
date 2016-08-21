package com.example.jackson.gameuebung_3.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.jackson.gameuebung_3.VertexBuffer;
import com.example.jackson.gameuebung_3.VertexElement;
import com.example.jackson.gameuebung_3.math.Matrix4x4;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by Jackson on 29.03.2016.
 */
public class GraphicsDevice {

    private static final String TAG = "GraphicsDevice";
    private GL10 gl;

    private int mProgram;
    private int mProjViewModelHandle;
    private int mPositionHandle, mPositionHandle2;
    private int mColorHandle;
    private int mTextureCoordHandle;
    private Camera camera;
    private Matrix4x4 m_world;

    public void onSurfaceCreated(GL10 gl){
        //this.gl = gl;
        //GLES20 gl2;
        //GLES20.glHint(GLES20.GL_PERSPECTIVE_CORRECTION_HINT, GLES20.GL_NICEST);
        String vertexShaderCode =
                "uniform mat4 projViewModel; " +
                "attribute vec4 position;" +
                "attribute vec2 inputTextureCoordinate;" +
                "varying vec2 textureCoordinate;" +
                "void main()" +
                "{" +
                    "gl_Position = projViewModel * position;" +
                    "textureCoordinate = inputTextureCoordinate;" +
                "}";
        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);

        //dieser shader macht das rendering der textur auf die 3d modelle, der passt!
        String fragmentShaderCode =
                "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "varying vec2 textureCoordinate;                            \n" +
                "uniform sampler2D s_texture;               \n" +
                "void main(void) {" +
                    "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                    //"  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                "}";
        int fragmentShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
        mProjViewModelHandle = GLES20.glGetUniformLocation(mProgram, "projViewModel");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");

    }

    public void clear(float red, float green, float blue, float alpha){
        GLES20.glClearColor(red, green, blue, alpha); //farbe für view (fullscreen) als rgba übergeben
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); //sorgt dafür das farbe überschrieben wird
    }

    public void clear(float red, float green, float blue){ //mit fixem alpha
        GLES20.glClearColor(red, green, blue, 1.0f); //farbe für view (fullscreen) als rgba übergeben
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); //sorgt dafür das farbe überschrieben wird
    }

    public void clear(float red, float green, float blue, float alpha, float depth)
    {
        GLES20.glClearColor(red, green, blue, alpha);
        GLES20.glClearDepthf(depth);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    public void resize(int width, int height){ //damit bei änderung der auflösung auch der viewport angegelichen wird
        GLES20.glViewport(0, 0, width, height);
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    public void bindVertexBuffer(VertexBuffer vertexBuffer){
        ByteBuffer buffer = vertexBuffer.getByteBuffer();

        for (VertexElement element : vertexBuffer.getVertexElements()) {
            int offset = element.getOffset();
            int stride = element.getStride();
            int type = element.getType();
            int count = element.getCount();

            buffer.position(offset);

            switch (element.getSemantic()) {
                case VERTEX_ELEMENT_POSITION:
                    //GLES20.glEnableClientState(GLES20.GL_VERTEX_ARRAY);
                    //GLES20.glVertexPointer(count, type, stride, buffer);

                    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
                    GLES20.glEnableVertexAttribArray(mPositionHandle);
                    GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                            false, stride, buffer);
                    break;

                case VERTEX_ELEMENT_COLOR:
                    //GLES20.glEnableClientState(GLES20.GL_COLOR_ARRAY);
                    //GLES20.glColorPointer(count, type, stride, buffer);
                    //mColorHandle = GLES20.glGetAttribLocation(mProgram, "s_texture");
                    break;

                case VERTEX_ELEMENT_TEXCOORD:
                    //GLES20.glEnableClientState(GLES20.GL_TEXTURE_COORD_ARRAY);
                    //GLES20.glTexCoordPointer(count, type, stride, buffer);

                    mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
                    GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
                    GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT,
                            false, stride, buffer);
                    break;
            }
        }
    }

    public void unbindVertexBuffer(VertexBuffer vertexBuffer){
        for (VertexElement element : vertexBuffer.getVertexElements()) {
            switch (element.getSemantic()) {
                case VERTEX_ELEMENT_POSITION:
                    //GLES20.glDisableClientState(GLES20.GL_VERTEX_ARRAY);
                    GLES20.glDisableVertexAttribArray(mPositionHandle);
                    break;

                case VERTEX_ELEMENT_COLOR:
                    //GLES20.glDisableClientState(GLES20.GL_COLOR_ARRAY);
                    break;

                case VERTEX_ELEMENT_TEXCOORD:
                    //GLES20.glDisableClientState(GLES20.GL_TEXTURE_COORD_ARRAY);
                    GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
                    break;
            }
        }
    }

    public void draw(int mode, int count){
        GLES20.glUseProgram(mProgram);

        //macht die texture transparent
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        Matrix4x4 m_proj = camera.getM_projection();
        Matrix4x4 m_view = camera.getM_view();
        Matrix4x4 m_proj_view = Matrix4x4.multiply(m_proj, m_view);
        Matrix4x4 m_proj_view_world = Matrix4x4.multiply(m_proj_view, m_world);
        GLES20.glUniformMatrix4fv(mProjViewModelHandle, 1, false, m_proj_view_world.m, 0);

        GLES20.glDrawArrays(mode, 0, count);
        //GLES20.glUseProgram(0);
    }

    public void setWorldMatrix(Matrix4x4 m_world) {
        this.m_world = m_world;
    }

    public void setColor(float r, float g, float b, float a){
        //GLES20.glColor4f(r, g, b, a);
    }

    /**
     * Erlaubt Bilder im png oder jpeg format zu laden
     * @param stream
     * @return
     */
    public Texture createTexture(InputStream stream) {
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        if (bitmap == null) {
            return null;
        }
        return createTexture(bitmap);
    }

    public Texture createTexture(Bitmap bitmap) {
        int level = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Texture Handle erstellen
        int[] handles = new int[1];
        GLES20.glGenTextures(1, handles, 0);

        // Texture binden
        int handle = handles[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle);

        Texture texture = new Texture(handle, width, height);

        // Bitmap an der Y-Achse spiegeln
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        // MipMaps erzeugen und laden
        while (width >= 1 && height >= 1) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, level, bitmap, 0);

            if(height == 1 || width == 1)
                break;

            level++;
            height /= 2;
            width /= 2;

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        return texture;
    }

    public void bindTexture(Texture texture) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getHandle()); //texture.getHandle ist immer 2
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
    }

    public void unbindTexture() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDisable(GLES20.GL_TEXTURE_2D);
    }







    //neu
    public void setAlphaTest(CompareFunction func, float referenceValue) {
        if (func == CompareFunction.ALWAYS) {
            //GLES20.glDisable(GLES20.GL_ALPHA_TEST);
        } else {
            //GLES20.glEnable(GLES20.GL_ALPHA_TEST);
            //GLES20.glAlpha(getGLConstant(func), referenceValue);
        }
    }

    public void setBlendFactors(BlendFactor srcFactor, BlendFactor dstFactor) {
        if (srcFactor == BlendFactor.ONE && dstFactor == BlendFactor.ZERO) {
            GLES20.glDisable(GLES20.GL_BLEND);
        } else {
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(getGLConstant(srcFactor), getGLConstant(dstFactor));
        }
    }

    public void setCullSide(Side side) {
        if (side == Side.NONE) {
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        } else {
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            GLES20.glCullFace(getGLConstant(side));
        }
    }

    public void setDepthTest(CompareFunction func) {
        if (func == CompareFunction.ALWAYS) {
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        } else {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glDepthFunc(getGLConstant(func));
        }
    }

    public void setDepthWrite(boolean enabled) {
        GLES20.glDepthMask(enabled);
    }

    public void setMaterialColor(float[] color) {
        setMaterialColor(color[0], color[1], color[2], color[3]);
    }

    public void setMaterialColor(float red, float green, float blue, float alpha) {
        //GLES20.glColor4f(red, green, blue, alpha);
    }

    public void setTextureBlendColor(float[] color) {
        //GLES20.glTexEnvfv(GLES20.GL_TEXTURE_ENV, GLES20.GL_TEXTURE_ENV_COLOR, color, 0);
    }

    public void setTextureBlendColor(float red, float green, float blue, float alpha) {
        setTextureBlendColor(new float[]{red, green, blue, alpha});
    }

    public void setTextureBlendMode(TextureBlendMode blendMode) {
        //GLES20.glTexEnvx(GLES20.GL_TEXTURE_ENV, GLES20.GL_TEXTURE_ENV_MODE, getGLConstant(blendMode));
    }

    public void setTextureFilters(TextureFilter filterMin, TextureFilter filterMag) {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, getGLConstant(filterMin));
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, getGLConstant(filterMag));
    }

    public void setTextureWrapMode(TextureWrapMode wrapU, TextureWrapMode wrapV) {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, getGLConstant(wrapU));
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, getGLConstant(wrapV));
    }

    private static int getGLConstant(BlendFactor blendFactor) {
        switch (blendFactor) {
            case ZERO: 						return GLES20.GL_ZERO;
            case ONE:						return GLES20.GL_ONE;
            case SRC_COLOR:					return GLES20.GL_SRC_COLOR;
            case ONE_MINUS_SRC_COLOR:		return GLES20.GL_ONE_MINUS_SRC_COLOR;
            case DST_COLOR:					return GLES20.GL_DST_COLOR;
            case ONE_MINUS_DST_COLOR:		return GLES20.GL_ONE_MINUS_DST_COLOR;
            case SRC_ALPHA:					return GLES20.GL_SRC_ALPHA;
            case ONE_MINUS_SRC_ALPHA:		return GLES20.GL_ONE_MINUS_SRC_ALPHA;
            case DST_ALPHA:					return GLES20.GL_DST_ALPHA;
            case ONE_MINUS_DST_ALPHA:		return GLES20.GL_ONE_MINUS_DST_ALPHA;
            default:						throw new InvalidParameterException("Unknown value.");
        }
    }

    private static int getGLConstant(CompareFunction func) {
        switch (func) {
            case NEVER:						return GLES20.GL_NEVER;
            case ALWAYS:					return GLES20.GL_ALWAYS;
            case LESS:						return GLES20.GL_LESS;
            case LESS_OR_EQUAL:				return GLES20.GL_LEQUAL;
            case EQUAL:						return GLES20.GL_EQUAL;
            case GREATER_OR_EQUAL:			return GLES20.GL_GEQUAL;
            case GREATER:					return GLES20.GL_GREATER;
            case NOT_EQUAL:					return GLES20.GL_NOTEQUAL;
            default:						throw new InvalidParameterException("Unknown value.");
        }
    }

    private static int getGLConstant(Side side) {
        switch (side) {
            case NONE:						return 0;
            case FRONT:						return GLES20.GL_FRONT;
            case BACK:						return GLES20.GL_BACK;
            case FRONT_AND_BACK:			return GLES20.GL_FRONT_AND_BACK;
            default:						throw new InvalidParameterException("Unknown value.");
        }
    }

    private static int getGLConstant(TextureFilter filter) {
        switch (filter) {
            case NEAREST:					return GLES20.GL_NEAREST;
            case NEAREST_MIPMAP_NEAREST:	return GLES20.GL_NEAREST_MIPMAP_NEAREST;
            case NEAREST_MIPMAP_LINEAR:		return GLES20.GL_NEAREST_MIPMAP_LINEAR;
            case LINEAR:					return GLES20.GL_LINEAR;
            case LINEAR_MIPMAP_NEAREST:		return GLES20.GL_LINEAR_MIPMAP_NEAREST;
            case LINEAR_MIPMAP_LINEAR:		return GLES20.GL_LINEAR_MIPMAP_LINEAR;
            default:						throw new InvalidParameterException("Unknown value.");
        }
    }

    private static int getGLConstant(TextureWrapMode wrapMode) {
        switch (wrapMode) {
            case CLAMP:						return GLES20.GL_CLAMP_TO_EDGE;
            case REPEAT:					return GLES20.GL_REPEAT;
            default:						throw new InvalidParameterException("Unknown value.");
        }
    }

    private int loadGLShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }
}

