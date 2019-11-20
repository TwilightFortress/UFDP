package Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yuyang
 * @create 2019-11-20 9:51
 */
public class DPutils {

    /**
     * 指数机制 给定一个打分数组，然后从其中按照指数机制公式输出其的索引
     *
     * @param score
     * @param sensity
     * @param eps
     * @return
     */
    public static int dpIndex(List<Double> score, Double sensity, Double eps) {
        int m = score.size();
        double r = Math.random();
        List<Double> exponents_list = new LinkedList();
        exponents_list.add(Math.log(0.5 * (score.get(0) * eps / sensity)));
        for (int i = 1; i < m; i++) {
            exponents_list.add(Math.log(0.5 * (score.get(i) * eps / sensity)));
        }

        double sum = exponents_list.stream().mapToDouble(Double::doubleValue).sum();
        for (int i = 0; i < m; i++)
            exponents_list.set(i, exponents_list.get(i) / sum);
        double sum_exp = 0;
        int j = 0;
        for (; ; j++) {
            sum_exp = sum_exp + exponents_list.get(j);
            if (sum_exp > r)
                break;
        }
        return j;
    }

    /**
     * laplace机制，根据全局敏感度和隐私参数确定所添加的噪音大小
     *
     * @param sensity 全局敏感度
     * @param eps     隐私参数
     * @return
     */
    public static double dpLaplace(double sensity, double eps) {
        double beta = sensity / eps;
        double u1 = Math.random();
        double u2 = Math.random();
        if (u1 < 0.5)
            return (-1 * beta * Math.log(1.0 - u2));
        else
            return (-1 * beta * Math.log(u2));
    }


//    double laplaceMechanismCount(double realCountResult, double epsilon) {
//        LaplaceDistribution ld = new LaplaceDistribution(0, 1 / epsilon);
//        double noise = ld.sample();
//        return realCountResult + noise;
//    }
}
