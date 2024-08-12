package org.tairobea;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private int height = 250;
    private int width = 750;


    public App(){
        setTitle("Chrome Dino");
        setSize(new Dimension(width,height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        ChromeDino chromeDino = new ChromeDino();
        add(chromeDino);
        chromeDino.requestFocus();
        pack();
        setVisible(true);







    }
}
