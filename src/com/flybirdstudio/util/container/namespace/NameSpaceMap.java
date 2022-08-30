package com.flybirdstudio.util.container.namespace;

import com.flybirdstudio.util.container.CollectionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * this map is designed for name spaced item placing and indexing.
 * it can simply replace using 2d array by namespace in column,id in row.
 * @param <I>
 */
public class NameSpaceMap <I>{
    private final HashMap<String,HashMap<String,I>> map=new HashMap<>();
    private final String split;

    /**
     * @param split split char
     */
    public NameSpaceMap(String split){
        this.split=split;
    }

    /**
     * namespace item:put
     * if there's no namespace it will create a new one.
     * @param id id
     * @param namespace namespace
     * @param item item
     */
    public void set(String id,String namespace,I item){
        if(!this.map.containsKey(namespace)){
            this.map.put(namespace,new HashMap<>());
        }
        if(this.map.get(namespace).containsKey(id)){
            throw new RuntimeException("conflict register!");
        }
        this.map.get(namespace).put(id,item);
    }

    /**
     * use all string as alternative method. for example:
     * "sunrise_studio:jsysb"will do this:
     * namespace=sunrise_studio,id=jsysb
     * @param all string
     * @param item item
     */
    public void set(String all,I item){
        this.set(all.split(this.split)[1],all.split(this.split)[0],item);
    }

    /**
     * get an item using full string.
     * @return nothing
     */
    public I get(String all){
        return this.get(all.split(this.split)[1],all.split(this.split)[0]);
    }

    /**
     * get an item with id and namespace
     * @param id id
     * @param namespace namespace
     * @return item
     * @throws RuntimeException if not found(namespace/id)
     */
    public I get(String id, String namespace) {
        if(!this.map.containsKey(namespace)){
            throw new RuntimeException("namespace not found:"+namespace);
        }
        if(!this.map.get(namespace).containsKey(id)){
            throw new RuntimeException("item not found:"+id);
        }
        return this.map.get(namespace).get(id);
    }

    public Collection<String> idList(){
        Collection<String> list=new ArrayList<>();
        CollectionUtil.iterateMap(map, (key, item) -> {
            for (String k:item.keySet()){
                list.add(key+":"+k);
            }
        });
        return list;
    }
}
