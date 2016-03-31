/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.algorithm;

import java.util.Random;

/**
 *
 * @author Warren
 */
public class SimulatedAnnealing {
    
    private int maxIter;
    private int reheat;
    private Random rand;
    private double annealrate;

    //Initializes the algorithm with the number of iterations, the number of
    //times to restart the algorithm, and the anneal rate (a value between 0 and 1)
    public SimulatedAnnealing(int numTimes, int numReheats, double annealRate)
    {
       maxIter = numTimes;
       reheat = numReheats;
       annealrate = annealRate;
       rand = new Random();
    }

    //returns a tour of the shortest path
    public Tour findShortestPath(Graph g)
    {

      Tour start = g.getRandomTour();
      Tour best = start;
      Tour comparison;
      int counter;
      for(int k = 0; k < reheat; k++)
      {
          start = best;
          for(int i = 0; i < maxIter; i++)
          {
              comparison = getNeighborTour(start, g);
              double a = comparison.getLength();
              double b = start.getLength();
              if(a < b || rand.nextDouble() < anneal(i,a,b))
              {
                  start = comparison;
              }
              if(start.getLength() < best.getLength())
              {
                  best = start;
                  //System.out.printf("Iteration %d\n New best: %.2f\n", i, best.getLength());
              }
          }
      }
        return best;
    }

    //returns a neighboring tour of a given tour
    Tour getNeighborTour(Tour t, Graph g)
    {
        Vertex[] vertices = t.verticesSoFar();
        return new Tour(neighborVertexSet(vertices), g);
    }

    //returns a reordering of the vertices with two vertices swapped
    Vertex[] neighborVertexSet(Vertex[] vertices)
    {
        int len = vertices.length;
        Vertex[] newv = new Vertex[len];
        int random1 = rand.nextInt(len);
        int random2 = rand.nextInt(len);
        //System.out.printf("%d, %d, %d\n", len, random1, random2);
        while(random1 == random2)
            random2 = rand.nextInt(len);

        if(random2 < random1)
        {
            int temp = random2;
            random2 = random1;
            random1 = temp;
        }

        for(int i = 0; i < len; i++)
        {
            if(i == random1)
            {
                newv[i] = vertices[random2];
            }
            else if(i == random2)
            {
                newv[i] = vertices[random1];
            }
            else if(i > random1 && i < random2)
            {
                newv[i] = vertices[random1+random2 - i];
            }
            else
            {
                newv[i] = vertices[i];
            }
        }

        return newv;
    }

    //returns the probability of accepting a worse tour given
    //the number of iterations done so far and the weights of the
    //two tours
    double anneal(int iter, double a, double b)
    {
        double t = Math.pow(10,10)*Math.pow(annealrate,iter/100);
        return Math.exp(-(a-b)/t);
    }


}
