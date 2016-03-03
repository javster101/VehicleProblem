/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vehicleproblem;

import com.opengg.core.io.input.KeyboardEventHandler;
import com.opengg.core.io.input.KeyboardListener;
import com.opengg.core.movement.MovementLoader;
import com.opengg.core.render.VertexArrayObject;
import com.opengg.core.render.VertexBufferObject;
import com.opengg.core.render.shader.ShaderController;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.DisplayMode;
import com.opengg.core.render.window.GLFWWindow;
import static com.opengg.core.render.window.RenderUtil.endFrame;
import static com.opengg.core.render.window.RenderUtil.startFrame;
import com.opengg.core.util.GlobalInfo;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

/**
 *
 * @author Javier
 */
public class VehicleProblem implements KeyboardListener{
    GLFWWindow w;
    VertexBufferObject vbo;
    VertexArrayObject vao;
    Texture ground = new Texture();
    ShaderController s = new ShaderController();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new VehicleProblem();
        
    }
    
    public VehicleProblem(){
        KeyboardEventHandler.addToPool(this);
        try {
            w = new GLFWWindow(1280, 960, "Vehicle Problem!", DisplayMode.WINDOWED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        while (!w.shouldClose()) {

            startFrame();

            update();
            render();
            endFrame(w);
        }
        
        exit();
    }
    
    public void setup() throws UnsupportedEncodingException{
        MovementLoader.setup(w.getID(), 80);

        vao = new VertexArrayObject();
        vao.bind();
        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);
        GlobalInfo.b = vbo;
        
        ground.loadTexture("C:/res/ground.png", true);
        
        URL verts = VehicleProblem.class.getResource("res/shaders/shader.vert");
        URL frags = VehicleProblem.class.getResource("res/shaders/shader.frag");
        URL geoms = VehicleProblem.class.getResource("res/shaders/shader.geom");
        
         s.setup(verts, frags, geoms);
    }
    
    public void render(){
        
    }
    
    public void update(){
        
    }
    
    public void exit(){
        
    }
    
    @Override
    public void keyPressed(int key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(int key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
