package object;

import java.lang.reflect.Array;
import java.util.*;

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
     * set of venues which user has made check-ins
     */
    private ArrayList<String> p_u;

    /**
     * set of venues which friends of user have made check-ins
     */
    private ArrayList<String> sp_u;

    /**
     * set of venues which user and his friends have not made any check-ins
     */
    private ArrayList<String> n_u;

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

        p_u = new ArrayList<>(getAllVenues());
    }

    public void updateOtherSet(ArrayList<String> sp_u, ArrayList<String> n_u) {
        this.sp_u = sp_u;
        this.n_u = n_u;
    }

    /**
     * Random select venue that user has been check-ined
     * @return  the random venue id
     */
    public String getRandomP_u() {
        if (p_u.size() == 0)
            return null;
        Random r = new Random();
        return p_u.get(r.nextInt(p_u.size()));
    }

    /**
     * Random select venues that friends of user have been check-ined. Null if no friends
     * @return  venue id
     */
    public String getRandomSP_u() {
        if (sp_u.size() == 0)
            return null;
        Random r = new Random();
        return sp_u.get(r.nextInt(sp_u.size()));
    }

    /**
     * Random select venues that user and his friend never visit
     * @return  venue id
     */
    public String getRandomN_u() {
        if (n_u.size() == 0)
            return null;
        Random r = new Random();
        return n_u.get(r.nextInt(n_u.size()));
    }

    /**
     *
     * @return the set of venue id where user has done check-in
     */
    public Set<String> getAllVenues() {
        return checkinMap.keySet();
    }
}
