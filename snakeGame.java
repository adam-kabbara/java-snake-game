import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;


class snakeGame extends JComponent implements KeyListener{
    // Instance variables that define the current characteristics
    // of the animated objecs.
    final int WIDTH = 600; // width & height will be the same
    final int PAUSE = 100;
    final int rowsNum = 30;
    final int cellWidth = WIDTH / rowsNum;
    static JFrame frame = new JFrame();

    Snake snake;
    Cube snack;
    private int keyCode;
    private int score;
    private int highscore;
    private String highscoreFile = "highscore.txt";

    public snakeGame(){
        this.setPreferredSize(new Dimension(WIDTH, WIDTH)); // we set the size to gameWindow 
        frame.addKeyListener(this); // and not frame since frame contains the window properties 
                                   // tab (minimize; full screen; close)
        
        highscore = readHighscore();

    }

    public static void main(String[] args) {
        snakeGame gameWindow = new snakeGame();
        frame.add(gameWindow);         
        frame.pack(); // set frame size to content pane

        frame.setTitle("Snake Game");
        //frame.setResizable(false); todo
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Launch your animation!
        gameWindow.start();
    }
    
    // This special method is automatically called when the scene needs to be drawn.
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, WIDTH);
        snack.paintComponent(g, false);
        snake.draw(g);
        drawGrid(g);
        drawScore(g);
    }
    private void drawGrid(Graphics g){
        int x = 0;
        int y = 0;
        g.setColor(Color.WHITE);
        for (int i = 0; i<rowsNum; i++){
            y += cellWidth;
            g.drawLine(0, y, WIDTH, y);
            x += cellWidth;
            g.drawLine(x, 0, x, WIDTH);
        }
    }
    private void drawScore(Graphics g){
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.PLAIN, 25));
        g.drawString("score: "+score, 0, WIDTH-3);
        g.drawString("highscore: "+highscore, 0, WIDTH-25);

    }

    // Pause the program for ms milliseconds so the animation doesn't go too fast
    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException ex) {
            System.out.println("Error occurred!");
        }
    }

    public void start() {
        boolean collided;
        snake = new Snake(Color.RED, rowsNum/2 * cellWidth, rowsNum/2 * cellWidth, cellWidth);
        snack = randomSnack();
        while (true) { // todo check collision with borders in game loop and not snake class
            if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT){
                collided = snake.move(keyCode);
            }else{
                collided = snake.move(-1);
            }
            if (collided || checkWallCollision()){
                showLostPopup();
                break;
            }
            if (checkSnackCollision()){
                snack = randomSnack();
                snake.addCube();
                score++;
                if (score>highscore){
                    highscore = score;
                }
            }
            repaint();
            //Use pause(x) to pause the program's execution for x milliseconds (you should fill in a value for x)
            pause(PAUSE);
        }
    }

    public Cube randomSnack(){
        Random rand = new Random();
        int x = 0;
        int y = 0;
        int positions[][];
        boolean searchForSnackPosition = true;
        while (searchForSnackPosition){
            x = rand.nextInt(rowsNum) * cellWidth;
            y = rand.nextInt(rowsNum) * cellWidth;
            positions = new int[snake.body.size()][2];
            for (int[] pos: positions){
                if (! (pos == new int[] {x, y})){
                    searchForSnackPosition = false;
                    break;
                }
            }
        }
        return snack = new Cube(x, y, cellWidth, Color.GREEN); // fixx todo snack not in snake body
    }

    private void showLostPopup() {
        saveHighscore();
        System.out.println("youuuuu loooose");
        JOptionPane.showMessageDialog(frame, "AHAHAHAHA YOU LOSE");
        frame.dispose();

    }

    private void saveHighscore(){
        // create fle
        try {
            File file = new File(highscoreFile);
            if (file.createNewFile()) { //true if file created; false if it already exists
              System.out.println("File created: " + file.getName());
            } else {
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

        // write to file
        try {
            FileWriter myWriter = new FileWriter(highscoreFile);
            myWriter.write(String.valueOf(highscore)); //wierd value if i dont convert to string
            myWriter.close();
            System.out.println("Successfully saved highscore to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    private int readHighscore(){
        int hs = -1;
        try {
            File file = new File(highscoreFile);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              hs = Integer.parseInt(data);
            }
            myReader.close();
          } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

        return hs;
    }

    private boolean checkWallCollision(){
        if (snake.head.x < 0 || snake.head.x > WIDTH-cellWidth || snake.head.y < 0 || snake.head.y > WIDTH-cellWidth)
            return true;
        return false;
    }

    private boolean checkSnackCollision(){
        if (snake.head.x == snack.x && snake.head.y == snack.y)
            return true;
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();
    }


    //usless
    @Override
    public void keyReleased(KeyEvent e) {
    }@Override
    public void keyTyped(KeyEvent e) {
    }
}