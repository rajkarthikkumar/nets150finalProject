import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class betweenessVisual {
    public static void main(String[] args) throws IOException {


        bbrefGraph b = new bbrefGraph();
        ArrayList<Player> playerRoster = b.createEntireRoster();
        System.out.println("Roster created");

//        int[][] adjM = new int[playerRoster.size()][playerRoster.size()];
//        Scanner input = new Scanner (new File("adjM.txt"));
//        String line = "";
//        for(int j = 0; j < playerRoster.size(); j++) {
//            line = input.nextLine();
//            String[] values = line.split(",");
//            for (int i = 0; i < values.length; i++) {
//                adjM[i][j] = (Integer.parseInt(values[i]));
//            }
//        }
//
//        System.out.println("Matrix filled");

        // the centrality function was used to create the eigenvector.txt file
        //b.centrality(adjM);

        double[] eigenvector = new double[playerRoster.size()];
        Scanner input1 = new Scanner (new File("eigenvector.txt"));
        for(int i = 0; i < playerRoster.size(); i++) {
            eigenvector[i] = input1.nextDouble();
        }
        System.out.println("Eigenvector array filled");

        int count = 0;
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        for(int i = 0; i < playerRoster.size(); i++) {
            if(eigenvector[i] >= 0 && eigenvector[i] < 9E-8) {
                count++;
            }
            else if(eigenvector[i] > 9E-8 && eigenvector[i] < 9E-6) {
                count1++;
            }
            else if(eigenvector[i] > 9E-6 && eigenvector[i] < 9E-5) {
                count2++;
            }
            else if(eigenvector[i] > 9E-5 && eigenvector[i] < 9E-4) {
                count3++;
            }
            else if(eigenvector[i] > 9E-4) {
                count4++;
            }
        }

        System.out.println(count);
        System.out.println(count1);
        System.out.println(count2);
        System.out.println(count3);
        System.out.println(count4);



    }
}
