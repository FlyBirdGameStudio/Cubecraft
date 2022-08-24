package com.sunrisestudio.cubecraft.world.biome;

public class Biome {
    private final double continental, temperature,humidity,erosion,altitude;
    private final String id;
    private final String[] surfaceBlock;
    private final String basicBlock;

    public Biome(
            double continental, double temperature, double humidity, double erosion, double altitude,
            String id,
            String basicBlock,
            String[] surfaceBlock
    ) {
        this.continental = continental;
        this.temperature = temperature;
        this.humidity = humidity;
        this.erosion = erosion;
        this.altitude = altitude;
        this.id = id;
        this.basicBlock=basicBlock;
        this.surfaceBlock=surfaceBlock;
    }

    public double match(double continental, double temperature, double humidity, double erosion, double altitude){
        double d=Math.abs(this.continental - continental);
        double d2=Math.abs(this.temperature - temperature);
        double d3=Math.abs(this.humidity - humidity);
        double d4=Math.abs(this.erosion - erosion);
        double d5=Math.abs(this.altitude - altitude);

        return d+d2+d3+d4+d5;
    }

    public String getId() {
        return id;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getContinental() {
        return continental;
    }

    public double getErosion() {
        return erosion;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getBasicBlock() {
        return basicBlock;
    }

    public String[] getSurfaceBlock() {
        return surfaceBlock;
    }
}
