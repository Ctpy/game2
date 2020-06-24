package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRender {

    private StaticShader shader = new StaticShader();
    private Render render = new Render(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(Light light, Camera camera){
        render.prepare();
        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);

        shader.stop();
        entities.clear();
    }

    public void cleanUp(){
        shader.cleanUp();
    }
}
