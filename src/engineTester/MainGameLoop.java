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
        RawModel model3 = OBJLoader.loadOBJModel("dragon", loader);



        ModelTexture texture = new ModelTexture(loader.loadTexture("tree"));
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("stallTexture"));
        ModelTexture texture3 = new ModelTexture(loader.loadTexture("kingblue"));
        TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
        TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setHasFakeLighting(true);
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setHasFakeLighting(true);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        TexturedModel texturedModel2 = new TexturedModel(model2, texture2);
        TexturedModel texturedModel3 = new TexturedModel(model3, texture3);

        Random random = new Random();
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < 500; i++){
            entities.add(new Entity(texturedModel, new Vector3f(random.nextInt(1000), 0, random.nextInt(1000)), 0, 0, 0, 10));
            entities.add(new Entity(grass, new Vector3f(random.nextInt(1000), 0, random.nextInt(1000)), 0, 0, 0, 1));
            entities.add(new Entity(fern, new Vector3f(random.nextInt(1000), 0, random.nextInt(1000)), 0, 0, 0, 1));
        }
        entities.add(new Entity(texturedModel2, new Vector3f(50, 0, 50), 0, 180,0, 3));
        entities.add(new Entity(texturedModel3, new Vector3f(0, 0, 0), 0, 90,0, 1));


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
                entity.getModel().getTexture().setReflectivity(1);
                entity.getModel().getTexture().setShineDamper(10);
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
