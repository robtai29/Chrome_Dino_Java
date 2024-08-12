package org.tairobea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class ChromeDino extends JPanel implements ActionListener, KeyListener {
    private final int boardWidth = 750;
    private final int boardHeight = 250;

    private Image cactusImg1;
    private Image cactusImg2;
    private Image cactusImg3;
    private Image bigCactusImg1;
    private Image bigCactusImg2;
    private Image bigCactusImg3;
    private Image birdImg1;
    private Image birdImg2;
    private Image birdImg3;
    private Image dinoImg1;
    private Image dinoImg2;
    private Image dinoImg3;
    private Image deadDinoImg;


    private Block dino;

    private int dinosaurWidth = 88;
    private int dinosaurHeight = 94;
    private int dinosaurX = 50;
    private int dinosaurY = boardHeight - dinosaurHeight;


    //cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;

    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    LinkedList<Block> cactusArray;

    int velocityX = -12; //cactus moving left speed
    int velocityY = 0; //dinosaur jump speed
    int gravity = 1;

    boolean gameOver = false;
    int score = 0;

    Random random = new Random();

    Timer gameLoop;
    Timer placeCactusTimer;
    boolean dinoImgAlternate = true;
    int count = 0;

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    ChromeDino(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);
        addKeyListener(this);
        initImages();
        cactusArray = new LinkedList<>();
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
        dino = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinoImg1);
        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });

        placeCactusTimer.start();

    }

    private void placeCactus() {
        Block cactus = null;
        int probableValue = random.nextInt(10);
        if (probableValue > 8){
            cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactusImg1);
        }else if (probableValue > 4){
            cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactusImg2);
        }else if (probableValue > 1){
            cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactusImg3);
        }

        if (cactus != null){
            cactusArray.add(cactus);
        }

        System.out.println(cactusArray.size());

    }

    private void initImages() {
        bigCactusImg1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/big-cactus1.png"))).getImage();
        bigCactusImg2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/big-cactus2.png"))).getImage();
        bigCactusImg3 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/big-cactus3.png"))).getImage();
        birdImg1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/bird1.png"))).getImage();
        birdImg2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/bird2.png"))).getImage();
        cactusImg1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/cactus1.png"))).getImage();
        cactusImg2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/cactus2.png"))).getImage();
        cactusImg3 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/cactus3.png"))).getImage();
        dinoImg1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/dino-run1.png"))).getImage();
        dinoImg2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/dino-run2.png"))).getImage();
        dinoImg3 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/dino.png"))).getImage();
        deadDinoImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/dino-dead.png"))).getImage();
    }


    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
                a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    private void move(){
        velocityY += gravity;
        dino.y += velocityY;

        if (dino.y > dinosaurY){
            dino.y = dinosaurY;
            velocityY = 0;
        }
        for (Block cactus: cactusArray){

            if (collision(dino, cactus)){
                gameOver = true;
                dino.img = deadDinoImg;
            }

            cactus.x += velocityX;

        }

        if (cactusArray.size() > 2){
            cactusArray.remove(0);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver){
            gameLoop.stop();
            placeCactusTimer.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g){
        drawDino(g);
        drawCactus(g);

        if (gameOver){
            drawGameOver(g);
        }

    }

    private void drawGameOver(Graphics g){
        g.setFont(new Font("Arial", 35, Font.BOLD));
        g.drawString("Game over", boardWidth/2, boardHeight/2);
    }

    private void drawCactus(Graphics g) {
        for (Block cactus : cactusArray){
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }
    }

    private void drawDino(Graphics g){
        g.drawImage(dino.img, dino.x, dino.y, dino.width, dino.height, null);
        count++;

        if (count >= 12){
            count = 0;
            dino.img = dinoImgAlternate ? dinoImg1 : dinoImg2;
            dinoImgAlternate = !dinoImgAlternate;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && isDinoOnGround()){
            velocityY = -16;
            dino.img = dinoImg3;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private boolean isDinoOnGround(){
        return dino.y == dinosaurY;
    }
}
