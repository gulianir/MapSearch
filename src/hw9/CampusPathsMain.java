package hw9;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import hw5.Graph;
import hw6.MarvelParser.MalformedDataException;
import hw8.*;

/**
 * Main Class to run the CampusPaths with the GUI.
 * @author Rohan Guliani
 *
 */
public class CampusPathsMain {
    
    /**
     * starts up the GUI representation of both the campus map view
     * as well as the toolbars used to find routes.
     * @param model: The route finding model 
     */
    public static void startGUI(RouteFindingModel model){
        Graph.checkNull(model);        
        
        JFrame frame = new JFrame("Route Finding Tool");
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setLayout(new BoxLayout(frame.getContentPane(),
                      BoxLayout.Y_AXIS));
        
        CampusMapView view = new CampusMapView(model);
        
        CampusToolbars controls = new CampusToolbars(model, view);
        controls.setPreferredSize(new Dimension(1024, 70));
        
        frame.add(controls);
        frame.add(view);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String [] args) throws MalformedDataException{
        
        RouteFindingModel model = 
                new RouteFindingModel("src/hw8/data/campus_buildings.dat", 
                                      "src/hw8/data/campus_paths.dat");
        
        startGUI(model);
    }

}
