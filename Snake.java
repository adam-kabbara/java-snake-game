import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Snake{
    
    public ArrayList<Cube> body;
    private HashMap<String, String> turns;
    Cube head;
    private int cubeWidth;
    private Color c;

    private int dirX;
    private int dirY;
    int prevCode;


    public Snake (Color c, int x, int y, int cubeWidth){
        this.body = new ArrayList<Cube>();
        this.turns = new HashMap<String, String>();
        this.cubeWidth = cubeWidth;
        this.head = new Cube(x, y, this.cubeWidth, c);
        this.body.add(this.head);
        this.c = c;
        this.dirX = 0;
        this.dirY = 0;
        this.prevCode = 0;
    }

    public void draw(Graphics g){
        Cube cube;
        for (int i=0; i<this.body.size(); i++){
            cube = this.body.get(i);
            if (i == 0)
                cube.paintComponent(g, true);
            else{
                cube.paintComponent(g, false);
            }
        }
    }
    
    public boolean move(int keyCode){

        if (KeyEvent.VK_RIGHT == keyCode && this.dirX != -1){
            this.dirX = 1;
            this.dirY = 0;
            this.turns.put(Integer.toString(this.head.x)+","+Integer.toString(this.head.y), Integer.toString(this.dirX)+","+Integer.toString(this.dirY));
            this.prevCode = keyCode;
        }
        else if(KeyEvent.VK_LEFT == keyCode && this.dirX != 1){
            this.dirX = -1;
            this.dirY = 0;
            this.turns.put(Integer.toString(this.head.x)+","+Integer.toString(this.head.y), Integer.toString(this.dirX)+","+Integer.toString(this.dirY));
            this.prevCode = keyCode;
        }
        else if(KeyEvent.VK_UP == keyCode && this.dirY != 1){
            this.dirX = 0;
            this.dirY = -1;
            this.turns.put(Integer.toString(this.head.x)+","+Integer.toString(this.head.y), Integer.toString(this.dirX)+","+Integer.toString(this.dirY));
            this.prevCode = keyCode;
        }
        else if(KeyEvent.VK_DOWN == keyCode && this.dirY != -1){
            this.dirX = 0;
            this.dirY = 1;
            this.turns.put(Integer.toString(this.head.x)+","+Integer.toString(this.head.y), Integer.toString(this.dirX)+","+Integer.toString(this.dirY));
            this.prevCode = keyCode;
        }

        // was not able to compare to key of type Integer[] so gonna make them String
        for (Cube c : this.body){ 
            String cubePosition = Integer.toString(c.x)+","+Integer.toString(c.y);
            if (this.turns.containsKey(cubePosition)){ // if the head turned at this position, make
                int[] newDirs = {Integer.parseInt(this.turns.get(cubePosition).split(",")[0]), Integer.parseInt(this.turns.get(cubePosition).split(",")[1])}; // all other cubes turn here
                c.move(newDirs[0], newDirs[1]);
                if (this.body.indexOf(c) == this.body.size() - 1){ // if the last element turned
                    this.turns.remove(cubePosition); // stop all elems from turning at this pos 
                }
            }
            else{
                c.move(c.dirX, c.dirY);
            }

        }
        return checkCollision();
    }

    public void addCube(){
        Cube tail = this.body.get(this.body.size()-1);

        if (tail.dirX == 1)
            this.body.add(new Cube(tail.x - cubeWidth, tail.y, cubeWidth, this.c));
        else if (tail.dirX == -1)
            this.body.add(new Cube(tail.x + cubeWidth, tail.y, cubeWidth, this.c));
        else if (tail.dirY == 1)
            this.body.add(new Cube(tail.x, tail.y - cubeWidth, cubeWidth, this.c));
        else if (tail.dirY == -1)
            this.body.add(new Cube(tail.x, tail.y + cubeWidth, cubeWidth, this.c));

        this.body.get(this.body.size()-1).dirX = tail.dirX;
        this.body.get(this.body.size()-1).dirY = tail.dirY;
    }

    private boolean checkCollision(){
        int[][] snakePositions = new int[this.body.size()][2];
        for (int i=1; i<snakePositions.length; i++){
            if (this.head.x == this.body.get(i).x && this.head.y == this.body.get(i).y){
                return true;
            }
        }
        return false;
    }


}
