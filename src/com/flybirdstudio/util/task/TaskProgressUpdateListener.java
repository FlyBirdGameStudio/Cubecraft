package com.flybirdstudio.util.task;

public interface TaskProgressUpdateListener {
    void onProgressChange(int prog);
    void onProgressStageChanged(String newStage);
    default void refreshScreen(){};
}
