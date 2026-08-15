package rendering;

public class RayHit {
    private double distance;
    private boolean isVerticalSide;
    private int wallType;
    private double wallX;

    public RayHit(double distance, boolean isVerticalSide, int wallType, double wallX){
        this.distance = distance;
        this.isVerticalSide = isVerticalSide;
        this.wallType = wallType;
        this.wallX = wallX; //where along the wall did the ray hit (for shading)
    }

    public double getDistance() {
        return distance;
    }

    public boolean isVerticalSide() {
        return isVerticalSide;
    }

    public int getWallType() {
        return wallType;
    }

    public double getWallX() {
        return wallX;
    }
}
