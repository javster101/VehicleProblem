/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.parser;

import com.opengg.core.Vector2f;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import vehicleproblem.Quadtree;
import vehicleproblem.algorithm.DataPoint;
import vehicleproblem.algorithm.KMeans;

/**
 *
 * @author 19make
 */
public class Parser {
    
    public static ParserData parseFile(String fileName) throws FileNotFoundException, IOException {
        ParserData res = new ParserData();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        res.cycleNumber = Integer.parseInt(br.readLine());
        br.readLine();
        String data = br.readLine();
        ArrayList<DataPoint> templist = new ArrayList<>();
        int i = 0;
        while (!data.equals("Bart Complex"))
        {
            String[] sp = data.split(",");
            Vector2f v =new Vector2f((Integer.parseInt(sp[0].replace("s", ""))-1) * 3, ((Integer.parseInt(sp[1].replace("a", "")) - 1) * 11) + (sp[2].charAt(0) - 'A'));           
            res.houses.add(v);
            templist.add(DataPoint.toDataPoint(v, String.valueOf(i)));
            data = br.readLine();
            
        }
        System.out.println("Testing " + (templist.size()+ 30)  + " houses.");
        res.bartComplex = Integer.parseInt(br.readLine());
        br.readLine();
        res.lisaComplex = Integer.parseInt(br.readLine());
        
        KMeans garbinst = new KMeans(1, 1, templist);
        if(templist.size()  < 500){
            garbinst = new KMeans(1, 30000, templist);
        }
        if(templist.size()  > 3000 && templist.size() < 4500){
            garbinst = new KMeans(4, 30000, templist);
        }
        if(templist.size()  <= 3000 && templist.size() >= 500){
            garbinst = new KMeans(3, 300000, templist);
        }
        if(templist.size() >= 4500){
            garbinst = new KMeans(5, 30000, templist);
        }
        
        
        garbinst.startAnalysis();
        res.groups = garbinst.getClusterOutput();
        System.out.println("Completed clustering");
        return res;
    }
    
    public static ArrayList<ArrayList<Vector2f>> getDividedVertices(ArrayList<Vector2f> pts, Quadtree q) {
        ArrayList<Vector2f> done = new ArrayList<>();
        ArrayList<ArrayList<Vector2f>> res = new ArrayList<>();
        for (Vector2f v : pts) {
            if (done.contains(v))
                continue;
            ArrayList<Vector2f> add = q.getList(v);
            res.add(add);
            done.addAll(add);
        }
        return res;
    }
}
