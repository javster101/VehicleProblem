/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author Warren
 */
public class Graph {
     private int numVertices;
    private double[][] adjMat;
    private Random rand;
    private EuclideanVertex[] vertices;

    // each vertex must have a distinct ID between 0, numVertices - 1
    public Graph(EuclideanVertex[] vertices)
    {
        rand = new Random();
        this.vertices = vertices;
        numVertices = vertices.length;
        adjMat = new double [numVertices][numVertices];
        for(int i = 0; i < numVertices; i++)
        {
            for(int j = 0; j <= i; j++ )
            {
                double val = vertices[i].dist(vertices[j]);
                adjMat[i][j] = val;
                adjMat[j][i] = val;
            }
        }
    }

    // generates a graph given an adjacency matrix
    public Graph(double[][] matrix) {
        rand = new Random();
        adjMat = matrix;
        numVertices = adjMat.length;
    }

    // generates a graph of a tree given a list of edges
    // we give the edge weight between two vertices infinity if there is no edge
    public Graph(Edge[] edges) {
        rand = new Random();
        numVertices = edges.length + 1;
        adjMat = new double [numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                adjMat[i][j] = -1;
            }
        }
        for (Edge e : edges) {
            int v1 = e.getFirstVertex().getId();
            int v2 = e.getSecondVertex().getId();
            double weight = e.getWeight();
            adjMat[v1][v2] = weight;
            adjMat[v2][v1] = weight;
        }
    }

    public double[][] getAdjMat() {
        return adjMat;
    }

    // returns the degree of a vertex with ID id
    public int getDegree(int id) {
        int degree = 0;
        for (int i = 0; i < adjMat.length; i++) {
            if (adjMat[id][i] > 0) {
                degree++;
            }
        }
        return degree;
    }

    //returns an array of edges connected to a given vertex
    public Edge[] edgesOf(Vertex v)
    {
      int x = v.getId();
      Edge[] edgeList = new Edge[numVertices-1];
      int counter = 0;
      for(int i = 0; i < numVertices; i++)
      {
        if(i != x)
        {
            Vertex v2 = new Vertex(i);
            Edge e = new Edge(v,v2,v.dist(v2));
            edgeList[counter] = e;
            counter++;
        }
      }
      return edgeList;
    }

    //returns the edge between two given vertices
    public Edge edgeBetween(Vertex v1, Vertex v2)
    {
        return new Edge(v1,v2,adjMat[v1.getId()][v2.getId()]);
    }

    //returns a random edge connected to a given vertex
    public Edge getRandomNeighborEdge(Vertex v)
    {
        int x = v.getId();
        int randomNum = rand.nextInt(numVertices);
        while(randomNum == x)
        {
            randomNum = rand.nextInt();
        }

        return new Edge(v, new Vertex(randomNum), adjMat[x][randomNum]);

    }

    //returns a random vertex in the graph
    public Vertex getRandomVertex()
    {
        int randomNum = rand.nextInt(numVertices);

        return new Vertex(randomNum);
    }

    //returns true is a vertex is not a member of the parameter vertex array
    public boolean isNotMemberOf(Vertex v, Vertex[] all)
    {
        int x = v.getId();
        for(int i = 0; i < all.length; i++)
        {
          if(x == all[i].getId())
              return false;
        }
        return true;
    }

    //returns the shortest edge connected to a given vertex
    //that does not contain a vertex in the inputted vertex array
    public Edge getShortestNeighborEdge (Vertex v, Vertex[] already)
    {
        int x = v.getId();
        double minweight = -1;
        int minvertex = -1;
        for(int i = 0; i < numVertices; i++)
        {
            if (x != i && isNotMemberOf(new Vertex(i),already))
            {
              if(minvertex == -1 || minweight > adjMat[x][i])
              {
                minvertex = i;
                minweight = adjMat[x][i];
              }
            }
        }

        return new Edge(v, new Vertex(minvertex), minweight);
    }

    //returns a random tour in the graph
    public Tour getRandomTour()
    {
        Vertex v = getRandomVertex();
        Tour t = new Tour(v, numVertices);
        int randomNum = rand.nextInt(numVertices);
        int tourlength = 0;
        while(tourlength < numVertices - 1)
        {
            Vertex curr = t.getCurrentVertex();
            if(t.addEdge(new Edge(curr,new Vertex(randomNum), adjMat[curr.getId()][randomNum])))
            {
                tourlength++;
            }
            randomNum = rand.nextInt(numVertices);
        }

        t.addEdge(new Edge(t.getCurrentVertex(),v,adjMat[t.getCurrentVertex().getId()][v.getId()]));

        return t;
    }

    public int numVertices()
    {
        return numVertices;
    }

    //returns an array of the vertices that make up the graph
    public EuclideanVertex[] getVertices() {
        return vertices;
    }

    //returns a double array with the mins of the coordinates
    public double[] getMins()
    {
        double[] mins = new double[vertices[0].getDim()];
        for(int i = 0; i < mins.length; i++)
        {
            double min = vertices[0].getCoord(i);
            for(int j = 1; j < numVertices; j++)
            {
                double currcoord = vertices[j].getCoord(i);
                if(currcoord < min)
                    min = currcoord;
            }
            mins[i] = min;
        }

        return mins;
    }

    //returns the maxes of each of the coordinates
    public double[] getMaxes()
    {
        double[] maxes = new double[vertices[0].getDim()];
        for(int i = 0; i < maxes.length; i++)
        {
            double max = vertices[0].getCoord(i);
            for(int j = 1; j < numVertices; j++)
            {
                double currcoord = vertices[j].getCoord(i);
                if(currcoord > max)
                    max = currcoord;
            }
            maxes[i] = max;
        }

        return maxes;
    }

    //prints the graph to a given file
    public void printGraphToFile(String file)
    {
        try
        {
            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
                out.print(display());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns a stringified version of the graph
    public String display()
    {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < numVertices; i++)
        {
            for(int j = 0; j < numVertices; j++)
            {
                builder.append(adjMat[i][j]).append(" ");
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}
