package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        RawModel model = OBJLoader.loadOBJModel("dragon", loader);

        ModelTexture texture = new ModelTexture(loader.loadTexture("kingblue"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1, 1));

        Camera camera = new Camera();

        MasterRender masterRender = new MasterRender();
        while (!Display.isCloseRequested()){
            entity.increaseRotation(0, 1, 0);
            camera.move();
            masterRender.processEntity(entity);
            //game logic
            //render
            masterRender.render(light, camera);
            DisplayManager.updateDisplay();
        }

        masterRender.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
