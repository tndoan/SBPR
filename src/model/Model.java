package model;

import object.UserObject;
import object.VenueObject;
import utils.Function;
import utils.ReadFile;
import utils.Utils;

import java.io.IOException;
import java.util.*;

/**
 * Created by tndoan on 1/14/18.
 */
public class Model {

    /**
     * number of latent factors
     */
    private int k;

    private HashMap<String, VenueObject> venueMap;

    private HashMap<String, UserObject> userMap;

    private Parameters params;

    public Model(String uFile, String venueLocFile, String cksFile, String fFile, int k, Parameters params){
        this.k = k;
        this.params = params;

        // initialize
        venueMap = new HashMap<>();
        userMap = new HashMap<>();

        HashMap<String, HashMap<String, Integer>> cksMap = ReadFile.readNumCksFile(cksFile);

        HashMap<String, ArrayList<String>> userOfVenueMap = Utils.collectUsers(cksMap);
        HashMap<String, ArrayList<String>> friendInfoMap = ReadFile.readFriendship(fFile);
        HashMap<String, Integer> countMap = Utils.countCks(cksMap);

        Set<String> allVenues = userOfVenueMap.keySet();
        // make user object
        Set<String> uSet = cksMap.keySet();
        for (String uId : uSet) {
            HashMap<String, Integer> checkinMap = cksMap.get(uId);
            ArrayList<String> lOfFriends = friendInfoMap.get(uId);
            UserObject u = new UserObject(uId, checkinMap, lOfFriends, k);
            userMap.put(uId, u);
        }

        // update user object
        for (String uId : uSet) {
            UserObject uObj = userMap.get(uId);
            Set<String> vOfU = new HashSet<>(uObj.getAllVenues());
            ArrayList<String> lOfFriends = uObj.getListOfFriends();
            Set<String> sp_u = new HashSet<>();

            if (lOfFriends != null) {// users without friends
                for (String fId : lOfFriends) {
                    UserObject fObj = userMap.get(fId);
                    if (fObj != null)
                        sp_u.addAll(fObj.getAllVenues());
                }
                sp_u.removeAll(vOfU); // only venues friend makes check-in
            }

            Set<String> n_u = new HashSet<>(allVenues); // set of venues which is not check-ined by user and his friends
            n_u.removeAll(sp_u);
            n_u.removeAll(vOfU);
            uObj.updateOtherSet(new ArrayList<>(sp_u), new ArrayList<>(n_u));
        }
        System.out.println("Finish loading users. Total users:" + venueMap.keySet().size());

        // make venue object
        for (String vId : allVenues) {
            ArrayList<String> uOfV = userOfVenueMap.get(vId);
            int totalCks = countMap.get(vId);
            VenueObject vObj = new VenueObject(vId, totalCks, uOfV, k);
            venueMap.put(vId, vObj);
        }

        params = new Parameters(0.01, 0.01, 0.01);
    }

    public void learnParameters(int numIteration) {
        ArrayList<String> allUsers = new ArrayList<>(userMap.keySet());
        double learningRate = -0.0001;

        for (int iter = 0; iter < numIteration; iter++) {
            Collections.shuffle(allUsers);
            for (String uId : allUsers) { // randomly select a user
                UserObject uObj = userMap.get(uId);
                String i = uObj.getRandomP_u();
                String j = uObj.getRandomN_u();
                String k = uObj.getRandomSP_u();

                if (k == null) { // if no venues from his social network, treat as BPR
                    VenueObject iObj = venueMap.get(i);
                    VenueObject jObj = venueMap.get(j);

                    double x_ui = Function.innerProduct(uObj.getFactors(), iObj.getFactors()) + iObj.getBias();
                    double x_uj = Function.innerProduct(uObj.getFactors(), jObj.getFactors()) + jObj.getBias();
                    double x_uij = x_ui - x_uj;

                    // user gradient
                    double[] Vij = Function.minus(iObj.getFactors(), jObj.getFactors());
                    double[] uGrad = Function.multiply(1.0 - Function.sigmoidFunction(x_uij), Vij);

                    // venue i gradient
                    double weight_i = 1.0 - Function.sigmoidFunction(x_uij);
                    double[] iGrad = Function.multiply(weight_i, uObj.getFactors());

                    // venue j gradient
                    double weight_j = - (1.0 - Function.sigmoidFunction(x_uij));
                    double[] jGrad = Function.multiply(weight_j, uObj.getFactors());

                    double i_biasGrad = weight_i; // bias of venue i
                    double j_biasGrad = weight_j; // bias of venue j

                    // update
                    // update user
                    double[] uVec = Function.minus(uObj.getFactors(), Function.multiply(learningRate, uGrad));
                    uObj.setFactors(uVec);

                    // update i
                    double[] iVec = Function.minus(iObj.getFactors(), Function.multiply(learningRate, iGrad));
                    iObj.setFactors(iVec);

                    // update j
                    double[] jVec = Function.minus(jObj.getFactors(), Function.multiply(learningRate, jGrad));
                    jObj.setFactors(jVec);
                } else {
                    VenueObject iObj = venueMap.get(i);
                    VenueObject jObj = venueMap.get(j);
                    VenueObject kObj = venueMap.get(k);

                    double x_ui = Function.innerProduct(uObj.getFactors(), iObj.getFactors()) + iObj.getBias();
                    double x_uk = Function.innerProduct(uObj.getFactors(), kObj.getFactors()) + kObj.getBias();
                    double x_uj = Function.innerProduct(uObj.getFactors(), jObj.getFactors()) + jObj.getBias();

                    double x_uik = x_ui - x_uk;
                    double x_ukj = x_uk - x_uj;

                    double auc = Math.log(Function.sigmoidFunction(x_uik)) + Math.log(Function.sigmoidFunction(x_ukj));
                    System.out.println("Before:\t" + auc);

                    // gradient of user
                    double[] Vik = Function.minus(iObj.getFactors(), kObj.getFactors());
                    double[] Vjk = Function.minus(jObj.getFactors(), kObj.getFactors());
                    double[] uGrad = Function.multiply(1.0 - Function.sigmoidFunction(x_uik), Vik);
                    double weight_vjk = 1 - Function.sigmoidFunction(x_ukj);
                    uGrad = Function.minus(uGrad, Function.multiply(weight_vjk, Vjk));

                    // gradient of i
                    double weight_i = 1.0 - Function.sigmoidFunction(x_uik);
                    double[] iGrad = Function.multiply(weight_i, uObj.getFactors());

                    // gradient of j
                    double weight_j = - (1.0 - Function.sigmoidFunction(x_ukj));
                    double[] jGrad = Function.multiply(weight_j, uObj.getFactors());

                    // gradient of k
                    double weight_k = - (1.0 - Function.sigmoidFunction(x_uik)) + (1.0 - Function.sigmoidFunction(x_ukj));
                    double[] kGrad = Function.multiply(weight_k, uObj.getFactors());

                    double k_biasGrad = weight_k; // gradient of bias k
                    double i_biasGrad = weight_i; // gradient of bias i
                    double j_biasGrad = weight_j; // gradient of bias j

                    // update parameters
                    // update user
                    double[] uVec = Function.minus(uObj.getFactors(), Function.multiply(learningRate, uGrad));
                    uObj.setFactors(uVec);

                    // update i
                    double[] iVec = Function.minus(iObj.getFactors(), Function.multiply(learningRate, iGrad));
                    iObj.setFactors(iVec);

                    // update j
                    double[] jVec = Function.minus(jObj.getFactors(), Function.multiply(learningRate, jGrad));
                    jObj.setFactors(jVec);

                    // update k
                    double[] kVec = Function.minus(kObj.getFactors(), Function.multiply(learningRate, kGrad));
                    kObj.setFactors(kVec);

                    // update bias
                    kObj.setBias(kObj.getBias() - learningRate * k_biasGrad);
                    iObj.setBias(iObj.getBias() - learningRate * i_biasGrad);
                    jObj.setBias(jObj.getBias() - learningRate * j_biasGrad);

                    System.out.println("After:\t" + getAUC(uObj, iObj, jObj, kObj));
                }
            }
        }
    }

    private double getAUC(UserObject uObj, VenueObject iObj, VenueObject jObj, VenueObject kObj) {
        double x_ui = Function.innerProduct(uObj.getFactors(), iObj.getFactors()) + iObj.getBias();
        double x_uk = Function.innerProduct(uObj.getFactors(), kObj.getFactors()) + kObj.getBias();
        double x_uj = Function.innerProduct(uObj.getFactors(), jObj.getFactors()) + jObj.getBias();

        double x_uik = x_ui - x_uk;
        double x_ukj = x_uk - x_uj;

        double auc = Math.log(Function.sigmoidFunction(x_uik)) + Math.log(Function.sigmoidFunction(x_ukj));

        return auc;
    }

    public void writeModel(String filename) throws IOException {

        ArrayList<String> result = new ArrayList<>();
        // parameters
        String parameters = "k=" + k + ";lambda_u=" + params.getLambda_u() + ";lambda_v=" + params.getLambda_v() +
                "lambda_b=" + params.getLambda_b();
        result.add(parameters);

        // user
        result.add("users:");
        for (String uId : userMap.keySet()) {
            StringBuffer sb = new StringBuffer();
            sb.append(uId + " ");
            UserObject uo = userMap.get(uId);
            sb.append(Arrays.toString(uo.getFactors()));
            result.add(sb.toString());
        }

        // venue
        result.add("venues:");
        for (String vId : venueMap.keySet()) {
            StringBuffer sb = new StringBuffer();
            sb.append(vId + " ");
            VenueObject vo = venueMap.get(vId);
            sb.append(Arrays.toString(vo.getFactors()));
            result.add(sb.toString());
        }

        Utils.writeFile(result, filename);
    }
}