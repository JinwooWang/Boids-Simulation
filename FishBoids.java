import java.awt.Point;

public class FishBoids{
    private Point[] fishes = null; //define fish boids

    private int width = 700; //define the width of canvas
    private int height = 500; //define the height of canvas
    private int numSteps = 10000; //define the total number of steps
    private int numFish = 20; //define the number of fishes
    private int numNeighbor = 5; //define the number of neighbors for each fish
    private int leader = 0; //define the leader index
    private int stepSize = 8; //define the normal size of each step
    private int bounder = 50; //define the bounder of canvas
    private int radius = 5; //difine the radius of fish

    private final double pi = Math.PI;
    private double direction = pi/3.0; //define the direction of the movement
    private double leaderChangeProb = 0.0001; //define the probability for leader changing
    private double randomMoveProb = 0.001; //define the probability for random movement
    //get bounder
    public int getBounder()
    {
        return bounder;
    }
    //get width
    public int getWidth()
    {
        return width;
    }
    //get height
    public int getHeight()
    {
        return height;
    }

    //get step number
    public int getNumSteps()
    {
        return numSteps;
    }
    //get fish array
    public Point[] getFishes()
    {
        return fishes;
    }
    //get radius
    public int getRadius()
    {
        return radius;
    }
    //get leader index
    public int getLeader()
    {
        return leader;
    }
    //initialization
    public void init()
    {
        fishes = new Point[numFish];
        for (int i = 0;i < numFish;i ++)
        {
            int x = UniformDistribution.uniform(bounder, width + bounder);
            int y = UniformDistribution.uniform(bounder, height + bounder);
            fishes[i] = new Point(x, y);
        }
    }
    //define the fish behavior
    public void fishMovement()
    {

        double prob = UniformDistribution.uniform();
        if (prob < leaderChangeProb)
        {
            leader = UniformDistribution.uniform(0, numFish - 1);
            direction = UniformDistribution.uniform(0.0, pi);
        }

        for (int i = 0;i < numFish; i ++)
        {
            if (leader == i)
                fishes[i] = leaderMovement();
            else
                fishes[i] = followerMovement(i);
        }

    }
    //define the leader movement
    private Point leaderMovement()
    {
        int leader_x = (int)(fishes[leader].x + stepSize * Math.cos(direction));
        int leader_y = (int)(fishes[leader].y + stepSize * Math.sin(direction));

        if ((leader_x < bounder) || (leader_x > width + bounder) || (leader_y < bounder) || leader_y > height + bounder)
        {
            double prob = UniformDistribution.uniform();
            if (prob < 0.5)
                direction = UniformDistribution.uniform(0.0, pi);

            else
                direction = 2*pi - direction;


            leader_x = fishes[leader].x;
            leader_y = fishes[leader].y;
        }
        Point retleader = new Point(leader_x, leader_y);
        return retleader;
    }
    //define the follower movement
    private Point followerMovement(int followerInd)
    {
        double prob = UniformDistribution.uniform();
        if (prob < randomMoveProb)
            return randomMovement(followerInd);
        else
        {
            int[] neighbors = getNeighbors(followerInd);
            Point centroid = getCentroid(neighbors);

            return rules(followerInd, centroid);
        }
    }
    //define the follower behavior rule1
    private Point rule1(int followerInd)
    {
        int x = 0;
        int y = 0;
        for(int i = 0;i < numFish;i ++)
            if(i != followerInd)
            {
                x += fishes[i].x;
                y += fishes[i].y;
            }
        x /= numFish - 1;
        y /= numFish - 1;
        return new Point((x - fishes[followerInd].x) / 2, (y - fishes[followerInd].y) / 2);
    }
    //define the follower behavior rule2
    private Point rule2(int followerInd)
    {
        int x = 0;
        int y = 0;
        for(int i = 0;i < numFish;i ++)
        {
            if (i != followerInd)
                if (distance(fishes[i].x, fishes[i].y, fishes[followerInd].x, fishes[followerInd].y) < 20 * 20)
                {
                    x -= fishes[i].x - fishes[followerInd].x;
                    y -= fishes[i].y - fishes[followerInd].y;
                }
        }
        return new Point(x, y);
    }
    //define the follower behavior rule3
    private Point rule3(int followerInd, Point centroid)
    {
        int x = 0;
        int y = 0;
        double centerDistance = Math.sqrt(distance(fishes[followerInd].x, fishes[followerInd].y, centroid.x, centroid.y));
        double verocityControl = 1.0;
        if (centerDistance < (double)stepSize)
            verocityControl = 1.0;
        else
            verocityControl = (double)stepSize / centerDistance;

        x = (int) (fishes[followerInd].x + verocityControl * (centroid.x - fishes[followerInd].x));
        y = (int) (fishes[followerInd].y + verocityControl * (centroid.y - fishes[followerInd].y));

        return new Point(x, y);
    }
    //applying rule1, rule2, rule3
    private Point rules(int followerInd, Point centroid)
    {
        Point v1 = rule1(followerInd);
        Point v2 = rule2(followerInd);
        Point v3 = rule3(followerInd, centroid);
        return new Point(v1.x + v2.x + v3.x, v1.y + v2.y + v3.y);
    }
    //define random movement behavior
    private Point randomMovement(int followerInd)
    {
        double angle = UniformDistribution.uniform(0.0, 2.0 * pi);
        int x = (int)(fishes[followerInd].x + stepSize * Math.cos(angle));
        int y = (int)(fishes[followerInd].y + stepSize * Math.sin(angle));
        return new Point(x, y);
    }
    //get the nearest(index-based) several neighbors
    private int[] getNeighbors(int followerInd)
    {
        int[] neighbors = new int[numNeighbor];
        int cfidx = followerInd;
        for(int i = 0;i < numNeighbor;i ++)
        {
            cfidx = cfidx % (numNeighbor - 1) + 1;
            neighbors[i] = cfidx;
        }
        return neighbors;
    }
    //get centroid of these neighbors
    private Point getCentroid(int[] neighbors)
    {
        int ctrd_x = 0;
        int ctrd_y = 0;
        for(int i = 0;i < numNeighbor;i ++)
        {
            ctrd_x += fishes[neighbors[i]].x / numNeighbor;
            ctrd_y += fishes[neighbors[i]].y / numNeighbor;
        }
        return new Point(ctrd_x, ctrd_y);
    }
    //define the distance(without sqrt)
    private int distance(int x1, int y1, int x2, int y2)
    {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }


}
