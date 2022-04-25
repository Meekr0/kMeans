public class Iris {

    private final String type;
    private final double[] params;

    private int myCluster;

    public Iris(String name, double[] params) {
        this.type = name;
        this.params = params;
        this.myCluster = 0;
    }

    public boolean isNotAssignedToCluster(){
        return this.myCluster == 0;
    }

    public int getMyCluster() {
        return myCluster;
    }

    public void setMyCluster(int myCluster) {
        this.myCluster = myCluster;
    }

    public double[] getParams() {
        return params;
    }

    public String getType() {
        return type;
    }
}