/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class LocalPlayer extends Player
{
    private float directionX;
    private float directionY;

    private boolean wallHit;
    public double wallHitX;
    public double wallHitY;

    public static final float WALL_UNHIT_RANGE = 5.0f;

    public LocalPlayer(float initX, float initY)
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

    public void move(float x, float y, Map map)
    {
        directionX = (float) ((x - this.x) / Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y)));
        directionY = (float) ((y - this.y) / Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y)));
        if (!wallHit)
        {
            for (Map.Obstacle obstacle : map.obstacles)
            {
                if (obstacle.check(x, y))
                {
                    // Ax+By+C=0
                    float A, B, C;
                    if ((x - this.x) == 0)
                    {
                        A = 1.0f;
                        B = 0.0f;
                    }
                    else if ((y - this.y) == 0)
                    {
                        A = 0.0f;
                        B = 1.0f;
                    }
                    else
                    {
                        A = 1.0f;
                        B = -A * (x - this.x) / (y - this.y);
                    }
                    C = -A * x - B * y;
                    float[] temp;
                    try
                    {
                        temp = obstacle.boundariesCrossing(A, B, C, x, y);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        temp = new float[]{0.0f, 0.0f};
                    }
                    wallHit = true;
                    wallHitX = temp[0];
                    wallHitY = temp[1];
                }
            }
        }
        else
        {
            if (Math.sqrt((x - wallHitX) * (x - wallHitX) + (y - wallHitY) * (y - wallHitY)) < WALL_UNHIT_RANGE)
            {
                boolean stillInsideTheWall = false;
                for (Map.Obstacle obstacle : map.obstacles)
                {
                    if (obstacle.check(x, y))
                        stillInsideTheWall = true;
                }
                if (!stillInsideTheWall)
                    wallHit = false;
            }
        }
        this.x = x;
        this.y = y;
    }
}