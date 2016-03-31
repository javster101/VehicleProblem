/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *
 * @author Warren
 */
public class Tour {
    private double length;
    private Edge[] touredges;
    private Vertex[] vertices;
    // position of the current vertex of the tour
    private int currpos;
    private int num;

    //initializes a tour with a set of edges (doesn't need to be complete)
    public Tour (Edge[] edges) {
        length = 0;
        touredges = edges;
        num = edges.length;
        // figure out which is the source vertex
        Edge first_edge = edges[0];
        Edge second_edge = edges[1];
        Vertex[] arr = first_edge.getVArray();
        int source;

        int v1 = arr[0].getId();
        int v2 = arr[1].getId();

        if (second_edge.getVertices().contains(v1)) {
            source = v1;
        }
        else {
            source = v2;
        }

        LinkedList<Integer> vs = new LinkedList<>();

        vs.add(source);
        int curr_vertex = source;

        // create a list of vertices in the order in which they are visited
        for (Edge e : edges) {
            // get the next vertex (of the edge) that is not the current vertex
            Vertex[] v_arr = e.getVArray();
            int v_1 = v_arr[0].getId();
            int v_2 = v_arr[1].getId();
            length+=e.getWeight();
            if (curr_vertex == v_1) {
                vs.add(v_2);
                curr_vertex = v_2;
            }
            else {
                vs.add(v_1);
                curr_vertex = v_1;
            }
        }
        vertices = new Vertex[num];
        for(int i = 0; i < num; i++)
        {
            vertices[i] = new Vertex(vs.get(i));
        }
        currpos = num;



    }
    public Tour(){
        
    }
    //initializes a tour with the vertices (in order) of a tour
    public Tour(Vertex[] allVertices, Graph g)
    {
        num = g.numVertices();
        length = 0;
        touredges = new Edge[num];
        vertices = allVertices;

        for(int i = 0; i < num; i++)
        {
          touredges[i] = g.edgeBetween(vertices[(i%num)],vertices[(i+1)%num]);
          length+=touredges[i].getWeight();
        }

        currpos = num;
    }

    //initializes a tour with a starting vertex and total number of vertices
    public Tour (Vertex v, int numVertices)
    {
        this.length = 0;
        num = numVertices;
        touredges = new Edge[num];
        vertices = new Vertex[num];
        vertices[0] = v;
        currpos = 0;
    }

    //returns true if the tour contains a certain vertex
    boolean containsVertex(Vertex v)
    {
        int x = v.getId();
        for(int i = 0; i <= currpos; i++)
        {
            if (x == vertices[i].getId())
            {
                return true;
            }
        }
        return false;
    }

    //adds an edge to the tour, returning false if unsuccessful
    boolean addEdge(Edge e)
    {
        if( (e.getFirstVertex().getId() != vertices[currpos].getId()
                || containsVertex(e.getSecondVertex()))
                && currpos != num - 1)
        {
            return false;
        }
        else
        {
            touredges[currpos] = e;
            if(currpos < num - 1)
            {
                vertices[currpos+1] = e.getSecondVertex();
            }
            currpos++;
            length+=e.getWeight();
            return true;
        }
    }

    //returns the last vertex that is part of the tour so far
    Vertex getCurrentVertex()
    {
        return vertices[currpos];
    }

    // returns an array of length (number of vertices in graph)
    Edge[] allEdges()
    {
        return touredges;
    }

    // returns a vertex list of length n where n is number of valid vertices in tour so far
    public Vertex[] verticesSoFar()
    {
        Vertex[] soFar = new Vertex[currpos];
         System.arraycopy(vertices, 0, soFar, 0, currpos);
        return soFar;
    }

    //returns a double containing the length of the tour
    double getLength() {
        return length;
    }

    //creates a string representing the tour
     @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < currpos; i++)
        {
            builder.append(touredges[i].toString()).append("\n");
        }

        return builder.toString();
    }

    //prints the tour to a given file
    void printTourToFile(String file)
    {
        try
        {
            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
                out.print(toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
