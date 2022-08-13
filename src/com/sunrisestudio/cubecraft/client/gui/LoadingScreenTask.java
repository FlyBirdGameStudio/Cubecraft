package com.sunrisestudio.cubecraft.client.gui;

public abstract class LoadingScreenTask implements Runnable{
    private int status;
    private boolean isCancelled;
    private String text;

    public void setText(String t){
        this.text=t;
    }

    public void cancel(){
        this.isCancelled=true;
    }

    public int getStatus() {
        return status;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public String getText() {
        return text;

    }

    public void setStatus(int status) {
        this.status = status;
    }
}
