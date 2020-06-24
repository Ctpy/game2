package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import terrain.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        RawModel model = OBJLoader.loadOBJModel("tree", loader);
        RawModel model2 = OBJLoader.loadOBJModel("stall", loader);

        ModelTexture texture = new ModelTexture(loader.loadTexture("tree"));
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("stallTexture"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        TexturedModel texturedModel2 = new TexturedModel(model2, texture2);

        Random random = new Random();
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            entities.add(new Entity(texturedModel, new Vector3f(random.nextInt(1000), 0, random.nextInt(1000)), 0, 0, 0, 10));
        }
        entities.add(new Entity(texturedModel2, new Vector3f(50, 0, 50), 0, 180,0, 3));


        Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));

        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();

        MasterRender masterRender = new MasterRender();
        while (!Display.isCloseRequested()){
            camera.move();

            masterRender.processTerrain(terrain);
            masterRender.processTerrain(terrain2);
            for (Entity entity: entities) {
                masterRender.processEntity(entity);
            }

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
