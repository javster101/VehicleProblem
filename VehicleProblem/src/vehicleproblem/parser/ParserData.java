/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.parser;

import com.opengg.core.Vector2f;
import java.awt.Rectangle;
import java.util.ArrayList;
import vehicleproblem.Quadtree;
import vehicleproblem.global.GlobalInfo;

/**
 *
 * @author 19make
 */
public class ParserData {
    public int cycleNumber;
    public int bartComplex;
    public int lisaComplex; 
    public ArrayList<Vector2f> houses = new ArrayList<>();
    public Quadtree tree = new Quadtree(new Rectangle(GlobalInfo.arrayWidth/2, GlobalInfo.arrayHeight/2, GlobalInfo.arrayWidth, GlobalInfo.arrayHeight));
}
