package gui;

import map.MapGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Window {

    private JFrame frame;
    private MapGenerator mg;

    private DrawPanel drawPanel;

    private JFileChooser fileChooser;

    public Window(int drawX, int drawY, MapGenerator mg) {
        this.mg = mg;

        fileChooser = new JFileChooser();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        drawPanel = new DrawPanel(drawX, drawY);
        frame.add(drawPanel, BorderLayout.CENTER);

        createAddButton();
        createProcessButton();

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void createProcessButton() {
        JButton button = new JButton("Process Map");
        button.addActionListener(ae -> mg.generateMap());
        frame.add(button, BorderLayout.EAST);
    }

    private void createAddButton() {
        JButton button = new JButton("Add Map");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    String name = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        mg.loadNewKMLMap(name);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Selected file as kml: " + name);
                }
            }
        });
        frame.add(button, BorderLayout.NORTH);
    }

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }
}
