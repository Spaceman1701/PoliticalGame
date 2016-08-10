package gui;

import math.Line;
import math.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ethan on 8/10/2016.
 */
public class DrawPanel extends JPanel {

    private List<Line> drawList;

    public DrawPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));

        drawList = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight());

        for (Line l : drawList) {
            line(l.getStart(), l.getEnd(), g2, Color.BLACK);
        }

    }

    public void drawLine(Line line) {

    }

    private void line(Vector2i start, Vector2i end, Graphics2D g2, Color c) {

    }
}
