/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicleproblem.parser;

import com.opengg.core.Vector2f;

/**
 *
 * @author 19make
 */
public class House {
    public int avenue;
    public int street;
    public String house;
    
    
    
    public House(int avenue, int street, String house) {
        this.avenue = avenue;
        this.street = street;
        this.house = house;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof House))
            return false;
        House h = (House) o;
        return h.avenue == this.avenue && h.street == this.street && (h.house.length() > 1 ? h.house : h.house + h.house).equals((house.length() > 1 ? house : house + house));
            
    }
    
    public Vector2f getWorldPos(){
        return new Vector2f(avenue,street);
    }
        
}
