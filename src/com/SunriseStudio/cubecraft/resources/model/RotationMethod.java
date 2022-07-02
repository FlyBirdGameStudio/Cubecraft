package com.SunriseStudio.cubecraft.resources.model;

public enum RotationMethod {
    GROUPED,
    FOLLOW,
    ABSOLUTE;

    /**
     * get rotation enum from id. Return "ABSOLUTE" if doesn`t match any.
     * @param name name
     * @return enum
     */
    public static RotationMethod fromName(String name){
        return switch (name) {
            case "follow" -> FOLLOW;
            case "grouped" -> GROUPED;
            default -> ABSOLUTE;
        };
    }
}
