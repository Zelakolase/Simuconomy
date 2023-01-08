import java.util.ArrayList;

public class OfferList {
    public static ArrayList<Offer> List = new ArrayList<>();
    public static int HighestUnit;

    public static class Offer {
        public int CompanyID;
        public double Price;
        public String Product;
        public int UnitsAvailable;
    }
}
