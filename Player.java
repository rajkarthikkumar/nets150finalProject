public class Player {

    private String name;
    private String link;

    public Player(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() { return name; }
    public String getLink() { return link; }

    public void setName(String name) { this.name = name; }
    public void setLink(String link) { this.link = link; }

    public void printPlayer(){
        System.out.println("Name: " + name + " Link: " + link);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if(!(o instanceof Player)) {
            return false;
        }
        Player p = (Player) o;
        return p.getLink().compareTo(getLink()) == 0 && p.getName().compareTo(getName()) == 0;
    }

}
