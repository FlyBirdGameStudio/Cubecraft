package io.flybird.starfish3d.render.multiThread;

import io.flybird.util.container.ArrayQueue;

public class AsyncRenderCompileService<V extends VertexArrayCompileCallable> implements IDrawService<V>{
    private final ArrayQueue<DrawCompile> multiDrawResult=new ArrayQueue<>();
    private final ArrayQueue<IDrawCompile> all =new ArrayQueue<>();

    @Override
    public void startDrawing(V v){
        IDrawCompile[] res=v.compile();
        for (IDrawCompile dc:res) {
            if(dc instanceof DrawCompile) {
                this.multiDrawResult.add((DrawCompile) dc);
            }
            this.all.add(dc);
        }
    }

    @Override
    public int getResultSize() {
        return this.multiDrawResult.size();
    }

    @Override
    public DrawCompile getAvailableCompile(){
        return this.multiDrawResult.poll();
    }

    @Override
    public IDrawCompile getAllCompile(){
        return this.all.poll();
    }

    @Override
    public int getAllResultSize() {
        return this.all.size();
    }
}
