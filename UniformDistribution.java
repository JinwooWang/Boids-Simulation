import java.util.Random;

public final class UniformDistribution {

    private static long seed = System.currentTimeMillis();//get seed
    private static Random rd = new Random(seed);//get random number

    //uniform distribution for inteager[0-N)
    public static int uniform(int N)
    {
        return rd.nextInt(N);
    }
    //uniform distribution for double[0-1)
    public static double uniform()
    {
        return rd.nextDouble();
    }
    //uniform distribution for inteager[start-end)
    public static int uniform(int start, int end)
    {
        return start + uniform(end - start);
    }
    //uniform distribution for double[start-end)
    public static double uniform(double start, double end)
    {
        return start + uniform() * (end - start);
    }
}
