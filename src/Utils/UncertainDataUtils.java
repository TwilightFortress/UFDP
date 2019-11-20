package Utils;

import java.text.DecimalFormat;

/**
 * @author yuyang
 * @create 2019-11-20 15:18
 */
public class UncertainDataUtils {


    /**
     * @return 标准正态随机分布
     */
    public static double StandardNormalDistribution() {
        java.util.Random random = new java.util.Random();
        return random.nextGaussian();
    }

    /**
     * @return 0-1的随机概率
     */
    public static String RoadomProb() {
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        return dcmFmt.format(Math.random());
    }
}
