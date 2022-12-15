package io.flybird.util.network.base;

public abstract class UDPEventLoop implements Runnable{
    protected final UDPPipeline parent;
    public UDPEventLoop(UDPPipeline udpPipeline) {
        this.parent=udpPipeline;
    }

    private boolean run=true;

    @Override
    public void run() {
        while (run){
            this.handle();
        }
    }

    public void stop(){
        this.run=false;
    }

    public abstract void handle();
}
