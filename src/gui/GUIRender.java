package gui;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;
import toolbox.Math;

import java.util.List;

public class GUIRender {

    private final RawModel quad;
    private GUIShader shader;

    public GUIRender(Loader loader){
        float[] positons = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positons);
        shader = new GUIShader();
    }

    public void renderGUI(List<GUITexture> guiTextureList){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        //render
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        for (GUITexture guiTexture: guiTextureList){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, guiTexture.getTexture());
            Matrix4f matrix = Math.createTransformationMatrix(guiTexture.getPosition(), guiTexture.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanup(){
        shader.cleanUp();
    }
}
