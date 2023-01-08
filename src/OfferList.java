import java.util.ArrayList;

public class OfferList {
    public ArrayList<Offer> List = new ArrayList<>();
    public static int HighestUnit = 0;

    public static class Offer {
        public int CompanyID;
        public double Price;
        public String Product;
        public int UnitsAvailable;

        @Override
        public String toString() {
            StringBuilder SB = new StringBuilder();
            SB.append("CompanyID: "+CompanyID);
            SB.append(", Product: "+Product);
            SB.append(", Price: "+Price);
            SB.append(", UnitsAvailable: "+UnitsAvailable);

            return SB.toString();
        }
    }

    public void add(Offer F) {
        if(F.UnitsAvailable > HighestUnit) HighestUnit = F.UnitsAvailable;
        List.add(F);
    }

    @Override
    public String toString() {
        StringBuilder SB = new StringBuilder();
        for(Offer F : List) {
            SB.append(F);
        }

        return SB.toString();
    }
}
