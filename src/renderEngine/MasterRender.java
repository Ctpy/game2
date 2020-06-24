package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shader.StaticShader;
import shader.TerrainShader;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRender {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRender entityRender;

    private TerrainRender terrainRender;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRender(){
        createProjectionMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        entityRender = new EntityRender(shader, projectionMatrix);
        terrainRender = new TerrainRender(terrainShader, projectionMatrix);
    }

    public void render(Light light, Camera camera){
        prepare();
        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        entityRender.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRender.render(terrains);
        terrainShader.stop();
        terrains.clear();
        entities.clear();
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.3f, 0.0f, 0.0f, 1);
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null){
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float)Display.getHeight();
        float y_scale = (float)(1.0D / java.lang.Math.tan(java.lang.Math.toRadians(35.0D)) * (double)aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = 999.9F;
        this.projectionMatrix = new Matrix4f();
        this.projectionMatrix.m00 = x_scale;
        this.projectionMatrix.m11 = y_scale;
        this.projectionMatrix.m22 = -(1000.1F / frustum_length);
        this.projectionMatrix.m23 = -1.0F;
        this.projectionMatrix.m32 = -(200.0F / frustum_length);
        this.projectionMatrix.m33 = 0.0F;
    }

    public void cleanUp(){
        terrainShader.cleanUp();
        shader.cleanUp();
    }
}
