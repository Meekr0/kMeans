public class Centroid {

    private final int clusterNum;
    private double[] params;

    public Centroid(int clusterNum, double[] params) {
        this.clusterNum = clusterNum;
        this.params = params;
    }

    public int getClusterNum() {
        return clusterNum;
    }

    public double[] getParams() {
        return params;
    }

    public void setParams(double[] params) {
        this.params = params;
    }

}
