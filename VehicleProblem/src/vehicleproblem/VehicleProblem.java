/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vehicleproblem;

import com.opengg.core.Matrix4f;
import com.opengg.core.Vector2f;
import com.opengg.core.Vector3f;
import com.opengg.core.io.input.KeyboardEventHandler;
import com.opengg.core.io.input.KeyboardListener;
import com.opengg.core.io.objloader.parser.OBJParser;
import com.opengg.core.movement.MovementLoader;
import com.opengg.core.render.VertexArrayObject;
import com.opengg.core.render.VertexBufferObject;
import com.opengg.core.render.buffer.ObjectBuffers;
import com.opengg.core.render.drawn.DrawnObject;
import com.opengg.core.render.drawn.DrawnObjectGroup;
import static com.opengg.core.render.gl.GLOptions.enable;
import com.opengg.core.render.shader.Mode;
import com.opengg.core.render.shader.ShaderController;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.DisplayMode;
import com.opengg.core.render.window.GLFWWindow;
import static com.opengg.core.render.window.RenderUtil.endFrame;
import static com.opengg.core.render.window.RenderUtil.startFrame;
import com.opengg.core.util.GlobalInfo;
import static com.opengg.core.util.GlobalUtil.print;
import com.opengg.core.world.Camera;
import com.opengg.core.world.Terrain;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;
import static vehicleproblem.algorithm.AStar.test;
import vehicleproblem.algorithm.EuclideanVertex;
import vehicleproblem.algorithm.Graph;
import vehicleproblem.algorithm.SimulatedAnnealing;
import vehicleproblem.algorithm.Tour;
import vehicleproblem.algorithm.Vertex;
import vehicleproblem.parser.Parser;
import vehicleproblem.parser.ParserData;

/**
 *
 * @author Javier
 */
public class VehicleProblem implements KeyboardListener{
    DrawnObjectGroup house;
    GLFWWindow w;
    VertexBufferObject vbo;
    VertexArrayObject vao;
    Texture ground = new Texture();
    ShaderController s = new ShaderController();
    DrawnObject groundobj, renderpage, testawp;
    Vector3f pos = new Vector3f(),rot = new Vector3f();
    float rot1, rot2;
    float xrot, yrot;
    Matrix4f basehome;
    int lodbias = 0;
    Texture renderfinal;
    int mode1, mode2;
    int rdistx = 1000/20;
    int rdisty = 1000/100;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new VehicleProblem();
        
    }
    private Camera c;
    
    
    public VehicleProblem(){
        KeyboardEventHandler.addToPool(this);
        try {
            w = new GLFWWindow(1280, 960, "Vehicle Problem", DisplayMode.WINDOWED);
            setup();
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
    
    public void setup() throws Exception{
        genPath();
        MovementLoader.setup(w.getID(), 20);

        vao = new VertexArrayObject();
        vao.bind();
        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);
        GlobalInfo.b = vbo;
        renderfinal = new Texture();
        renderfinal.setupTexToBuffer(w.getWidth() ,  w.getHeight());
        ground.loadTexture("C:/res/newthing.png", true);
        ground.setMinFilter(GL_LINEAR_MIPMAP_LINEAR);
        URL verts = VehicleProblem.class.getResource("res/shaders/shader.vert");
        URL frags = VehicleProblem.class.getResource("res/shaders/shader.frag");
        URL geoms = VehicleProblem.class.getResource("res/shaders/shader.geom");
        
        s.setup(verts, frags, geoms);
        InputStream heightmap = VehicleProblem.class.getResource("res/heightmap.jpg").openStream();
        Terrain groundmod = new Terrain(0, 0, ground);
        
        groundmod.generateTerrain(heightmap);
        groundobj = new DrawnObject(groundmod.elementals, vbo,groundmod.indices);
        groundobj.setMatrix(Matrix4f.scale(1000, 1, 1000));
        
        renderpage = new DrawnObject(ObjectBuffers.getSquareUI(-1, 1, -1, 1, -0.6f, 0, false), 12);
        renderpage.setMatrix(new Matrix4f());
        
        house = new DrawnObjectGroup(VehicleProblem.class.getResource("res/obj/finalhouse.obj"),1f);
        testawp = new DrawnObject(ObjectBuffers.genBuffer(
                new OBJParser().parse(VehicleProblem.class.getResource("res/obj/awp3.obj"))
                , 1f,0.1f, new Vector3f()),12);

        groundmod.removeBuffer();
        
        enable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        enable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        
        glCullFace(GL_BACK);
        enable(GL_TEXTURE_2D);

        enable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
        
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
        
        GlobalInfo.main = s;
        
        s.setLightPos(new Vector3f(10, 30, 10));
        
        c = new Camera();
        
        c.setPos(pos);
        c.setRot(rot);
        
        s.setView(c);
        
        s.setUVMultX(50);
        s.setUVMultY(250);
        basehome = Matrix4f.translate(2.75f,0,1.6f).multiply(Matrix4f.scale(0.4f, 0.4f, 0.4f)).multiply(Matrix4f.rotate(90, 0, 1, 0));
    }
    
    
    public void render(){
        renderfinal.startTexRender();
        s.setPerspective(90, w.getRatio(), 0.1f, 1500f); 
        s.setUVMultX(50);
        s.setUVMultY(250);
        house.setMatrix(basehome);
        enable(GL_CULL_FACE);
        rot = new Vector3f(-yrot, -xrot, 0);
        pos = MovementLoader.processMovement(pos, rot);
        c.setPos(pos);
        c.setRot(rot);
        s.setView(c);
        s.setMode(Mode.GUI);
        ground.useTexture(0);
        groundobj.draw();
        s.setUVMultX(1);
        s.setUVMultY(1);
        s.setMode(Mode.OBJECT_NO_SHADOW);
        //testawp.draw();
        //s.setMode(Mode.GUI);
        drawLocalHomes(new Vector2f(-pos.z/20, -pos.x/4), 40);
        renderfinal.endTexRender();
        renderfinal.useTexture(0);
        renderfinal.useDepthTexture(1);
        s.setOrtho(-1, 1, -1, 1, -1, 1);
        s.setView(new Matrix4f());
        s.setMode(Mode.PP);
        glDisable(GL_CULL_FACE);
        renderpage.draw();
    }
    
    public void drawLocalHomes(Vector2f pos, int concentration){
        for(int i = (int)(pos.x - concentration * 2 /rdistx); i < (int)(pos.x + concentration * 2.5f /rdistx); i++){
            for(int j = (int)(pos.y - concentration* 0.8f /rdisty); j < (int)(pos.y + concentration * 1.2 /rdisty); j++){
                for(int k = 0; k < 10; k++){
                    house.setMatrix(basehome.multiply(Matrix4f.translate(-i *rdistx - (4.75f * (k)), 0, j * rdisty)));
                    house.drawShaded();
                    house.setMatrix(basehome.multiply(Matrix4f.translate(-i *rdistx - (4.75f * (k * 1f)) , 0, j * rdisty - 4.5f)));
                    house.drawShaded();
                }
                
            }
        }
        
    }
    
    public void update(){
        xrot += rot1 * 5;
        yrot += rot2 * 5;
    }
    
    public void exit(){
        vao.delete();
        vbo.delete();
    }
    
    public void genPath(){
        
        
        EuclideanVertex[] vertices;
        ParserData data = new ParserData();
        try {
             data = Parser.parseFile("C:\\minicycle.txt");
        } catch (IOException ex) {
            
        }
        
        ArrayList<EuclideanVertex> vlist = new ArrayList<>();
        int i2 = 0;
        for(Vector2f v : data.houses){
            int[] co = { (int)v.y, (int)v.x};
            vlist.add(new EuclideanVertex(i2, co ));
            i2++;
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
        
        int width = (((50) * 11) + 1);
        int height = (((250) * 3) + 1);
        ArrayList<int[]> barriers = new ArrayList<>();
        for(int i = 0;i<height;i++){
            
            for(int x = 0; x< width;x++){
                if(i%3 != 0){
                    if(x%11 != 0){
                        barriers.add(new int[]{x,i});
                    }
                }
            }
        }
            
        
        int[][] bar = new int[barriers.size()][2];
        
        for (int i = 0; i < bar.length; i++) {
            bar[i] = barriers.get(i);
        }
        List<Vector2f> tottest = new LinkedList<>();
        for(int i = 0;i<temp.size();i++){

            EuclideanVertex badname = temp.get(i);

            EuclideanVertex jesus;
            if(i == (temp.size()-1)){
                jesus = temp.get(0);
            }else{
               jesus = temp.get(i+1);
            }
            //print(jesus.toString());             
            List<Vector2f> temps = test(
                    i, 
                    width, 
                    height, 
                    badname.getCoord(0), 
                    badname.getCoord(1), 
                    jesus.getCoord(0), 
                    jesus.getCoord(1), 
                    bar);

            tottest.addAll(temps);
        }
        System.out.println("Time in minutes: " + (tottest.size() * 3f/60f + data.houses.size()));
        
    }
    
    @Override
    public void keyPressed(int key) {

        if (key == GLFW_KEY_Q) {
            rot1 += 0.3;

        }
        if (key == GLFW_KEY_E) {
            rot1 -= 0.3;

        }
        if (key == GLFW_KEY_R) {
            rot2 += 0.3;

        }
        if (key == GLFW_KEY_F) {
            rot2 -= 0.3;

        }
        if (key == GLFW_KEY_K) {
            lodbias++;
            ground.setLODBias(lodbias);

        }
        if (key == GLFW_KEY_L) {
            lodbias--;
            ground.setLODBias(lodbias);

        }
        if (key == GLFW_KEY_M) {
            switch(mode1){
                case 0: 
                    ground.setMinFilter(GL_NEAREST);
                    mode1++;
                    break;
                 case 1: 
                    ground.setMinFilter(GL_LINEAR);
                    mode1++;
                    break;
                case 2: 
                    ground.setMinFilter(GL_NEAREST_MIPMAP_NEAREST);
                    mode1++;
                    break;
                case 3: 
                    ground.setMinFilter(GL_LINEAR_MIPMAP_LINEAR);
                    mode1 = 0;
                    break;
            }

        }
        if (key == GLFW_KEY_N){
            if(mode2 == 0){
                mode2 = 1;
                ground.setMagFilter(GL_NEAREST);
            }else{
                mode2 = 0;
                ground.setMagFilter(GL_LINEAR);
            }
        }
        

    }

    @Override
    public void keyReleased(int key) {

        if (key == GLFW_KEY_Q) {
            rot1 -= 0.3;

        }
        if (key == GLFW_KEY_E) {
            rot1 += 0.3;

        }
        if (key == GLFW_KEY_R) {
            rot2 -= 0.3;

        }
        if (key == GLFW_KEY_F) {
            rot2 += 0.3;

        }
    }
    
}
