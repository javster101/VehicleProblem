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
        while (data.equals("Bart Complex"))
        {
            String[] sp = data.split(",");
            Vector2f v =new Vector2f((Integer.parseInt(sp[0])-1) * 2 + 1, (Integer.parseInt(sp[1]) - 1) * 10 + (sp[2].charAt(0) - 'A') + 1);
            res.houses.add(v);
            res.tree.add(v);
            data = br.readLine();
        }
        res.bartComplex = Integer.parseInt(br.readLine());
        br.readLine();
        res.lisaComplex = Integer.parseInt(br.readLine());
        return res;
    }
    
    public ArrayList<Vector2f>[] getDividedVertices(ArrayList<Vector2f> pts, Quadtree q) {
        ArrayList<Vector2f> done = new ArrayList<>();
        ArrayList<ArrayList<Vector2f>> res = new ArrayList<>();
        for (Vector2f v : pts) {
            if (done.contains(v))
                continue;
            ArrayList<Vector2f> add = q.getList(v);
            res.add(add);
            done.addAll(add);
        }
        return (ArrayList<Vector2f>[]) res.toArray();
    }
}
