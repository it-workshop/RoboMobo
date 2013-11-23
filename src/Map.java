import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: loredan
 * Date: 23.11.13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class Map
{
    private static final double width = 100;
    private static final double height = 100;
    private class Obstacle
    {
        private Rect boundaries;
        private int type;
    }

    private ArrayList<Obstacle> obstacles;
}
