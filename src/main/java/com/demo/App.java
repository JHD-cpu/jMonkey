package com.demo;

import com.demo.util.factory.MaterialFactory;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import java.awt.*;
import java.util.Random;

public class App  extends SimpleApplication {

    public  Geometry geometry;


    public static void main(String[] args) {
        App app = new App();
        app.setShowSettings(false);
        app.start();
    }

    public App() {
        init();
    }

    @Override
    public void simpleInitApp() {
        draw();

    }

    public void  draw(){


        for (int i = 0; i < 300; i++) {
            rootNode.attachChild(createPoint(i+""));
        }
    }

    private Geometry createPoint(String name){
        Random random = new Random();
        Sphere sphere = new Sphere(30,30,1);
        Material material = MaterialFactory.CreateMatDefsMiscUnshaded(assetManager);
        Geometry geometry = new Geometry(name);
        material.setColor("Color",ColorRGBA.White);
        geometry.setMesh(sphere);
        geometry.setMaterial(material);
        geometry.move(new Vector3f(3+random.nextFloat(1000),3+random.nextFloat(1000),3+random.nextFloat(1000)));
        return geometry;
    }

    @Override
    public void simpleUpdate(float tpf) {
        float speed = FastMath.TWO_PI;
        if(geometry!=null){
            geometry.rotate(0,speed*tpf,0);
        }
    }

    /**
     * 无光材质 小球
     */
    private void  addNotLightSphere(){
        Mesh sphere = new Sphere(30,30,1);
        Material material = MaterialFactory.CreateMatDefsMiscUnshaded(assetManager);
        material.setColor("Color", ColorRGBA.Red);
        Geometry geometry = new Geometry("NotLightMem");
        geometry.setMesh(sphere);
        geometry.setMaterial(material);
        rootNode.attachChild(geometry);
    }

    /**
     * 受光材质 小球
     */
    private void  addLightSphere(){
        Mesh sphere = new Sphere(30,30,1);
        Material material = MaterialFactory.CreateMatDefsLightLighting(assetManager);
        material.setColor("Diffuse", ColorRGBA.Red);// 在漫射光照射下反射的颜色。
        material.setColor("Ambient", ColorRGBA.Red);// 在环境光照射下，反射的颜色。
        material.setColor("Specular", ColorRGBA.White);// 镜面反射时，高光的颜色。
        Geometry geometry = new Geometry("NotLightMem");
        geometry.setMesh(sphere);
        geometry.setMaterial(material);
        geometry.move(0, 3, 0);
        rootNode.attachChild(geometry);
    }

    /**
     * 无光立方体
     */
    public  void  addUnshadedBox(){
        Mesh box = new Box(1,1,1);
        Material material = MaterialFactory.CreateMatDefsMiscUnshaded(assetManager);
        Texture tex = assetManager.loadTexture("Textures/linggan.jpg");
        material.setTexture("ColorMap", tex);// 设置贴图
        Geometry geometry = new Geometry();
        geometry.setMesh(box);
        geometry.setMaterial(material);
        geometry.move(4, 0, 0);
        rootNode.attachChild(geometry);

    }

    /**
     * 创造一个方块，应用受光材质。
     *
     */
    private void addLightingBox() {
        Box box = new Box(3, 3, 3);
        Material mat =MaterialFactory.CreateMatDefsMiscUnshaded(assetManager);
        Texture tex = assetManager.loadTexture("Textures/zcx.jpeg");

        mat.setTexture("ColorMap",tex);
        mat.setTransparent(true);
        Geometry geom = new Geometry("文艺方块");
        geom.setMesh(box);
        geom.setMaterial(mat);
        geom.move(9,9,-9);
        rootNode.attachChild(geom);
    }

    public void addLight(){
//      定向光
        DirectionalLight sun  = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3));
        //环境光
        AmbientLight ambient = new AmbientLight();
        // 调整光照亮度
        ColorRGBA lightColor = new ColorRGBA();
        sun.setColor(ColorRGBA.White);
        ambient.setColor(ColorRGBA.Orange);
        rootNode.addLight(sun );
    }

    private void init(){
        addLight();

        int width=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    }
}
