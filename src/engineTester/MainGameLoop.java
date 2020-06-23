package engineTester;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import renderEngine.*;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
        Render render = new Render();
        StaticShader staticShader = new StaticShader();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
        };

        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };

        RawModel model = loader.loadToVAO(vertices, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        while (!Display.isCloseRequested()){
            render.prepare();
            staticShader.start();
            render.render(texturedModel);
            //game logic
            //render
            staticShader.stop();
            DisplayManager.updateDisplay();
        }

        staticShader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
