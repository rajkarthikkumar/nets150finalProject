NETS 150 – Homework 5 – Project User Manual

This project is a special variation of the Six Degrees of Kevin Bacon problem. Instead of figuring out the degrees of separation between an actor and Kevin Bacon, we figure out the distance between the greatest center of all time, Shaquille O’Neal, and an NBA player. The higher the Shaq number, the greater the separation from Shaq the basketball player is.

In order to start the project, we needed to construct a graph of every single player that has ever played in the NBA. Each vertex in the graph is defined by an NBA player and an edge exists between two vertices if they have played on the same team at some point. We then constructed an adjacency matrix from this graph. All player information was scraped from https://www.basketball-reference.com. The adjacency matrix was saved into the txt file adjM.txt.

There are two files in this project of significance to the user: shaqNumber.java and betweennessVisual.java. 

shaqNumber.java: This file is the main file for the Shaq number calculation. The program requests the user to input a source and destination. For the example, we used Shaquille O’Neal as our source and the greatest shooting guard in the league, Devin Booker, as our destination. The program takes around 20 seconds to create the roster of all players in the NBA and fill out the adjacency matrix from AdjM.txt. It will then output the path between the destination and the source, along with the time of the BFS search. After this point, the program takes around three minutes to conduct a BFS search on every single player that has ever played in the league in order to find the maximum path from the source, along with the average length of the path from the source.

betweenessVisual.java: This file is used to visualize the Adjacency Matrix that was constructed. It calculates the normalized Eigenvectors of each node to give us a centrality value. The values are normalized to tell us the percentage of the time that a random walk would take us to a specific node. Using this information, we label each node into one of five groups and use it to color in our visualization.

Other files used: Player.java, bbrefGraph.java, AdjM.txt, eigenvector.txt

