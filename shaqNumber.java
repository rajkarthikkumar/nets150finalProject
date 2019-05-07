import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class shaqNumber {
    public static void main(String[] args) throws IOException {

        bbrefGraph b = new bbrefGraph();

        System.out.println("The Shaq number of a basketball player is the number of degrees of separation he has from Shaq.\n" +
                "The higher the Shaq number, the greater the separation from Shaq the basketball player is. " +
                "The full name of Shaq is \"Shaquille O'Neal\" Please be exact when you type");
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Enter desired basketball player source");
        String source = myObj.nextLine();  // Read user input
        String link1 = "";
        link1 = b.playerLink(source);
        if(!link1.equals("")) {
            System.out.println("Desired basketball player source is: " + source
                    + " with the link: " + link1);  // Output user input
        } else {
            System.out.println("Either the player does not exist or you did not provide the full spelling." +
                " Check Basketball Reference for the full name. Ex. Steph should be typed as Stephen Curry.");
            System.exit(0);
        }

        System.out.println("Enter desired basketball player destination");
        String destination = myObj.nextLine();  // Read user input
        String link2 = "";
        link2 = b.playerLink(destination);
        if(!link2.equals("")) {
            System.out.println("Desired basketball player destination is: " + destination
                    + " with the link: " + link2);  // Output user input
        } else {
            System.out.println("Either the player does not exist or you did not provide the full spelling." +
                " Check Basketball Reference for the full name. Ex. Steph should be typed as Stephen Curry.");
            System.exit(0);
        }
        System.out.println("Constructing roster and reading in matrix");

        //the createMatrix function was used to create the adjM.txt file (adjacency matrix)
        //b.createMatrix();

        ArrayList<Player> playerRoster = b.createEntireRoster();
        int[] playerArr = new int[playerRoster.size()];
        System.out.println("Roster created");
        int[][] adjM = new int[playerRoster.size()][playerRoster.size()];

        Scanner input = new Scanner (new File("adjM.txt"));
        String line = "";
        for(int j = 0; j < playerRoster.size(); j++) {
            line = input.nextLine();
            String[] values = line.split(",");
            for (int i = 0; i < values.length; i++) {
                adjM[i][j] = (Integer.parseInt(values[i]));
            }
        }


        System.out.println("Matrix filled");

        // BFS search
        long startTime = System.nanoTime();
        List<Integer> shortestPath = b.bfsSearch(adjM, source, destination);
        int distance = shortestPath.size() - 1;
        int s = 0;
        int dest = 0;
        for(int i = 0; i < playerRoster.size(); i++) {
            if(source.equals(playerRoster.get(i).getName())) {
                s = i;
            }
            if(destination.equals(playerRoster.get(i).getName())) {
                dest = i;
            }
        }
        System.out.print("The path between the source " + playerRoster.get(s).getName() +
                " and destination " + playerRoster.get(dest).getName() + " is [");
        for(int i = 0; i < shortestPath.size(); i++) {
            System.out.print(playerRoster.get(shortestPath.get(i)).getName());
            if(i != shortestPath.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("The distance between the source " + playerRoster.get(s).getName() +
                " and destination " + playerRoster.get(dest).getName() + " is " + distance);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double seconds = (double) elapsedTime / 1_000_000_000.0;
        System.out.println("The BFS search took " + seconds + " seconds");

        // longest path calculation
        System.out.println("Determining the longest path from " + source + " as well as the average distance.");
        for (int i = 0; i < playerRoster.size(); i++) {
            playerArr[i] = b.bfsSearch(adjM, source, playerRoster.get(i).getName()).size() - 1;
        }
        int max = Integer.MIN_VALUE;
        int index = -1;
        for(int i = 0; i < playerArr.length; i ++) {
            if (max < playerArr[i]) {
                max = playerArr[i];
                index = i;
            }
        }
        shortestPath = b.bfsSearch(adjM, source, playerRoster.get(index).getName());
        destination = playerRoster.get(index).getName();
        s = 0;
        dest = 0;
        for(int i = 0; i < playerRoster.size(); i++) {
            if(source.equals(playerRoster.get(i).getName())) {
                s = i;
            }
            if(destination.equals(playerRoster.get(i).getName())) {
                dest = i;
            }
        }
        System.out.print("The max path from the source " + playerRoster.get(s).getName() +
                " is [");
        for(int i = 0; i < shortestPath.size(); i++) {
            System.out.print(playerRoster.get(shortestPath.get(i)).getName());
            if(i != shortestPath.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        distance = shortestPath.size() - 1;
        System.out.println("The max " + source +  " number is: " + max + " and belongs to: "
                + playerRoster.get(shortestPath.get(distance)).getName());

        // average calculation
        int sum = 0;
        double average;
        for(int i=0; i < playerArr.length; i++){
            sum = sum + playerArr[i];
        }
        average = (double)sum/playerArr.length;
        System.out.println("Average distance from " + source +  " is: " + average);


    }
}
