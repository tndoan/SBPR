package model;

/**
 * Created by tndoan
 */
public class Parameters {

    /**
     * regularizer parameter for user vector
     */
    private double lambda_u;

    /**
     * regularizer parameter for venue vector
     */
    private double lambda_v;

    /**
     * regularizer parameter for bias
     */
    private double lambda_b;

    /**
     *
     * @param lambda_u  regularizer parameter for user vector
     * @param lambda_v  regularizer parameter for venue vector
     * @param lambda_b  regularizer parameter for bias
     */
    public Parameters(double lambda_u, double lambda_v, double lambda_b) {
        this.lambda_b = lambda_b;
        this.lambda_u = lambda_u;
        this.lambda_v = lambda_v;
    }

    /**
     *
     * @return  regularizer of user
     */
    public double getLambda_u() {
        return lambda_u;
    }

    /**
     *
     * @return  regularizer of venue
     */
    public double getLambda_v() {
        return lambda_v;
    }

    /**
     *
     * @return  regularizer of friendship
     */
    public double getLambda_b() {
        return lambda_b;
    }
}
