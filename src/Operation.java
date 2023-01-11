import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map.Entry;

public class Operation extends GlobalENV{
    public static void Work() {
        offerList.List.clear();
        AverageProductAPrice = 0;
        AverageProductBPrice = 0;
        double[] tempAPrices = new double[ACorps];
        double[] tempBPrices = new double[NumberOfCompanies - ACorps];
        int AInd = 0, BInd = 0;
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
            if(C.ProductName.equals("B")) { 
                tempBPrices[BInd] = Price;
                BInd++;
            }
            else { 
                tempAPrices[AInd] = Price;
                AInd++;
            }
            C.PreviousPrice = Price;

            OfferList.Offer F = new OfferList.Offer();
            F.CompanyID = C.ID;
            F.Price = Price;
            F.Product = C.ProductName;
            F.UnitsAvailable = UnitsToProduce;

            offerList.add(F);
        }
        Arrays.sort(tempAPrices);
        if (tempAPrices.length % 2 == 0) AverageProductAPrice = ((double)tempAPrices[tempAPrices.length/2] + (double)tempAPrices[tempAPrices.length/2 - 1])/2;
        else AverageProductAPrice = (double) tempAPrices[tempAPrices.length/2];
        Arrays.sort(tempBPrices);
        if (tempBPrices.length % 2 == 0) AverageProductBPrice = ((double)tempBPrices[tempBPrices.length/2] + (double)tempBPrices[tempBPrices.length/2 - 1])/2;
        else AverageProductBPrice = (double) tempBPrices[tempBPrices.length/2];
    }

    public static void Demand() {
        for(Company C : Companies) {
            if(C.ProductName.equals("B")) {
                int UnitsToPurchase = BDependencyOnA * C.PreviousUnitsProduced;
                if(UnitsToPurchase / RawMaterials > 0.25) {
                    Entry<Double, Integer> RawMaterialPurchaseProcess = Purchase("A", UnitsToPurchase, C.Wealth);
                    C.RawMaterials += RawMaterialPurchaseProcess.getValue();
                    C.Wealth -= RawMaterialPurchaseProcess.getKey();
                }
            }

            for(Employee E : C.Employees) {
                int UpdatedEnergy = 0;
                if(E.Wealth > 0) {
                    if(AverageProductAPrice * LowestFoodIntake < C.Salary) {
                        Entry<Double, Integer> APurchase = Purchase("A", E.AConsumptionFactor * E.Salary);
                        E.Wealth -= APurchase.getKey();
                        E.Salary -= APurchase.getKey();
                        UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();

                        if(E.Salary > 0) {
                            Entry<Double, Integer> BPurchase = Purchase("B", E.Salary);
                            E.Wealth -= BPurchase.getKey();
                            E.Salary -= BPurchase.getKey();
                            UpdatedEnergy += ProductBEnergyMultiplier * BPurchase.getValue();
                            E.FearFactor -= R.nextDouble(0.02, 0.1);
                        }else {
                            E.AConsumptionFactor += (E.AConsumptionFactor * E.FearFactor);
                        }
                    } else {
                        double tempWealthRatio = 1 - ((AverageProductAPrice * LowestFoodIntake) / E.Wealth);
                        if(tempWealthRatio > 1) tempWealthRatio = 1;
                        Entry<Double, Integer> APurchase = Purchase("A", tempWealthRatio * E.Wealth);
                        E.Wealth -= APurchase.getKey();
                        E.Salary -= APurchase.getKey();
                        UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();
                        if(tempWealthRatio > 0) E.FearFactor += R.nextDouble(tempWealthRatio / 10, tempWealthRatio);
                    }
                }
                
                E.Energy = UpdatedEnergy;
                if(E.FearFactor < 0) E.FearFactor = LowestFearFactor;
                if(E.AConsumptionFactor > 1) E.AConsumptionFactor = 1;
                if(E.AConsumptionFactor < 0) E.AConsumptionFactor = 0.25;
            }
        }
    }

    public static void Revenue() {
        for(Company C : Companies) {
            OfferList.Offer F = offerList.List.get(C.ID);
            boolean RPI = (F.UnitsAvailable <= 0 && C.PreviousUnitsProduced > 0) ? true : false; // Require Price Increment
            boolean RPD = (C.PreviousUnitsProduced > 0 && F.UnitsAvailable/C.PreviousUnitsProduced > 0.25) ? true : false; // Require Price Decrement
            boolean TMP = false;
            F.UnitsAvailable = F.UnitsAvailable < 0 ? 0 : F.UnitsAvailable;
            double Revenue = (C.PreviousUnitsProduced - F.UnitsAvailable) * F.Price;

            while(Revenue < (C.Salary * C.Employees.size())) {
                Revenue += C.Wealth;
                C.Salary -= (C.Salary * (C.GreedMultiplier/100));
                TMP = true;
            }

            for(Employee E : C.Employees) {
                E.Wealth += C.Salary;
                E.Salary = C.Salary;
                Revenue -= C.Salary;
            }

            if(TMP) C.PriceMultiplier += R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/25);

            if(RPI) {
                C.PriceMultiplier += R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/25);
                if(R.nextDouble() < C.GreedMultiplier) C.Salary -= (C.Salary * R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/25));
            }else if(RPD) {
                C.PriceMultiplier -= R.nextDouble((1-C.GreedMultiplier)/100, (1-C.GreedMultiplier)/25);
                if(R.nextDouble() < 1-C.GreedMultiplier) C.Salary += (C.Salary * R.nextDouble((1-C.GreedMultiplier)/100, (1-C.GreedMultiplier)/25));
                else C.Salary -= (C.Salary * R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/25));
            }

            if(C.PriceMultiplier < 0) C.PriceMultiplier = 0.85;
            if(TMP) C.Wealth = Revenue;
            else C.Wealth += Revenue;
            if(C.PreviousUnitsProduced <= 0)  {
                C.PreviousPrice = AverageProductAPrice;
                C.PriceMultiplier = 1;
            }

            int deltaEmployees = C.Employees.size() - EmployeesPerCompany;
            if(deltaEmployees > 0 && !(RPI || RPD)) C.Salary -= (C.Salary * R.nextDouble(C.GreedMultiplier/100, C.GreedMultiplier/25));
            else if(deltaEmployees < 0 && !(RPI || RPD)) C.Salary += (C.Salary * R.nextDouble((1-C.GreedMultiplier)/100, (1-C.GreedMultiplier)/25));
        }
    }

    public static void EmployeeTransfer() {
        double medianSalary = 0; 
        double[] allSalaries = new double[Companies.size()];
        int Ind = 0;
        for(Company C : Companies){ 
            allSalaries[Ind] = C.Salary;
            Ind++;
        }

        Arrays.sort(allSalaries);
        if (allSalaries.length % 2 == 0) medianSalary = ((double)allSalaries[allSalaries.length/2] + (double)allSalaries[allSalaries.length/2 - 1])/2;
        else medianSalary = (double) allSalaries[allSalaries.length/2];

        Main: for(int i = 0; i < NumberOfCompanies / 10; i++) {
            int fromCMPID = FindCompanyBySalaryRange(false, medianSalary);
            int toCMPID = FindCompanyBySalaryRange(true, medianSalary);
            Employee EMPtoTransfer = null;
            if(fromCMPID == -1 || toCMPID == -1) break Main;

            Inner: for(Company C : Companies) {
                if(C.ID == fromCMPID && C.Employees.size() > 0) {
                    int randIndex = R.nextInt(0, C.Employees.size());
                    EMPtoTransfer = C.Employees.get(randIndex);
                    C.Employees.remove(randIndex);
                    break Inner;
                }
            }

            Inner: for(Company C : Companies) {
                if(C.ID == toCMPID) {
                    if(EMPtoTransfer == null) {
                        EMPtoTransfer = new Employee();
                        EMPtoTransfer.Energy = Energy;
                        EMPtoTransfer.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                        EMPtoTransfer.AConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                        EMPtoTransfer.Salary = C.Salary;
                        EMPtoTransfer.Wealth = EmployeeWealth;
                    }else {
                        EMPtoTransfer.Salary = C.Salary;
                    }
                    C.Employees.add(EMPtoTransfer);
                    break Inner;
                }
            }
        }
    }

    public static void Expand() {
        int numOfNewCMPS = R.nextDouble() < 0.4 ? 1 : 0;
        int highestID = 0;
        for(Company C : Companies) {
            //if(C.PreviousUnitsProduced > (EmployeesPerCompany * Energy)) numOfNewCMPS++;
            if(highestID < C.ID) highestID = C.ID;
        }

        double[] allSalaries = new double[Companies.size()];
        int Ind = 0;
        double medianSalary = 0;
        for(Company C : Companies){ 
            allSalaries[Ind] = C.Salary;
            Ind++;
        }

        Arrays.sort(allSalaries);
        if (allSalaries.length % 2 == 0) medianSalary = ((double)allSalaries[allSalaries.length/2] + (double)allSalaries[allSalaries.length/2 - 1])/2;
        else medianSalary = (double) allSalaries[allSalaries.length/2];

        for(int i = 0;i < numOfNewCMPS; i++) {
            Company TempCMP = new Company();
            TempCMP.ID = highestID + 1;
            TempCMP.GreedMultiplier = R.nextDouble(LowestGreedMultiplier, HighestGreedMultiplier);
            boolean isGoingToBeA = R.nextDouble() <= AtoBCompanyRatio? true : false;
            TempCMP.ProductName = isGoingToBeA ? "A" : "B";
            if(isGoingToBeA) ACorps++;
            TempCMP.PreviousPrice = isGoingToBeA? AverageProductAPrice * LowestPriceMultiplier : AverageProductBPrice * LowestPriceMultiplier;
            TempCMP.PreviousUnitsProduced = 0;
            TempCMP.Salary = medianSalary * 1.01;
            TempCMP.Wealth = CompanyWealth;
            TempCMP.PriceMultiplier = R.nextDouble(LowestPriceMultiplier, HighestPriceMultiplier);
            TempCMP.RawMaterials = RawMaterials;
            for(int j = 0; j < EmployeesPerCompany; j++) {
                Employee TempEMP = new Employee();
                TempEMP.Energy = Energy;
                TempEMP.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                TempEMP.AConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                TempEMP.Salary = TempCMP.Salary;
                TempEMP.Wealth = EmployeeWealth;
                TempCMP.Employees.add(TempEMP);
            }

            Companies.add(TempCMP);
            NumberOfCompanies ++;
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
