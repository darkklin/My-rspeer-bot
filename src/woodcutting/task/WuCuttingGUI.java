package woodcutting.task;

import woodcutting.WuCutting;
import woodcutting.data.Location;
import woodcutting.data.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WuCuttingGUI extends JFrame {

    private  JComboBox treeComboBox;
    private JComboBox locationComboBox;
    private JButton initiate;

    public WuCuttingGUI(){
        super("WuCutting Configuration");

        setLayout(new FlowLayout());

        initiate = new JButton("Initiate");
        locationComboBox = new JComboBox(Location.values());
        treeComboBox = new JComboBox(Tree.values());

        locationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ActionEvent");

                Location location = (Location) locationComboBox.getSelectedItem();
                treeComboBox.setModel(new DefaultComboBoxModel(location.getTrees()));
            }
        });

        initiate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("initiate");

                WuCutting.location=(Location) locationComboBox.getSelectedItem();
                WuCutting.tree = (Tree) treeComboBox.getSelectedItem();
                setVisible(false);
            }
        });

        add(locationComboBox);
        add(treeComboBox);
        add(initiate);


        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
    }
    public static void main(String... args){
        new WuCuttingGUI().setVisible(true);
    }


}
