package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GUIRender;
import gui.GUITexture;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));



        RawModel model = OBJLoader.loadOBJModel("tree", loader);
        RawModel model2 = OBJLoader.loadOBJModel("stall", loader);
        RawModel model3 = OBJLoader.loadOBJModel("dragon", loader);



        ModelTexture texture = new ModelTexture(loader.loadTexture("tree"));
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("stallTexture"));
        ModelTexture texture3 = new ModelTexture(loader.loadTexture("kingblue"));
        TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));

        ModelTexture textureAtlasFern = new ModelTexture(loader.loadTexture("fern"));
        textureAtlasFern.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader), textureAtlasFern);

        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setHasFakeLighting(true);
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setHasFakeLighting(true);
        TexturedModel texturedModel = new TexturedModel(model, texture);
        TexturedModel texturedModel2 = new TexturedModel(model2, texture2);
        TexturedModel texturedModel3 = new TexturedModel(model3, texture3);
        Terrain terrain = new Terrain(0, 0, loader, terrainTexturePack, blendMap, "heightmap");
        Terrain terrain2 = new Terrain(1, 0, loader, terrainTexturePack, blendMap, "heightmap");
        Random random = new Random();
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < 500; i++){
            int x1 = random.nextInt(800);
            int z1 = random.nextInt(800);
            int x2 = random.nextInt(800);
            int z2 = random.nextInt(800);
            int x3 = random.nextInt(800);
            int z3 = random.nextInt(800);
            entities.add(new Entity(texturedModel, new Vector3f(x1, terrain.getTerrainHeight(x1, z1), z1), 0, 0, 0, 10));
            entities.add(new Entity(grass, new Vector3f(x2, terrain.getTerrainHeight(x2, z2), z2), 0, 0, 0, 1));
            entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x3, terrain.getTerrainHeight(x3, z3), z3), 0, 0, 0, 1));
        }
        entities.add(new Entity(texturedModel2, new Vector3f(50, 0, 50), 0, 180,0, 3));
        entities.add(new Entity(texturedModel3, new Vector3f(0, 0, 0), 0, 90,0, 1));


        Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
        Light light2 = new Light(new Vector3f(-200,10,-200), new Vector3f(10,1,1));
        Light light3 = new Light(new Vector3f(200,10,200), new Vector3f(1,10,1));
        Light light4 = new Light(new Vector3f(300,10,200), new Vector3f(1,1,10));
        List<Light> lights = new ArrayList<>();
        lights.add(light);
        lights.add(light2);
        lights.add(light3);
        lights.add(light4);
        RawModel bunnyModel = OBJLoader.loadOBJModel("person", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));
        Player player = new Player(stanfordBunny, new Vector3f(800, 0, 100), 0, 0, 0, 1);
        Camera camera = new Camera(player);
        MasterRender masterRender = new MasterRender();

        List<GUITexture> guiTextures = new ArrayList<>();
        GUITexture gui = new GUITexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        guiTextures.add(gui);

        GUIRender guiRender = new GUIRender(loader);

        while (!Display.isCloseRequested()){
            camera.move();
            player.move(terrain);
            masterRender.processEntity(player);
            masterRender.processTerrain(terrain);
            masterRender.processTerrain(terrain2);
            for (Entity entity: entities) {
                masterRender.processEntity(entity);
            }

            //game logic
            //render
            masterRender.render(lights, camera);
            guiRender.renderGUI(guiTextures);
            DisplayManager.updateDisplay();
        }
        guiRender.cleanup();
        masterRender.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
