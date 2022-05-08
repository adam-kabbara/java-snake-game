import java.awt.*;

public class Cube {

    int x;
    int y;
    int dirX;
    int dirY;
    private int w;
    private Color c;

    public Cube(int x, int y, int w, Color c){
        this.x = x;
        this.y = y;
        this.w = w;
        this.c = c;
        this.dirX = 0;
        this.dirY = 0;
    }

    public void paintComponent(Graphics g, boolean firstCube) {
        g.setColor(c);
        if (firstCube)
            g.setColor(Color.PINK);
        g.fillRect(x, y, w, w);
    }

    public void move(int dirX, int dirY){
        this.dirX = dirX;
        this.dirY = dirY;
        this.x = this.x + this.dirX * this.w;
        this.y = this.y + this.dirY * this.w;
    }
}
