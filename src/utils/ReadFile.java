package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * All functions are used to read input files
 * @author tndoan
 *
 */
public class ReadFile {

    /**
     * each line has the format
     * <userId> <venueId>:<numCks> <venueId>:<numCks> <venueId>:<numCks> <venueId>:<numCks> ....
     * @param filename the name of file
     * @return	hashmap whose key is user id and value is map (key venue id, value is # of cks between user and venue)
     */
    public static HashMap<String, HashMap<String, Integer>> readNumCksFile(String filename){
        HashMap<String, HashMap<String, Integer>> result = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            result = new HashMap<>();
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] comp = sCurrentLine.split(" ");
                String userId = comp[0];

                HashMap<String, Integer> map = new HashMap<>();
                for (int i = 1; i < comp.length; i++){
                    String[] c = comp[i].split(":");
                    String venueId = c[0];
                    int numCks = Integer.parseInt(c[1]);
                    map.put(venueId, numCks);
                }

                result.put(userId, map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * each line has the format
     * <venueId> <venueId_1> <venueId_2> <venueId_3> ...
     * <venueId_1> <venueId_2> <venueId_3> ... are neighbors of <venueId>
     * @param filename the name of file
     * @return
     */
    public static HashMap<String, ArrayList<String>> readNeighbors(String filename){
        HashMap<String, ArrayList<String>> result = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            result = new HashMap<>();
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] comp = sCurrentLine.split(" ");
                String vId = comp[0];

                ArrayList<String> list = new ArrayList<>();
                for (int i = 1; i < comp.length; i++)
                    list.add(comp[i]);

                result.put(vId, list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * each line has format
     * <id> ?
     * if we dont know the location of user or venue whose id is in this line. Or, the format is
     * <id> lat,lng
     * is the latitude and longitude of home location of user
     * @param filename
     * @return
     */
    public static HashMap<String, String> readLocation(String filename){
        HashMap<String, String> result = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            result = new HashMap<>();
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] comp = sCurrentLine.split(" ");
                String id = comp[0];
                String info = comp[1];

                result.put(id, info);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * each line has the format
     * <userId> <userId_1> <userId_2> <userId_3> ...
     * <userId_1> <userId_2> <userId_3> ... are friends of <userId>
     * @param filename the name of file
     * @return
     */
    public static HashMap<String, ArrayList<String>> readFriendship(String filename) {
        return readNeighbors(filename); // they have the same format
    }
}
