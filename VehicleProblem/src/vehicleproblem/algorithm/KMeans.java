/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.algorithm;

import java.util.ArrayList;

/**
 *
 * @author Warren
 */
public class KMeans {
        private Cluster[] clusters;
    private int miter;
    private ArrayList<DataPoint> mDataPoints = new ArrayList<>();
    private double mSWCSS;

    public KMeans(int k, int iter, ArrayList<DataPoint> dataPoints) {
        clusters = new Cluster[k];
        for (int i = 0; i < k; i++) {
            clusters[i] = new Cluster("Cluster" + i);
        }
        this.miter = iter;
        this.mDataPoints = dataPoints;
    }

    private void calcSWCSS() {
        double temp = 0;
        for (Cluster cluster : clusters) {
            temp = temp + cluster.getSumSqr();
        }
        mSWCSS = temp;
    }

    public void startAnalysis() {
        //set Starting centroid positions - Start of Step 1
        setInitialCentroids();
        int n = 0;
        //assign DataPoint to clusters
        loop1: while (true) {
            for (Cluster cluster : clusters) {
                cluster.addDataPoint((DataPoint)mDataPoints.get(n));
                n++;
                if (n >= mDataPoints.size())
                    break loop1;
            }
        }
        
        //calculate E for all the clusters
        calcSWCSS();
        
        //recalculate Cluster centroids - Start of Step 2
        for (Cluster cluster : clusters) {
            cluster.getCentroid().calcCentroid();
        }
        
        //recalculate E for all the clusters
        calcSWCSS();

        for (int i = 0; i < miter; i++) {
            //enter the loop for cluster 1
            for (Cluster cluster1 : clusters) {
                for (int k = 0; k < cluster1.getNumDataPoints(); k++) {
                    //pick the first element of the first cluster
                    //get the current Euclidean distance
                    double tempEuDt = cluster1.getDataPoint(k).getCurrentEuDt();
                    Cluster tempCluster = null;
                    boolean matchFoundFlag = false;
                    //call testEuclidean distance for all clusters
                    for (int l = 0; l < clusters.length; l++) {
                        //if testEuclidean < currentEuclidean then
                        if (tempEuDt > cluster1.getDataPoint(k).testEuclideanDistance(clusters[l].getCentroid())) {
                            tempEuDt = cluster1.getDataPoint(k).testEuclideanDistance(clusters[l].getCentroid());
                            tempCluster = clusters[l];
                            matchFoundFlag = true;
                        }
                        //if statement - Check whether the Last EuDt is > Present EuDt
                    }
//for variable 'l' - Looping between different Clusters for matching a Data Point.
//add DataPoint to the cluster and calcSWCSS
                    if (matchFoundFlag) {
                        tempCluster.addDataPoint(cluster1.getDataPoint(k));
                        cluster1.removeDataPoint(cluster1.getDataPoint(k));
                        for (Cluster cluster : clusters) {
                            cluster.getCentroid().calcCentroid();
                        }
                        //for variable 'm' - Recalculating centroids for all Clusters

                        calcSWCSS();
                    }
//if statement - A Data Point is eligible for transfer between Clusters.
                }
                //for variable 'k' - Looping through all Data Points of the current Cluster.
            } //for variable 'j' - Looping through all the Clusters.
        }//for variable 'i' - Number of iterations.
    }

    public ArrayList<ArrayList<DataPoint>> getClusterOutput() {
        ArrayList<ArrayList<DataPoint>> v = new ArrayList<>();
        for (int i = 0; i < clusters.length; i++) {
            v.add(clusters[i].getDataPoints());
        }
        return v;
    }


    private void setInitialCentroids() {
        //kn = (round((max-min)/k)*n)+min where n is from 0 to (k-1).
        double cx = 0, cy = 0;
        for (int n = 1; n <= clusters.length; n++) {
            cx = (((getMaxXValue() - getMinXValue()) / (clusters.length + 1)) * n) + getMinXValue();
            cy = (((getMaxYValue() - getMinYValue()) / (clusters.length + 1)) * n) + getMinYValue();
            Centroid c1 = new Centroid(cx, cy);
            clusters[n - 1].setCentroid(c1);
            c1.setCluster(clusters[n - 1]);
        }
    }

    private double getMaxXValue() {
        double temp;
        temp = ((DataPoint) mDataPoints.get(0)).getX();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.get(i);
            temp = (dp.getX() > temp) ? dp.getX() : temp;
        }
        return temp;
    }

    private double getMinXValue() {
        double temp = 0;
        temp = ((DataPoint) mDataPoints.get(0)).getX();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.get(i);
            temp = (dp.getX() < temp) ? dp.getX() : temp;
        }
        return temp;
    }

    private double getMaxYValue() {
        double temp = 0;
        temp = ((DataPoint) mDataPoints.get(0)).getY();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.get(i);
            temp = (dp.getY() > temp) ? dp.getY() : temp;
        }
        return temp;
    }

    private double getMinYValue() {
        double temp = 0;
        temp = ((DataPoint) mDataPoints.get(0)).getY();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.get(i);
            temp = (dp.getY() < temp) ? dp.getY() : temp;
        }
        return temp;
    }

    public int getKValue() {
        return clusters.length;
    }

    public int getIterations() {
        return miter;
    }

    public int getTotalDataPoints() {
        return mDataPoints.size();
    }

    public double getSWCSS() {
        return mSWCSS;
    }

    public Cluster getCluster(int pos) {
        return clusters[pos];
    }
     public static void main (String args[]){
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        dataPoints.add(new DataPoint(22,21,"Javier"));
        dataPoints.add(new DataPoint(19,20,"a"));
        dataPoints.add(new DataPoint(18,22,"is"));
        dataPoints.add(new DataPoint(1,3,"confirmed"));
        dataPoints.add(new DataPoint(3,2,"scrub"));

        KMeans garbageinstance = new KMeans(2,1000,dataPoints);
        garbageinstance.startAnalysis();

       ArrayList<ArrayList<DataPoint>> v = garbageinstance.getClusterOutput();
        for (int i=0; i<v.size(); i++){
            ArrayList<DataPoint> tempV = v.get(i);
            System.out.println("-----------Cluster "+i+"---------");
            for (DataPoint dpTemp : tempV) {
                System.out.println(dpTemp.getObjName()+"("+dpTemp.getX()+","+dpTemp.getY()+")");
            }
        }
    }
}
