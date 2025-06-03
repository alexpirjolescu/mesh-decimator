/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URL;
import mesh.Mesh;
import io.OBJMeshIO;
import FrameViewer.*;
import javax.swing.*;

/**
 *
 * @author alexp
 */
public class MeshLoaderTest {
    
    public MeshLoaderTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testOnjFileLoadsSuccessfully() throws Exception{
        // Load .obj file 
        URL is = getClass().getResource("/HumanMesh.obj");
        assertNotNull(is, "Failed to laod HumanMesh.obj from resources");
        
        File file = new File(is.toURI());
        OBJMeshIO loader = new OBJMeshIO();
        Mesh mesh = loader.load(file);
        System.out.println("Loaded mesh with " + mesh.getVertices().size() + " vertices.");
        
        JFrame frame = new JFrame("Wireframe Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.add(new WireframeViewer(mesh));
        frame.setVisible(true);
    }
}
