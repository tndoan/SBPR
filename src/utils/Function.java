package utils;

/**
 *
 * @author tndoan
 *
 */
public class Function {
    /**
     * calculate the sigmoid function of x
     *
     * @param x
     * @return
     */
    public static double sigmoidFunction(double x) {
        double result = 1.0 / (1.0 + Math.exp(-x));
        return result;
    }

    /**
     * return vector x - y
     * @param x
     * @param y
     * @return
     */
    public static double[] minus(double[] x, double[] y) {
        assert(x.length == y.length);
        double[] result = new double[x.length];

        for (int i = 0; i < x.length; i++)
            result[i] = x[i] - y[i];

        return result;
    }

    /**
     * return vector x + y
     * @param x
     * @param y
     * @return
     */
    public static double[] plus(double[] x, double[] y) {
        assert(x.length == y.length);
        double[] result = new double[x.length];

        for (int i = 0; i < x.length; i++)
            result[i] = x[i] + y[i];

        return result;
    }

    /**
     * return vector t * x
     * @param t		scalar value
     * @param x		vector in the format of array
     * @return
     */
    public static double[] multiply(double t, double[] x) {
        double[] result = new double[x.length];

        for (int i = 0; i < x.length; i++)
            result[i] = t * x[i];

        return result;
    }

    /**
     * taking differentiation of log Sigmoid function at one point. Should be
     * careful if the parameter is -x
     *
     * @param x
     *            value
     * @return differentiation
     */
    public static double diffLogSigmoidFunction(double x) {
        double s = sigmoidFunction(x);
        return (1.0 - s);
    }

    /**
     * calculate the inner product of two vectors
     * @param v1
     * @param v2
     * @return
     */
    public static double innerProduct(double[] v1, double[] v2) {
        int length = v1.length;
        assert (v1.length == v2.length);

//		double result = IntStream.range(0, length).mapToDouble(i -> v1[i] * v2[i]).sum(); // it is slower than using for :(
        double result = 0.0;
        for (int i = 0; i < length; i++)
            result += v1[i] * v2[i];

        return result;
    }

    /**
     * calculate the square norm of a vector
     * @param u	vector in the form of array
     * @return	square norm of this vector
     */
    public static double sqrNorm(double[] u) {
        return innerProduct(u, u);
    }

    /**
     * calculate the value of (tanh(x) + 1) / 2
     * @param x
     * @return		value of function
     */
    public static double tanh1_2(double x) {
        return 1.0 / (1.0 + Math.exp(-2.0 * x));
    }

    /**
     * return the logistic function whose midpoint = 0
     * it is the general case of Sigmoid and tanh
     * steepness = 1  => Sigmoid
     * steepness = 2  => (1 + tanh)/2
     * @param a	steepness of logistic function
     * @param x	variable
     * @return	logistic function
     */
    public static double logisticFunc(double a, double x) {
        return 1.0 / (1.0 + Math.exp(-a * x));
    }
}