package hw9;

import hw8.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import hw5.Graph;
import java.util.*;

public class CampusToolbars extends JPanel{
    
    private static final long serialVersionUID = 1L;
    
    private CampusMapView view;
    JComboBox<String> startSelectionBox;
    JComboBox<String> endSelectionBox;
       
    /**
     * Constructor to add the toolbars to the GUI.
     * 
     * @param m model of campus route tool
     * @param v map view of the campus route tool
     * @requires m and v to be non-null.
     */
    public CampusToolbars(RouteFindingModel m, CampusMapView v) {
        
        Graph.checkNull(m);
        Graph.checkNull(v);

        RouteFindingModel model = m;
        view = v;
        
        Set<String> buildings = new TreeSet<String>(model.getAllBuildings().values());
        String[] buildingsArray = buildings.toArray(new String[0]);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        JLabel startBuilding = new JLabel("Start building (blue): ");
        startSelectionBox = new JComboBox<>(buildingsArray);
        JLabel endBuilding = new JLabel("End building (red): ");
        endSelectionBox = new JComboBox<>(buildingsArray);
        JButton getRouteButton = new JButton("Get Route");
        getRouteButton.addActionListener(new UpdateActionListener());
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new UpdateActionListener());
        
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints dimensions = new GridBagConstraints();
        
        
        // change dimensions for different buttons 
        dimensions.gridx = 1;
        dimensions.gridy = 1;
        dimensions.gridwidth = 3;
        selectionPanel.add(startBuilding, dimensions);
        dimensions.gridx = 6;
        dimensions.gridy = 1;
        dimensions.gridwidth = 5;
        selectionPanel.add(startSelectionBox, dimensions);
        dimensions.gridx = 2;
        dimensions.gridy = 2;
        dimensions.gridwidth = 3;
        selectionPanel.add(endBuilding, dimensions);
        dimensions.gridx = 10;
        dimensions.gridy = 2;
        dimensions.gridwidth = 5;
        selectionPanel.add(endSelectionBox, dimensions);
        
        buttonPanel.add(getRouteButton);
        buttonPanel.add(resetButton);
        
        this.add(buttonPanel);
        this.add(selectionPanel);
        
    }
    
    /**
     * Overrides action listener to update the shortest route or to reset the view.
     */
    private class UpdateActionListener implements ActionListener {
        /**
         * Invoked when an action occurs; in this case, when a button is
         * pressed.
         * 
         * @param e: an event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if (action.equals("Get Route")) {
                String startBuilding = startSelectionBox.getSelectedItem().toString();
                String endBuilding = endSelectionBox.getSelectedItem().toString();
                view.getShortestRoute(startBuilding, endBuilding);
            // since there are only two options, if the action wasn't to get
            // the route then it would be to reset the path
            } else {
                view.reset();
            }
        }
    }

}
