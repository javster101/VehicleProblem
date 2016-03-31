/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.algorithm;

import com.opengg.core.Vector2f;
import static com.opengg.core.util.GlobalUtil.print;
import static com.opengg.core.util.GlobalUtil.print;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author Warren
 */
//import java.util.*;
public class AStar {

    public static final int DIAGONAL_COST = 100;
    public static final int V_H_COST = 10;

    static class Cell {

        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent;

        Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "[" + this.i + ", " + this.j + "]";
        }
    }

    //Blocked cells are just null Cell values in grid
    static Cell[][] grid = new Cell[5][5];

    static PriorityQueue<Cell> open;

    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;

    public static void setBlocked(int i, int j) {
        
        grid[i][j] = null;
        
    }

    public static void setStartCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    public static void setEndCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    static void checkAndUpdateCost(Cell current, Cell t, int cost) {
        if (t == null || closed[t.i][t.j]) {
            return;
        }
        int t_final_cost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || t_final_cost < t.finalCost) {
            t.finalCost = t_final_cost;
            t.parent = current;
            if (!inOpen) {
                open.add(t);
            }
        }
    }

    public static void AStar() {

        //add the start location to open list.
        open.add(grid[startI][startJ]);

        Cell current;

        while (true) {
            current = open.poll();
            if (current == null) {
                break;
            }
            closed[current.i][current.j] = true;

            if (current.equals(grid[endI]
                    [endJ])) {
                return;
            }

            Cell t;
            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i - 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.i + 1 < grid.length) {
                t = grid[current.i + 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i + 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
        }
    }

    public static List test(int tCase, int x, int y, int si, int sj, int ei, int ej, int[][] blocked) {
        System.out.println("\n\nTest Case #" + tCase);
        //Reset
        grid = new Cell[x][y];
        closed = new boolean[x][y];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
            Cell c1 = (Cell) o1;
            Cell c2 = (Cell) o2;

            return c1.finalCost < c2.finalCost ? -1
                    : c1.finalCost > c2.finalCost ? 1 : 0;
        });
        //Set start position
        setStartCell(si, sj);  //Setting to 0,0 by default. Will be useful for the UI part

        //Set End Location
        setEndCell(ei, ej);

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);

            }

        }
        grid[si]
                [sj]
                .finalCost = 0;
        for (int i = 0; i < blocked.length; ++i) {
            setBlocked(blocked[i][0], blocked[i][1]);
        }


        AStar();

        List<Vector2f> s = new LinkedList<>();
        if (closed[endI][endJ]) {
            //Trace back the path 
            //System.out.println("Path: ");
            Cell current = grid[endI][endJ];
            //System.out.print(current);
            while (current.parent != null) {
                s.add(new Vector2f(current.j, current.i));
                current = current.parent;
            }
            //System.out.println();
        } else {
            //System.out.println("No possible path");
        }
        return s;
    }

    public static void main(String[] args) throws Exception {
        EuclideanVertex[] vertices;

        ArrayList<EuclideanVertex> vlist = new ArrayList<>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(AStar.class.getResource("text.txt").getPath()));
            String sCurrentLine;
            String[] xy;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
               
                xy = sCurrentLine.trim().split("\\s+");
                int[] coords = {Integer.parseInt(xy[1]), Integer.parseInt(xy[2])};
                vlist.add(new EuclideanVertex(i++, coords));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        vertices = new EuclideanVertex[vlist.size()];
        for (int i = 0; i < vlist.size(); i++) {
            vertices[i] = vlist.get(i);
        }
        
        Graph g = new Graph(vertices);
        
        SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 1, 0.995);
        Tour t = sim.findShortestPath(g);
        EuclideanVertex[] graphvertices = g.getVertices();
        ArrayList<EuclideanVertex> temp = new ArrayList<>();
        for(Vertex v:t.verticesSoFar()){
            int currentVertexId = v.getId();
            EuclideanVertex vs = graphvertices[currentVertexId];
            
            temp.add(vs);
        }
        
        int width = (((50 - 1) * 11) + 1);
        int height = ((250 - 1) * 3) + 1;
        ArrayList<int[]> barriers = new ArrayList<>();
        for(int i = 0;i<height;i++){
            
            for(int x = 0; x< width;x++){
                if(x%3 != 0){
                    if(i%11 != 0){
                        barriers.add(new int[]{x,i});
                    }
                }
            }
            }
            
        
        int[][] bar = new int[barriers.size()][2];
        
        for (int i = 0; i < bar.length; i++) {
            bar[i] = barriers.get(i);
        }
        for(int i = 0;i<temp.size();i++){
            print(i);
            EuclideanVertex badname = temp.get(i);
            print(badname.toString());
            EuclideanVertex jesus;
            if(i == (temp.size()-1)){
                jesus = temp.get(0);
            }else{
               jesus = temp.get(i+1);
            }
            print(jesus.toString());
             
            
            test(i, width, height, badname.getCoord(0), badname.getCoord(1), jesus.getCoord(0), jesus.getCoord(1), bar);
        }
       
        
    }
}

