import java.util.AbstractMap;
import java.util.Map.Entry;

public class Operation extends GlobalENV{
    public static void Work() {
        offerList.List.clear();
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
                    if(AverageProductAPrice * 20 < C.Salary) {
                        Entry<Double, Integer> APurchase = Purchase("A", E.FoodConsumptionFactor * E.Salary);
                        E.Wealth -= APurchase.getKey();
                        E.Salary -= APurchase.getKey();
                        UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();

                        if(E.Salary > 0) {
                            Entry<Double, Integer> BPurchase = Purchase("B", E.Salary);
                            E.Wealth -= BPurchase.getKey();
                            E.Salary -= BPurchase.getKey();
                            UpdatedEnergy += ProductBEnergyMultiplier * BPurchase.getValue();
                            E.FearFactor -= R.nextDouble(0.10, 0.30);
                        }else {
                            E.FoodConsumptionFactor += (E.FoodConsumptionFactor * E.FearFactor);
                        }
                    } else {
                        double tempWealthRatio = (1-((AverageProductAPrice * 20)/E.Wealth));
                        Entry<Double, Integer> APurchase = Purchase("A", tempWealthRatio * E.Wealth);
                        E.Wealth -= APurchase.getKey();
                        E.Salary -= APurchase.getKey();
                        UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();
                        if(tempWealthRatio > 0) E.FearFactor += R.nextDouble(tempWealthRatio / 10, tempWealthRatio);
                    }
                }
                
                E.Energy = UpdatedEnergy;
                if(E.FearFactor < 0) E.FearFactor = LowestFearFactor;
            }
        }
    }

    public static void Revenue() {
        for(Company C : Companies) {
            OfferList.Offer F = offerList.List.get(C.ID);
            boolean RPI = F.UnitsAvailable <= 0 ? true : false;
            boolean RPD = (C.PreviousUnitsProduced > 0 && F.UnitsAvailable/C.PreviousUnitsProduced > 0.25) ? true : false;
            boolean TMP = false;
            F.UnitsAvailable = F.UnitsAvailable < 0 ? 0 : F.UnitsAvailable;
            double Revenue = (C.PreviousUnitsProduced - F.UnitsAvailable) * F.Price;

            while(Revenue < (C.Salary * EmployeesPerCompany)) {
                RPI = true;
                C.Salary -= (C.Salary * (C.GreedMultiplier/100));
                Revenue += C.Wealth;
                TMP = true;
            }

            for(Employee E : C.Employees) {
                E.Wealth += C.Salary;
                E.Salary = C.Salary;
                Revenue -= C.Salary;
            }

            if(RPI) {
                C.PriceMultiplier += R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/50);
                if(R.nextDouble() < C.GreedMultiplier) C.Salary -= (C.Salary * R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/50));
            }else if(RPD) {
                C.PriceMultiplier += R.nextDouble((1-C.GreedMultiplier)/100, (1-C.GreedMultiplier)/50);
                if(R.nextDouble() < 1-C.GreedMultiplier) C.Salary -= (C.Salary * R.nextDouble((1-C.GreedMultiplier)/100, (1-C.GreedMultiplier)/50));
                else C.Salary -= (C.Salary * R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/50));
            }

            if(C.PriceMultiplier < 0) C.PriceMultiplier = 1;
            if(TMP && Revenue > 0) C.Wealth += Revenue;
            else if(!TMP && Revenue > 0) C.Wealth = Revenue;
        }
    }

    public static Entry<Double, Integer> Purchase(String Product, int UnitsToPurchase, double ToSpend) { // Returns Key: SpentMoney, Value: UnitsBought
        Entry<Double, Integer> output = new AbstractMap.SimpleEntry<Double, Integer>(0d, 0);
        int OptimalIndex = 0, OptimalUnits = 0;
        if(offerList.HighestUnit == 0) {
            for(OfferList.Offer F : offerList.List) {
                F.UnitsAvailable = -1;
            }
            offerList.HighestUnit = -1;
        }
        else if(offerList.HighestUnit > 0) {
            for(OfferList.Offer F : offerList.List) {
                int MaxPurchasableUnits = (int) (ToSpend / F.Price);
                MaxPurchasableUnits = MaxPurchasableUnits > F.UnitsAvailable ? F.UnitsAvailable : MaxPurchasableUnits;
                if(OptimalUnits < MaxPurchasableUnits) {
                    OptimalIndex = F.CompanyID;
                    OptimalUnits = MaxPurchasableUnits;
                }
            }

            if(OptimalUnits == 0) output = Purchase(Product, ToSpend);
            else if(OptimalUnits >= UnitsToPurchase) {
                offerList.List.get(OptimalIndex).UnitsAvailable -= UnitsToPurchase;
                output = new AbstractMap.SimpleEntry<Double, Integer>(offerList.List.get(OptimalIndex).Price * UnitsToPurchase, UnitsToPurchase);
            }else {
                double SpentMoney = 0;
                int UnitsBought = 0;
                offerList.List.get(OptimalIndex).UnitsAvailable -= OptimalUnits;
                UnitsBought += OptimalUnits;
                SpentMoney += OptimalUnits * offerList.List.get(OptimalIndex).Price;
                UnitsToPurchase -= UnitsBought;
                ToSpend -= SpentMoney;
                Main: while(ToSpend > 0 && UnitsToPurchase > 0) {
                    Entry<Double, Integer> temp = Purchase(Product, ToSpend);
                    if(temp.getKey() == 0 || temp.getValue() == 0) break Main;
                    SpentMoney += temp.getKey();
                    UnitsBought += temp.getValue();
                    UnitsToPurchase -= temp.getValue();
                    ToSpend -= temp.getKey();
                }
                output = new AbstractMap.SimpleEntry<Double, Integer>(SpentMoney, UnitsBought);
            }
        }
        offerList.recalculate();
        return output;
    }

    public static Entry<Double, Integer> Purchase(String Product, double ToSpend) { // Returns Key: SpentMoney, Value: UnitsBought
        Entry<Double, Integer> output = new AbstractMap.SimpleEntry<Double, Integer>(0d, 0);
        if(offerList.HighestUnit == 0) {
            for(OfferList.Offer F : offerList.List) {
                F.UnitsAvailable = -1;
            }
            offerList.HighestUnit = -1;
        } else if(offerList.HighestUnit > 0) {
            int OptimalIndex = 0, OptimalUnits = 0;

            for(OfferList.Offer F : offerList.List) {
                int MaxPurchasableUnits = (int) (ToSpend / F.Price);
                MaxPurchasableUnits = MaxPurchasableUnits > F.UnitsAvailable ? F.UnitsAvailable : MaxPurchasableUnits;
                if(OptimalUnits < MaxPurchasableUnits) {
                    OptimalIndex = F.CompanyID;
                    OptimalUnits = MaxPurchasableUnits;
                }
            }

            offerList.List.get(OptimalIndex).UnitsAvailable -= OptimalUnits;
            output = new AbstractMap.SimpleEntry<Double, Integer>(OptimalUnits * offerList.List.get(OptimalIndex).Price, OptimalUnits);
        }
        offerList.recalculate();
        return output;
    }
}
