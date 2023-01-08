import java.util.Map.Entry;

public class Operation extends GlobalENV{
    public static void Work() {
        for(Company C : Companies) {
            int UnitsToProduce = 0;
            for(Employee E : C.Employees) {
                UnitsToProduce += E.Energy;
                E.Energy = 0;
            }

            if(C.ProductName.equals("B")) {
                int tempUnitsToProduce = C.RawMaterials / BDependencyOnA;
                if(tempUnitsToProduce < UnitsToProduce) UnitsToProduce = tempUnitsToProduce;
                if(UnitsToProduce < BDependencyOnA) UnitsToProduce = 0;
                else C.RawMaterials -= (UnitsToProduce * BDependencyOnA);
            }

            C.PreviousUnitsProduced = UnitsToProduce;
            double Price = C.PreviousPrice * C.PriceMultiplier;
            C.PreviousPrice = Price;

            OfferList.Offer F = new OfferList.Offer();
            F.CompanyID = C.ID;
            F.Price = Price;
            F.Product = C.ProductName;
            F.UnitsAvailable = UnitsToProduce;

            offerList.add(F);
        }
    }

    public static void Demand() {
        
    }

    public static void Revenue() {
        
    }

    public static Entry<Double, Integer> Purchase(String Product, int UnitsToPurchase, double ToSpend) { // Returns Key: SpentMoney, Value: UnitsBought
        return null; // Under construction
    }

    public static Entry<Double, Integer> Purchase(String Product, double ToSpend) { // Returns Key: SpentMoney, Value: UnitsBought
        return null; // Under construction
    }
}
