import org.apache.commons.math3.geometry.euclidean.threed.PLYParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.*;
import java.io.*;
import org.jblas.*;

public class bbrefGraph {

    private Document doc;
    private ArrayList<Player> rosterOfAllPlayers;


    // takes player name and returns link
    public String playerLink(String player) {
        String[] firstLast = new String[2];
        try {
            firstLast = player.split(" "); // firstLast[1] gets last name
            if(firstLast[1].substring(0,1).equals("X")) {
                return "";
            }
        } catch (ArrayIndexOutOfBoundsException exception){
            System.out.println("Please type in the player's full name.");
            return "";
        }
        try {
            doc = Jsoup.connect("https://www.basketball-reference.com/players/"
                    + firstLast[1].substring(0,1).toLowerCase()).get();
        } catch (IOException e){
            e.printStackTrace();
        }
        Elements table = doc.select("table");
        Elements rows = table.select("tr");
        for (Element row : rows) {
            if(row.select("tr > th > a").text().contains(player)) { // Retired Players
                return row.select("tr > th > a[href]").attr("href");
            }
            if(row.select("tr > th > strong > a").text().contains(player)) { // Active Players
                return row.select("tr > th > strong > a[href]").attr("href");
            }
        }
        return "";
    }


    private static void removeDuplicates(ArrayList<?> arr) {
        for(int i = 0; i < arr.size(); i++) {
            for(int j = i + 1; j < arr.size(); j++) {
                if(arr.get(i).equals(arr.get(j))){
                    arr.remove(j);
                    j--;
                }
            }
        }
    }

    // returns Arraylist of all the players a particular player has played with
    private ArrayList<Player> playerRoster(Player player) {
        ArrayList<String> rosterLinks = new ArrayList<String>();
        ArrayList<Player> players = new ArrayList<Player>();
        try {
            doc = Jsoup.connect("https://www.basketball-reference.com/" + playerLink(player.getName())).get();
        } catch (IOException e){
            e.printStackTrace();
        }
        Element table = doc.selectFirst("table");
        Elements rows = table.select("tr");
        for (Element row : rows){
            String link = row.select("[data-stat='team_id'] > a[href]").attr("href");
            // for some reason, the 2016 Memphis Griz page is broken
            if(!link.equals("") && !link.equals("/teams/MEM/2016.html")) {
                rosterLinks.add(link);
            }
        }
        for(int i = 0; i < rosterLinks.size(); i++) {
            try {
                doc = Jsoup.connect("https://www.basketball-reference.com/" + rosterLinks.get(i)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element roster = doc.selectFirst("table");
            Elements rPlayers = roster.select("tr");
            for (Element row : rPlayers){
                Player generic = new Player("","");
                String play = row.select("[data-stat='player'] > a").text();
                String link = row.select("[data-stat='player'] > a[href]").attr("href");
                if(!play.equals("") && !play.equals(player.getName())) {
                    generic.setName(play);
                    generic.setLink(link);
                    players.add(generic);
                }
            }
        }
        // This is a way of getting rid of duplicates in the array that keeps order
        removeDuplicates(players);
        return players;
    }


    public ArrayList<Player> createEntireRoster() {
        rosterOfAllPlayers = new ArrayList<Player>();
        // there are no players with a last name that start with X
        String[] alphabet = "abcdefghijklmnopqrstuvwyz".split("");

        for(int i = 0; i < alphabet.length; i++) {
            try {
                doc = Jsoup.connect("https://www.basketball-reference.com/players/"
                        + alphabet[i]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements table = doc.select("table");
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Player generic = new Player("", "");
                if (!row.select("tr > th > a").text().equals("")) { // get rid of white spaces
                    generic.setName(row.select("tr > th > a").text());
                    generic.setLink(row.select("tr > th > a[href]").attr("href"));
                    rosterOfAllPlayers.add(generic); // retired players
                }
                if (!row.select("tr > th > strong > a").text().equals("")) { // get rid of white spaces
                    generic.setName(row.select("tr > th > strong > a").text());
                    generic.setLink(row.select("tr > th > strong > a[href]").attr("href"));
                    rosterOfAllPlayers.add(generic); // active players
                }
            }
        }
        return rosterOfAllPlayers;
    }

    public int[][] createMatrix() {
        createEntireRoster();
        int[][] adjM = new int[rosterOfAllPlayers.size()][rosterOfAllPlayers.size()];
        for(int i = 0; i < adjM.length; i++) {
            for(int j = 0; j < adjM[0].length; j++) {
                adjM[i][j] = 0;
            }
        }

        for(int i = 0; i < rosterOfAllPlayers.size(); i++) {
            System.out.println(i + " : Checking roster for " + rosterOfAllPlayers.get(i).getName());
            ArrayList<Player> roster = playerRoster(rosterOfAllPlayers.get(i));
            for(int j = 0; j < rosterOfAllPlayers.size(); j++) {
                if(roster.contains(rosterOfAllPlayers.get(j))) {
                    adjM[i][j] = 1;
                }
            }
        }

        // save adjM
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < adjM.length; i++) //for each row
        {
            for(int j = 0; j < adjM.length; j++) //for each column
            {
                builder.append(adjM[i][j]+""); //append to the output string
                if(j < adjM.length - 1) //if this is not the last row element
                    builder.append(","); //then add comma (if you don't like commas you can use spaces)
            }
            builder.append("\n"); //append new line at the end of the row
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("adjM" + ".txt"));
            writer.write(builder.toString()); //save the string representation of the board
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return adjM;
    }




    // bfs implementation
    public List<Integer> bfsSearch(int[][] adjM, String source, String destination) {
        int s = 0;
        int dest = 0;
        for(int i = 0; i < rosterOfAllPlayers.size(); i++) {
            if(source.equals(rosterOfAllPlayers.get(i).getName())) {
                s = i;
            }
            if(destination.equals(rosterOfAllPlayers.get(i).getName())) {
                dest = i;
            }
        }

        ArrayList<Player> path = new ArrayList<>();
        boolean[] bools = new boolean[adjM.length];
        int[] prev = new int[adjM.length];

        List<Integer> shortestPath = new LinkedList<Integer>();
        List<Integer> path1 = new LinkedList<Integer>();

        boolean connected = false;
        bools[s] = true;
        path1.add(0, s);
        int node = 0;

        while(!path1.isEmpty()) {
            int e = path1.get(0);
            node = path1.remove(0);
            if (e == dest) {
                break;
            }
            else {
                for (int i = 0; i < adjM.length; i++) {
                    if (!bools[i] && adjM[node][i] == 1) {
                        bools[i] = true;
                        path1.add(i);
                        prev[i] = node;
                    }
                }
            }

        }
        while (node != s) {
            shortestPath.add(0, node);
            node = prev[node];
        }
        shortestPath.add(0, s);

        return shortestPath;
    }


    // EigenVector Centrality
    public List<Double> centrality(int[][] adjM) {
        double[][] copied = new double[adjM.length][adjM.length];
        for(int i = 0; i < adjM.length; i++) {
            for (int j = 0; j < adjM.length; j++) {
                copied[i][j] = adjM[i][j];
            }
        }
        DoubleMatrix matrix = new DoubleMatrix(copied);

        ComplexDoubleMatrix eigenvalues = Eigen.eigenvalues(matrix);
        List<Double> principalEigenvector = getPrincipalEigenvector(matrix);
        // the values are normalized to tell us the % of time that a random walk would take you to this node:
        List<Double> result = normalised(principalEigenvector);
        System.out.println("normalisedPrincipalEigenvector = " + result);

        try {
            BufferedWriter outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter("eigenvector.txt"));
            for (int i = 0; i < result.size(); i++) {
                outputWriter.write(Double.toString(result.get(i)));
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
            }
        catch (IOException e) {
            e.printStackTrace();
        }

        return principalEigenvector;
    }

    private static int getMaxIndex(DoubleMatrix matrix) {
        ComplexDouble[] doubleMatrix = Eigen.eigenvalues(matrix).toArray();
        int maxIndex = 0;
        for (int i = 0; i < doubleMatrix.length; i++){
            double newnumber = doubleMatrix[i].abs();
            if ((newnumber > doubleMatrix[maxIndex].abs())){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static List<Double> getPrincipalEigenvector(DoubleMatrix matrix) {
        int maxIndex = getMaxIndex(matrix);
        ComplexDoubleMatrix eigenVectors = Eigen.eigenvectors(matrix)[0];
        return getEigenVector(eigenVectors, maxIndex);

    }

    private static List<Double> normalised(List<Double> principalEigenvector) {
        double total = sum(principalEigenvector);
        List<Double> normalisedValues = new ArrayList<Double>();
        for (Double aDouble : principalEigenvector) {
            normalisedValues.add(aDouble / total);
        }
        return normalisedValues;
    }

    private static List<Double> getEigenVector(ComplexDoubleMatrix eigenvector, int columnId) {
        ComplexDoubleMatrix column = eigenvector.getColumn(columnId);

        List<Double> values = new ArrayList<Double>();
        for (ComplexDouble value : column.toArray()) {
            values.add(value.abs()  );
        }
        return values;
    }

    private static double sum(List<Double> principalEigenvector) {
        double total = 0;
        for (Double aDouble : principalEigenvector) {
            total += aDouble;
        }
        return total;
    }


}
