/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vehicleproblem;

import com.opengg.core.Matrix4f;
import com.opengg.core.Vector2f;
import com.opengg.core.render.drawn.Drawable;
import com.opengg.core.util.Time;
import java.util.ArrayList;

/**
 *
 * @author Javier
 */
public class Truck {
    float mult;
    float x = 0,z = 0;
    float nextx, nextz;
    float curx, curz;
    int pathpos = 1;
    ArrayList<Vector2f> path;
    boolean isStopped;
    Time time = new Time();
    Drawable d;
    float timer;
    public Truck(ArrayList<Vector2f> path){
        this.path = path;
        pathpos = 1;
        curx = path.get(0).x;
        curz = path.get(0).y;
        x = curx;
        z = curz;
        nextx = path.get(1).x;
        nextz = path.get(1).y;
    }
    public void update(){
        
        float moved = time.getDeltaMs() * mult;
        timer += moved;
        if(isStopped){
            if(timer >= 3000){
                timer = 0;              
                pathpos += 2;
                next();
                
            }
            return;
        }
        float mov = timer/1000;
        if(nextx < curx || nextx > curx){
            if(nextx < curx){
                mov = -mov;
            }
            x = curx + mov;
        }else if( nextz < curz || nextz > curz){
            if(nextz < curz){
                mov = -mov;
            }
            z = curz + mov;
        }
        if(timer >= 1000){
            timer = 0;
            pathpos++;
            next();
        }
    }
    private void next(){
        if(path.get(pathpos + 1).x == 1000){
            isStopped = true;
            return;
        }
        if(isStopped){
            isStopped = !isStopped;
            curx = path.get(pathpos-2).x;
            curz = path.get(pathpos-2).y;
            x = curx;
            z = curz;
            nextx = path.get(pathpos).x;
            nextz = path.get(pathpos).y;
        }
        curx = path.get(pathpos).x;
        curz = path.get(pathpos).y;
        x = curx;
        z = curz;
        nextx = path.get(pathpos + 1).x;
        nextz = path.get(pathpos + 1).y;
    }
    public void render(){
        Matrix4f fin = Matrix4f.translate(x,10,z);
        d.setMatrix(fin);
        d.draw();
        
    }
    
}
