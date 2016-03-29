/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.algorithm;

/**
 *
 * @author Warren
 */
class EuclideanVertex extends Vertex{
     private int[] coords;
    private int dim;

    public EuclideanVertex (int i, int[] coordinates) {
        coords = coordinates;
        dim = coords.length;
        this.setId(i);
    }

    public int getCoord(int i) {
            return coords[i];
    }

    public int getDim()
    {
        return dim;
    }

    public double dist(EuclideanVertex v) {
        double total = 0;
        for(int i = 0; i < dim; i++)
        {
            total+=Math.pow(coords[i] - v.getCoord(i),2);
        }
        return Math.sqrt(total);
    }
     @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(getId()).append(" ");
            for(int i = 0; i < dim; i++)
            {
                builder.append(coords[i]).append(" ");
            }
            return builder.toString();
        }

}
