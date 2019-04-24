import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.io.*;


public class bbrefGraph {

    private Document doc;

    // takes player name and returns link
    private String playerLink(String player) {
        String[] firstLast = player.split(" "); // firstLast[1] gets last name
        if(firstLast[1].substring(0,1).equals("X")) {
            System.out.println("PLAYER DNE");
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

    private ArrayList<String> playerRoster(String player) {
        ArrayList<String> rosterLinks = new ArrayList<String>();
        ArrayList<String> players = new ArrayList<String>();
        try {
            doc = Jsoup.connect("https://www.basketball-reference.com/" + playerLink(player)).get();
        } catch (IOException e){
            e.printStackTrace();
        }
        Element table = doc.selectFirst("table");
        Elements rows = table.select("tr");
        for (Element row : rows){
            String link = row.select("[data-stat='team_id'] > a[href]").attr("href");
            if(!link.equals("")) {
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
                String play = row.select("[data-stat='player'] > a").text();
                if(!play.equals("") && !play.equals(player)) {
                    players.add(play);
                }
            }
        }
        return players;
    }

    // bfs implementation
    public ArrayList<String> bfsSearch(String source, String destination) {
        ArrayList<String> path = new ArrayList<>();
        try {
            doc = Jsoup.connect("https://www.basketball-reference.com/" + playerLink(source)).get();
        } catch (IOException e){
            e.printStackTrace();
        }

        // case if shaq num = 0
        if(source.equals(destination)) {
            System.out.println("You entered the same source and destination");
            return null;
        }


        // case if shaq num = 1
        for(String player : playerRoster(source)) {
            if(destination.equals(player)) { // case if shaq num > 1
                path.add(source);
                path.add(destination);
                return path;
            }
        }

        path.add(source);
        for(String player : playerRoster(source)) {
            for(String subplayer : playerRoster(player)) {
                if(destination.equals(subplayer)) {
                    path.add(player);
                    path.add(destination);
                    return path;
                }
            }
        }
        return path;
    }






}
