import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        String irisFileName = args[0];
        int k = Integer.parseInt(args[1]);

        int dimensions = 1;
        boolean dimensionsKnown = false;

        try {

            BufferedReader irisFileReader = new BufferedReader(new FileReader(irisFileName + ".csv"));

            List<Iris> irisList = new ArrayList<>();
            List<Centroid> centroids = new ArrayList<>();

            String line;
            while((line = irisFileReader.readLine()) != null) {

                if(!dimensionsKnown) {

                    dimensions = findDim(line, ',');
                    dimensionsKnown = true;

                }

                String[] lineContents = line.split(",");
                double[] currentIrisParams = new double[dimensions];

                for(int i = 0; i < dimensions; i++)
                    currentIrisParams[i] = Double.parseDouble(lineContents[i]);

                Iris iris = new Iris(lineContents[dimensions], currentIrisParams);
                irisList.add(iris);

            }

            irisFileReader.close();

            //Randomly select k irises as centroids
            for(int i = 1; i <= k; i++) {

               Random random = new Random();
               Iris randomlySelectedIris = irisList.get(random.nextInt(irisList.size()));

               if(randomlySelectedIris.isNotAssignedToCluster()) {
                   System.err.println(i + ": " + randomlySelectedIris.getType());
                   Centroid centroid = new Centroid(i, randomlySelectedIris.getParams());
                   centroids.add(centroid);
                   randomlySelectedIris.setMyCluster(i);
               }

            }

            for(Centroid c : centroids)
                System.out.println(c.getClusterNum());

            for(Iris iris : irisList) {
                if(iris.isNotAssignedToCluster()) {

                    //Get distance to each centroid
                    Map<Integer, Double> distanceMap = new TreeMap<>();
                    for(Centroid centroid : centroids) {

                        double distance = findDistanceToCentroid(iris, centroid);
                        distanceMap.put(centroid.getClusterNum(), distance);

                    }

                    Map.Entry<Integer, Double> minDistanceEntry = null;
                    for (Map.Entry<Integer, Double> entry : distanceMap.entrySet())
                        if (minDistanceEntry == null || minDistanceEntry.getValue() > entry.getValue())
                            minDistanceEntry = entry;

                    iris.setMyCluster(minDistanceEntry.getKey());

                }
            }

            boolean anythingHasChangedSinceLastIteration = true;
            int iterationNum = 1;

            while(anythingHasChangedSinceLastIteration) {

                System.out.println("Iteration number " + iterationNum);
                System.out.println("");
                iterationNum++;
                anythingHasChangedSinceLastIteration = false;

                //Get new centroid values
                for(Centroid centroid : centroids)
                {

                    List<Iris> irisesInCurrentCluster = new ArrayList<>();
                    for(Iris iris : irisList)
                        if(iris.getMyCluster() == centroid.getClusterNum())
                            irisesInCurrentCluster.add(iris);

                    double[] newCords = new double[dimensions];

                    for(int i = 0; i < dimensions; i++) {

                        newCords[i] = 0d;
                        for (Iris iris : irisesInCurrentCluster)
                            newCords[i] += iris.getParams()[i];
                        newCords[i] /= irisesInCurrentCluster.size();

                    }

                    for(int i = 0; i < dimensions; i++)
                        if(newCords[i] != centroid.getParams()[i]) {

                            centroid.setParams(newCords);
                            anythingHasChangedSinceLastIteration = true;

                        }

                }

                //Assign irises to new centroids
                for(Iris iris : irisList) {

                    Map<Integer, Double> distanceMap = new TreeMap<>();
                    for(Centroid centroid : centroids) {

                        double distance = findDistanceToCentroid(iris, centroid);
                        distanceMap.put(centroid.getClusterNum(), distance);

                    }

                    Map.Entry<Integer, Double> minDistanceEntry = null;
                    for (Map.Entry<Integer, Double> entry : distanceMap.entrySet())
                        if (minDistanceEntry == null || minDistanceEntry.getValue() > entry.getValue())
                            minDistanceEntry = entry;

                    iris.setMyCluster(minDistanceEntry.getKey());

                }

                for(Centroid centroid : centroids) {
                    System.out.println("Centroid in cluster " + centroid.getClusterNum() + ", at coordinates " +
                            Arrays.toString(centroid.getParams()));
                }

                //Display information about irises/clusters
                for(Iris iris : irisList) {
                    System.out.println(iris.getType() + ", cluster: " + iris.getMyCluster());
                }

                //Count avg. distance from centroid
                for(Centroid centroid : centroids) {

                    double distSum = 0d;

                    List<Iris> irisesInCurrentCluster = new ArrayList<>();
                    for(Iris iris : irisList)
                        if(iris.getMyCluster() == centroid.getClusterNum())
                            irisesInCurrentCluster.add(iris);


                    for(Iris iris : irisesInCurrentCluster) {

                        distSum += findDistanceToCentroid(iris, centroid);

                    }

                    System.out.println("Sum of distances in cluster number " + centroid.getClusterNum() +
                            " = " + distSum);

                }

                System.out.println("");
                System.out.println("-----------------------------");
                System.out.println("");


            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int findDim(String s, char c) {

        int x = 0;

        for(int i=0; i<s.length(); i++)
            if(s.charAt(i) == c)
                x++;

        return x;

    }

    public static double findDistanceToCentroid(Iris iris, Centroid centroid) {

        double[] irisParams = iris.getParams();
        double[] centroidParams = centroid.getParams();
        int dimensions = irisParams.length;

        double sum = 0;
        for(int i = 0; i < dimensions; i++) {

            sum += Math.pow((irisParams[i] - centroidParams[i]), 2);

        }

        return sum;

    }

}
