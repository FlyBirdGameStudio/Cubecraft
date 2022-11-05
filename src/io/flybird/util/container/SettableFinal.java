package io.flybird.util.container;

public class SettableFinal<T> {
    private boolean modified =false;
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if(!this.modified){
            this.value=value;
            this.modified =true;
        }else{
            throw new IllegalStateException("you can not modify a final value:)");
        }
    }
}
