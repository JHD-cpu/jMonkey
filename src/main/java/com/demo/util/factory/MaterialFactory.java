package com.demo.util.factory;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

public class MaterialFactory {

    /**
     * 受光材质
     * @param assetManager
     * @return Material
     */
    public static Material CreateMatDefsLightLighting(AssetManager assetManager){
        return new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
    }

    /**
     *  无光材质
     * @param assetManager
     * @return Material
     */
    public static Material CreateMatDefsMiscUnshaded(AssetManager assetManager){
        return new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
    }
}
