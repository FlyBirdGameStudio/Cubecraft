package com.sunrisestudio.cubecraft.gui.layout;

public class ViewportLayout extends LayoutManager{
    public float left,right,bottom,top;

    public ViewportLayout(int left,int right,int bottom,int top,Border border,int layer){
        this.left=left;
        this.right=right;
        this.bottom=bottom;
        this.top=top;
        this.layer=layer;
        this.border=border;
    }

    @Override
    public void resize(int scrWidth, int scrHeight) {
        this.ax= (int) (left/100*scrWidth);
        this.ay= (int) (top/100*scrHeight);
        this.width= (int) (right/100*scrWidth-ax);
        this.height= (int) (bottom/100*scrHeight-ay);
        this.ax+=this.border.left;
        this.ay+=this.border.top;
        this.width-=this.border.right*2;
        this.height-=this.border.bottom*2;
    }
}
