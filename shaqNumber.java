import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import java.io.*;


public class shaqNumber {


    public static void main(String[] args) throws IOException {


        bbrefGraph b = new bbrefGraph();
        ArrayList<String> finalPath = new ArrayList<String>();

        System.out.println("The Shaq number of a basketball player is the number of degrees of separation he has from Shaq.\n" +
                "The higher the Shaq number, the greater the separation from Shaq the basketball player is.");
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter desired basketball player");
        String player = myObj.nextLine();  // Read user input
        System.out.println("Desired basketball player is: " + player);  // Output user input

        finalPath = b.bfsSearch(player, "Shaquille O'Neal");

        System.out.println(Arrays.toString(finalPath.toArray()));





    }

}
