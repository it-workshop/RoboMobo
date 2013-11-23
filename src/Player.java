/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class Player
{
    private int id;
    public double x;
    public double y;
    //NOTE: по умолчанию ось X - вправо, ось Y - вверх
    private double directionX;
    private double directionY;
    private int score;

    private boolean wallHit;
    public double wallHitX;
    public double wallHitY;

    public Player(double initX, double initY)
    {
        id = 0; //TODO: синхронизация ID
        x = initX;
        y = initY;
        directionX = 0;
        directionY = 1;
        score = 0;
        wallHit = false;
        wallHitX = 0;
        wallHitY = 0;
    }

    public void move(double x, double y)
    {
        directionX = (x - this.x) / Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y));
        directionY = (y - this.y) / Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y));
        this.x = x;
        this.y = y;
    }
}