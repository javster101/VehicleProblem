/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
            res.houses.add(new Vector2f((Integer.parseInt(sp[0])-1) * 2) + 1, (Integer.parseInt(sp[1]) - 1) * 10 + ());
            data = br.readLine();
        }
        res.bartComplex = Integer.parseInt(br.readLine());
        br.readLine();
        res.lisaComplex = Integer.parseInt(br.readLine());
        return res;
    }
}
