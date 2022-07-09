package com.sunrisestudio.cubecraft.world.block.registery;

import com.sunrisestudio.util.LogHandler;
import com.sunrisestudio.cubecraft.world.block.material.IBlockMaterial;
import com.sunrisestudio.cubecraft.world.block.registery.behavior.BlockBehaviorGetter;
import com.sunrisestudio.cubecraft.world.block.registery.behavior.BlockBehaviorRegistery;
import com.sunrisestudio.cubecraft.world.block.registery.behavior.IBlockBehaviorRegistery;
import com.sunrisestudio.cubecraft.world.block.registery.block.BlockGetter;
import com.sunrisestudio.cubecraft.world.block.registery.block.BlockRegistery;
import com.sunrisestudio.cubecraft.world.block.registery.block.IBlockRegistery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BlockMap {
    public BlockMap(){}
    private static BlockMap instance=new BlockMap();
    public static BlockMap getInstance() {
        return instance;
    }

    private final HashMap<String, IBlockMaterial> blockMaterialHashMap=new HashMap<>();
    private final HashMap<String, IBlockMaterial> blockBehaviorHashMap=new HashMap<>();
    private final ArrayList<Class<? extends IBlockRegistery>> blockRegisterys =new ArrayList<>();
    private final ArrayList<Class<? extends IBlockBehaviorRegistery>> behaviorRegisters =new ArrayList<>();
    private LogHandler handler=LogHandler.create("blockMap","server");
    private float status=0;

    public IBlockMaterial getByID(String id){
        return blockMaterialHashMap.get(id);
    }

    public void addRegistererBlockClass(Class<? extends IBlockRegistery> clazz){
        blockRegisterys.add(clazz);
    }

    /**
     * search all class in jvm,find registerer,invoke them
     */
    public void registerBlock(){
        try{
        //get class with annotation

            //find annotated method
            Iterator<Class<? extends IBlockRegistery>> classIterator= blockRegisterys.iterator();
            while (classIterator.hasNext()){
                Class<? extends IBlockRegistery> clazz=classIterator.next();
                for (Method m:classIterator.next().getMethods()){
                    if(Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof BlockGetter)) {
                        BlockGetter getter = m.getAnnotation(BlockGetter.class);
                        blockMaterialHashMap.put(clazz.getAnnotation(BlockRegistery.class).namespace()
                            + ":" + getter.id(),
                            (IBlockMaterial) m.invoke(clazz.getConstructor().newInstance(), blockBehaviorHashMap.get(getter.behavior())));
                    }
                }
                status+=1f/(behaviorRegisters.size()+blockRegisterys.size());
            }
            status=1;
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            handler.error("can not register block behavior:"+e);
            status+=0.5f;
        }
    }

    public void addRegistererBlockBehaviorClass(Class<? extends IBlockBehaviorRegistery> clazz){
         behaviorRegisters.add(clazz);
    }

    /**
     * search all class in jvm,find registerer,invoke them
     */
    public void registerBlockBehavior(){
        try{
            //find annotated method
            Iterator<Class<? extends IBlockBehaviorRegistery>> classIterator= behaviorRegisters.iterator();
            while (classIterator.hasNext()){
                Class<? extends IBlockBehaviorRegistery> clazz=classIterator.next();
                for (Method m:classIterator.next().getMethods()){
                    if(Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof BlockBehaviorGetter)) {
                        BlockBehaviorGetter getter = m.getAnnotation(BlockBehaviorGetter.class);
                        blockBehaviorHashMap.put(clazz.getAnnotation(BlockBehaviorRegistery.class).namespace()
                                        + ":" + getter.id(),
                                (IBlockMaterial) m.invoke(clazz.getConstructor().newInstance()));
                    }
                }
                status+=1f/(behaviorRegisters.size()+blockRegisterys.size());
            }
            status=1;
        }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e){
            handler.error("can not register block:"+e);
            status+=0.5f;
        }
    }

    public float getStatus(){
        return status;
    }
}
