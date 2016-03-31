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
public class Vertex {
     private int id;

    public Vertex()
    {
        id = -1;
    }
    public Vertex(int i)
    {
        id = i;
    }

    public int getId() {
        return id;
    }

    public void setId(int i)
    {
        id = i;
    }

    public double dist(Vertex v2)
    {
      return 0;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }


}
