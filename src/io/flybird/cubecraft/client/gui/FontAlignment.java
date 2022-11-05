package io.flybird.cubecraft.client.gui;

public enum FontAlignment {
    LEFT,
    MIDDLE,
    RIGHT;

    public static FontAlignment from(String alignment) {
        return switch (alignment){
            case "left"->LEFT;
            case "middle"->MIDDLE;
            case "right"->RIGHT;
            default -> LEFT;
        };
    }
}
