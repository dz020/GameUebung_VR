package com.example.jackson.gameuebung_3.graphics;

/**
 * Created by Jackson on 12.04.2016.
 */

import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.VertexBuffer;
import com.example.jackson.gameuebung_3.math.Matrix4x4;

public class Renderer {

    private GraphicsDevice graphicsDevice;

    public Renderer(GraphicsDevice graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
    }

    public void drawMesh(Mesh mesh, Material material, Matrix4x4 world) {
        graphicsDevice.setWorldMatrix(world);

        setupMaterial(material);

        VertexBuffer vertexBuffer = mesh.getVertexBuffer();
        graphicsDevice.bindVertexBuffer(vertexBuffer);
        graphicsDevice.draw(mesh.getMode(), vertexBuffer.getNumVertices());
        graphicsDevice.unbindVertexBuffer(vertexBuffer);
        graphicsDevice.unbindTexture();
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }

    private void setupMaterial(Material material) {
        graphicsDevice.bindTexture(material.getTexture());
        graphicsDevice.setTextureFilters(material.getTextureFilterMin(), material.getTextureFilterMag());
        graphicsDevice.setTextureWrapMode(material.getTextureWrapModeU(), material.getTextureWrapModeV());
        graphicsDevice.setTextureBlendMode(material.getTextureBlendMode());
        graphicsDevice.setTextureBlendColor(material.getTextureBlendColor());

        graphicsDevice.setMaterialColor(material.getMaterialColor());
        graphicsDevice.setBlendFactors(material.getBlendSourceFactor(), material.getBlendDestFactor());

        graphicsDevice.setCullSide(material.getCullSide());
        graphicsDevice.setDepthTest(material.getDepthTestFunction());
        graphicsDevice.setDepthWrite(material.getDepthWrite());
        graphicsDevice.setAlphaTest(material.getAlphaTestFunction(), material.getAlphaTestValue());
    }

    public void drawText(TextBuffer textBuffer, Matrix4x4 world) {
        drawMesh(textBuffer.getMesh(), textBuffer.getSpriteFont().getMaterial(), world);
    }
}
