import java.util.Map.Entry;

public class Operation extends GlobalENV{
    public static void Work() {
        int ACorps = 0;
        AverageProductAPrice = 0;
        AverageProductBPrice = 0;
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
            }else ACorps++;

            C.PreviousUnitsProduced = UnitsToProduce;
            double Price = C.PreviousPrice * C.PriceMultiplier;
            if(C.ProductName.equals("B")) AverageProductBPrice += Price;
            else AverageProductAPrice += Price;
            C.PreviousPrice = Price;

            OfferList.Offer F = new OfferList.Offer();
            F.CompanyID = C.ID;
            F.Price = Price;
            F.Product = C.ProductName;
            F.UnitsAvailable = UnitsToProduce;

            offerList.add(F);
        }

        AverageProductAPrice /= ACorps;
        AverageProductBPrice /= (NumberOfCompanies - ACorps);
    }

    public static void Demand() {
        for(Company C : Companies) {
            if(C.ProductName.equals("B")) {
                int UnitsToPurchase = BDependencyOnA * C.PreviousUnitsProduced;
                if(UnitsToPurchase / RawMaterials > 0.25) {
                    Entry<Double, Integer> RawMaterialPurchaseProcess = Purchase("A", UnitsToPurchase, C.Wealth);
                    C.RawMaterials += RawMaterialPurchaseProcess.getValue();
                    C.Wealth += RawMaterialPurchaseProcess.getKey();
                }
            }

            for(Employee E : C.Employees) {
                int UpdatedEnergy = 0;
                if(E.Wealth > 0) {
                    if(AverageProductAPrice * 5 < C.Salary) {
                        Entry<Double, Integer> APurchase = Purchase("A", E.FoodConsumptionFactor * E.Salary);
                        E.Wealth -= APurchase.getKey();
                        E.Salary -= APurchase.getKey();
                        UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();

                        if(E.Salary > 0) {
                            Entry<Double, Integer> BPurchase = Purchase("B", E.Salary);
                            E.Wealth -= BPurchase.getKey();
                            E.Salary -= BPurchase.getKey();
                            UpdatedEnergy += ProductBEnergyMultiplier * BPurchase.getValue();
                            E.FearFactor -= R.nextDouble(0.05, 0.15);
                        }else {
                            E.FoodConsumptionFactor += (E.FoodConsumptionFactor * E.FearFactor);
                        }
                    } else {
                        double tempWealthRatio = (1-((AverageProductAPrice * 5)/E.Wealth));
                        Entry<Double, Integer> APurchase = Purchase("A", tempWealthRatio * E.Wealth);
                        E.Wealth -= APurchase.getKey();
                        E.Salary -= APurchase.getKey();
                        UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();
                        E.FearFactor += R.nextDouble(tempWealthRatio / 10, tempWealthRatio);
                    }
                }
                
                E.Energy = UpdatedEnergy;
            }
        }
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
