package io.flybird.util.task;

public interface WorkerTask<I> {
    String getID();
    I call();
}
