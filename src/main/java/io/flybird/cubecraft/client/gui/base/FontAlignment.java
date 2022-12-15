package io.flybird.cubecraft.client.gui.base;

public enum FontAlignment {
    LEFT,
    MIDDLE,
    RIGHT;

    public static FontAlignment from(String alignment) {
        return switch (alignment){
            case "middle"->MIDDLE;
            case "right"->RIGHT;
            default -> LEFT;
        };
    }
}
