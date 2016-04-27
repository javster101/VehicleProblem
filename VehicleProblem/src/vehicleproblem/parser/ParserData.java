/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.parser;

import com.opengg.core.Vector2f;
import java.util.ArrayList;
import vehicleproblem.algorithm.DataPoint;

/**
 *
 * @author 19make
 */
public class ParserData {
    public int cycleNumber;
    public int bartComplex;
    public int lisaComplex; 
    public ArrayList<Vector2f> houses = new ArrayList<>();
    public ArrayList<ArrayList<DataPoint>> groups = new ArrayList<>();
}
