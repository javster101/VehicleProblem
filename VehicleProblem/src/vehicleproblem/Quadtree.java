/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem;

import com.opengg.core.Vector2f;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ethachu19
 */
public class Quadtree {

    private final int MAX_OBJECTS = 10;
    public static int MAX_LEVELS = 1;
    private int level;
    private ArrayList<Vector2f> objects;
    private Rectangle bounds;
    private final Quadtree[] nodes;

    /*
     * Constructor
     */
    
    public Quadtree(Rectangle x) {
        level = 0;
        objects = new ArrayList<>();
        bounds = x;
        nodes = new Quadtree[4];
    }
    private Quadtree(int pLevel, Rectangle x) {
        level = pLevel;
        objects = new ArrayList<>();
        bounds = x;
        nodes = new Quadtree[4];
    }

    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }
    
    public int getLevel(Vector2f v) {
        if(objects.contains(v))
            return this.level;
        try {
            return nodes[getIndex(v)].getLevel(v);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("v is not a point");
        }
    }
    
    public ArrayList<Vector2f> getList(Vector2f v) {
        if(objects.contains(v)) {
            return objects;
        }
        try {
            return nodes[getIndex(v)].getList(v);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("v is not a point");
        }
    }
    
    private void split() {
        Quadtree.MAX_LEVELS++;
        int subWidth = (int) (bounds.getWidth() / 2);
        int subHeight = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }
    
    private int getIndex(Vector2f p) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // Object can completely fit within the top quadrants
        boolean topQuadrant = (p.x < horizontalMidpoint && p.y  < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (p.y > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        if (p.x < verticalMidpoint && p.x < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } // Object can completely fit within the right quadrants
        else if (p.x > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }
        return index;
    }

    public void insert(Vector2f p) {
        if (nodes[0] != null) {
            int index = getIndex(p);
            if (index != -1) {
                nodes[index].insert(p);
                return;
            }
        }
        objects.add(p);
        if (objects.size() > MAX_OBJECTS) {
            if (nodes[0] == null) {
                split();
            }
            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    public void add(Vector2f v) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
