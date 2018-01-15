package object;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author tndoan
 *
 */
public class VenueObject {

    public VenueObject(String id, int totalCks, ArrayList<String> userIds, int k){
        this.id = id;
        this.userIds = userIds;
        this.totalCks = totalCks;

        Random generator = new Random();
        this.factors = new double[k];
        for (int i = 0; i < k; i++)
            this.factors[i] = generator.nextDouble() + 1.0;
        this.bias = generator.nextDouble() + 1.0;
    }

    /**
     * latent factor vector
     */
    private double[] factors;

    /**
     * bias of venue
     */
    private double bias;

    public double[] getFactors() {
        return factors;
    }

    public void setFactors(double[] factors) {
        this.factors = factors;
    }

    /**
     * total number of check-in that it has
     */
    private int totalCks;

    /**
     * id of venue
     */
    private String id;

    /**
     * list of user ids who have check-in in this venue
     */
    private ArrayList<String> userIds;

    public String getId() {
        return id;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public int getTotalCks() {
        return totalCks;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
