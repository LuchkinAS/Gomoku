package user.View;

public class IMG {
    //settings
    private String eugene = "Eugene";
    private String arseny = "Arseny";
    //private String user = "Eugene";
    private String user = "Arseny";

    //constants for eugene
    private final String imgNullE = "/Users/Eugene/Documents/University/Java/Gomoku/sourses/null.png";
    private final String imgZeroE = "/Users/Eugene/Documents/University/Java/Gomoku/sourses/zero.png";
    private final String imgCrossE = "/Users/Eugene/Documents/University/Java/Gomoku/sourses/cross.png";

    //constants for arseny
    private final String imgNullA = "C:\\Users\\luchk\\Downloads\\Revision 11\\Gomoku\\sourses\\null.png";
    private final String imgZeroA = "C:\\Users\\luchk\\Downloads\\Revision 11\\Gomoku\\sourses\\zero.png";
    private final String imgCrossA = "C:\\Users\\luchk\\Downloads\\Revision 11\\Gomoku\\sourses\\cross.png";

    public void IMG() { }

    public String getNullIMGLink() {
        if (this.user.equals(this.eugene)) {
            return this.imgNullE;
        }
        else {
            return this.imgNullA;
        }
    }

    public String getZeroIMGLink() {
        if (this.user.equals(this.eugene)) {
            return this.imgZeroE;
        }
        else {
            return this.imgZeroA;
        }
    }

    public String getCrossIMGLink() {
        if (this.user.equals(this.eugene)) {
            return this.imgCrossE;
        }
        else {
            return this.imgCrossA;
        }
    }
}
