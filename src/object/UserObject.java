package object;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class UserObject {

    /**
     * checkin map whose key is venue id, value is number of check-in that user has made in this venue
     */
    private HashMap<String, Integer> checkinMap;

    /**
     * latent factor vector
     */
    private double[] factors;

    private ArrayList<String> listOfFriends;

    /**
     *
     * @return	list of id of his friends
     */
    public ArrayList<String> getListOfFriends() {
        return listOfFriends;
    }

    public double[] getFactors() {
        return factors;
    }

    public void setFactors(double[] factors) {
        this.factors = factors;
    }

    /**
     * id of user
     */
    private String id;

    /**
     * get how many check-in user has done in this venue
     * @param vIds	venue id
     * @return		number of check-in
     */
    public int retrieveNumCks(String vIds){
        Integer num = checkinMap.get(vIds);
        if (num == null)
            return 0;
        else
            return num;
    }

    public String getId() {
        return id;
    }

    /**
     *
     * @param id			id of user
     * @param checkinMap	check-in map of user
     * @param lOfFriends	friend of user
     * @param k				# of latent features
     */
    public UserObject(String id, HashMap<String, Integer> checkinMap, ArrayList<String> lOfFriends, int k){
        this.id = id;
        this.checkinMap = checkinMap;
        this.factors = new double[k];
        this.listOfFriends = lOfFriends;

        Random generator = new Random();
        for (int i = 0; i < k; i++)
            this.factors[i] = generator.nextDouble();
    }

    /**
     *
     * @return the set of venue id where user has done check-in
     */
    public Set<String> getAllVenues() {
        return checkinMap.keySet();
    }
}
