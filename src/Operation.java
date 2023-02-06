import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

public class Operation extends GlobalENV {
    static double medianSalary = -1;
    /*
     * Work() iterates over all companies, sums up all employees' energies and puts an offer on the market
     */
    public static void Work() {
        offerList.List.clear();
        AverageProductAPrice = 0;
        AverageProductBPrice = 0;
        double[] tempAPrices = new double[ACorps];
        double[] tempBPrices = new double[NumberOfCompanies - ACorps];
        int AInd = 0, BInd = 0;
        for (Company C : Companies) {
            int UnitsToProduce = 0;
            ArrayList<Employee> ToRemove = new ArrayList<>();

            for (Employee E : C.Employees) {
                /*
                 * The company extracts more value from labor than it pays back,
                 * the factor of extraction is determined by GreedMultiplier, 
                 * if GreedMultiplier = 0.5, the factor of extraction is 50% (1.5),
                 * the difference between the workers' salaries and the extracted value shrinks ..
                 * when GreedMultiplier is lower and vice versa.
                 */
                UnitsToProduce += (E.Energy * E.ActualSkillandWellbeing * (C.GreedMultiplier + 1));
                if(E.Energy == 0) ToRemove.add(E); // A dead employee
                E.Energy = 0;
            }

            C.Employees.removeAll(ToRemove); // Remove dead employees

            if (C.ProductName.equals("B")) {
                int tempUnitsToProduce = C.RawMaterials / BDependencyOnA;
                if (tempUnitsToProduce < UnitsToProduce) UnitsToProduce = tempUnitsToProduce;
                if (UnitsToProduce < BDependencyOnA) UnitsToProduce = 0;
                else C.RawMaterials -= (UnitsToProduce * BDependencyOnA);
            }

            UnitsToProduce *= C.Efficiency;

            C.PreviousUnitsProduced = UnitsToProduce;
            double Price = C.PreviousPrice * C.PriceMultiplier;
            /*
             * For median values calculation
             */
            if (C.ProductName.equals("B")) {
                tempBPrices[BInd] = Price;
                BInd++;
            } else {
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

        AverageProductAPrice = Median.median(tempAPrices);
        AverageProductBPrice = Median.median(tempBPrices);
    }

    /*
     * Both companies and employees demand according to needs (survival and luxury)
     */
    public static void Demand() {
        for (Company C : Companies) {
            if (C.ProductName.equals("B")) {
                int UnitsToPurchase = BDependencyOnA * C.PreviousUnitsProduced;
                /*
                 * If it is expected to consume >25% of the raw material inventory at one iteration, buy more raw materials
                 */
                if (UnitsToPurchase / RawMaterials > 0.25) {
                    Entry<Double, Integer> RawMaterialPurchaseProcess = Purchase("A", UnitsToPurchase, C.Wealth);
                    C.RawMaterials += RawMaterialPurchaseProcess.getValue();
                    C.Wealth -= RawMaterialPurchaseProcess.getKey();
                    if(C.Wealth < 0)System.out.print("0");
                }
            }

            /*
             * The company can buy Efficiency upgrades by 'B' units with 5%-15% of its wealth, 
             * when the company can buy 1 upgrade unit, productivity increases 5%-10%.
             * This is a head-tail situation.
             */
            if (C.Wealth * R.nextDouble(0.05, 0.15) > efficiencyCostAsB * AverageProductAPrice) {
                if (R.nextDouble() < 0.5) {
                    Entry<Double, Integer> Eff = Purchase("B", (int) efficiencyCostAsB, C.Wealth);
                    C.Wealth -= Eff.getKey();
                    if(C.Wealth < 0)System.out.print("1");
                    double UpgradeValue = Eff.getValue() / efficiencyCostAsB; // 1.0 is default, if the company can buy more than one upgrade, it will be effective
                    if (UpgradeValue > 0) C.Efficiency += R.nextDouble(0.05 * UpgradeValue, 0.1 * UpgradeValue);
                }
            }

            for (Employee E : C.Employees) E = EUpdate(E);
            C.CEO = EUpdate(C.CEO); // The CEO of the company is capable of doing demand as well
        }
    }

    /*
     * NOT A STAGE: It executes demand logic for any employee E.
     */
    private static Employee EUpdate(Employee E) {
        int UpdatedEnergy = 0;
        if (E.Wealth > 0) {
            /*
             * If the survival consumption of salary is higher than the actual survival cost
             */
            if (AverageProductAPrice * LowestFoodIntake <= E.Salary * E.AConsumptionFactor) {
                /*
                 * The salary is sufficient to cover survival costs
                 */
                Entry<Double, Integer> APurchase = Purchase("A", E.AConsumptionFactor * E.Salary);
                E.Wealth -= APurchase.getKey();
                E.Salary -= APurchase.getKey();
                UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();

                if (E.Salary > 0 && APurchase.getValue() > LowestFoodIntake) {
                    /*
                     * If there is remaining salary and we survived (hopefully), purchase luxury units
                     */
                    Entry<Double, Integer> BPurchase = Purchase("B", E.Salary);
                    E.Wealth -= BPurchase.getKey();
                    E.Salary -= BPurchase.getKey();
                    UpdatedEnergy += ProductBEnergyMultiplier * BPurchase.getValue();
                    /*
                     * Prioritize luxury spending by decreasing survival consumption factor
                     * If FearFactor = 0.2, AConsumptionFactor will be decreased by 30%.
                     */
                    E.AConsumptionFactor *= 1 - Math.abs(0.5 - E.FearFactor);
                    /*
                     * If we were able to buy 10 units of 'luxury' product, decrease FearFactor from 0.01 to 0.05
                     */
                    if(BPurchase.getValue() > 0) E.FearFactor -= R.nextDouble(0.001 * BPurchase.getValue(), 0.005 * BPurchase.getValue());
                } else {
                    /*
                     * If all of the salary is spent on survival units and/or it is not sufficient for survival, 
                     * increase the consumption factor by FearFactor slope.
                     */
                    E.AConsumptionFactor += (E.AConsumptionFactor * E.FearFactor);
                }
            } else {
                /*
                 * The salary is not sufficient to cover survival costs
                 * If survival costs 30% of total wealth, tempWealthRatio is 70%
                 */
                double tempWealthRatio = 1 - ((AverageProductAPrice * LowestFoodIntake) / E.Wealth);
                /*
                 * Not doing this will cause the employee to spend non-existent money
                 */
                if (tempWealthRatio > 1) tempWealthRatio = 1;
                /*
                 * Will purchase survival costs with Wealth
                 */
                Entry<Double, Integer> APurchase = Purchase("A", E.Wealth * tempWealthRatio);
                E.Wealth -= APurchase.getKey();
                E.Salary -= APurchase.getKey();
                UpdatedEnergy += ProductAEnergyMultiplier * APurchase.getValue();
                /*
                 * If tempWealthRatio is 70% (0.7), FearFactor should increase by 3.5% (0.035) - 7% (0.07)
                 */
                E.AConsumptionFactor += (E.AConsumptionFactor * E.FearFactor); // Increase AConsumptionFactor until the crisis ends
                if (tempWealthRatio > 0) E.FearFactor += R.nextDouble(tempWealthRatio / 20, tempWealthRatio / 10);
            }
        }

        E.Energy = UpdatedEnergy;
        if (E.FearFactor < 0) E.FearFactor = 0; // If FearFactor goes negative, it becomes zero
        if (E.FearFactor > 1) E.FearFactor = 1.0; // If FearFactor goes above 100%, it becomes 100%
        if (E.AConsumptionFactor > 1) E.AConsumptionFactor = 1; // If AConsumptionFactor goes above 100%, it becomes 100%
        // TODO: The AConsumptionFactor needs to increase/decrease (encourage/discourage consumerism) based on economic stability
        if (E.AConsumptionFactor < LowestAConsumptionFactor) E.AConsumptionFactor = LowestAConsumptionFactor; // So it wouldn't go < 0
        return E;
    }

    public static void Revenue() {
        for (Company C : Companies) {
            OfferList.Offer F = offerList.List.get(C.ID);
            if(C.PreviousUnitsProduced < F.UnitsAvailable) continue;
            /*
             * (R)equire (P)rice (I)ncrement if all the offer units are sold
             */
            boolean RPI = (F.UnitsAvailable <= 0 && C.PreviousUnitsProduced > 0) ? true : false;
            /*
             * (R)equire (P)rice (D)ecrement if 10% or more of the offer is left unsold
             */
            boolean RPD = (C.PreviousUnitsProduced > 0 && F.UnitsAvailable / C.PreviousUnitsProduced > 0.1) ? true : false;
            boolean TMP = false;
            F.UnitsAvailable = F.UnitsAvailable < 0 ? 0 : F.UnitsAvailable;
            double Revenue = (C.PreviousUnitsProduced - F.UnitsAvailable) * F.Price;
            /*
             * The company has no money, skip
             */
            if(Revenue + C.Wealth == 0 || C.Wealth <= 0 || Revenue < 0) continue; // Dead

            boolean OneTimeFlag = false;
            /*
             * If GreedMultiplier = 0.9 and the revenue is not enough to pay salaries,
             * decrease salaries by 0.9% - 1.8% until the revenue is sufficient.
             */
            double RevPercent = R.nextDouble(C.GreedMultiplier / 600, C.GreedMultiplier / 120);
            while (Revenue * (1 - RevPercent) <= (C.Salary * C.Employees.size() * 3)) {
                if(!OneTimeFlag) OneTimeFlag = true;
                if(OneTimeFlag) Revenue += C.Wealth; // Company wealth will be used to pay out workers
                C.Salary *= 1 - R.nextDouble(C.GreedMultiplier / 150, C.GreedMultiplier / 50);
                /*
                 * TMP flag is raised if the revenue is not sufficient and we needed the company wealth
                 */
                TMP = true;
            }

            /*
             * If the revenue was sufficient and GreedMultiplier = 0.9, 
             * the CEO gets 50x the pay of the worker or 0.15% - 0.75% of the revenue, which is lower
             */
            C.CEO.Salary = RevPercent * Revenue;

            /*
             * If the current CEO salary is higher than or equal to the revenue and the revenue was not sufficient to pay out workers,
             * and GreedMultiplier = 0.9,
             * the salary will be 30% - 45% the revenue.
             */
            Revenue -= C.CEO.Salary;
            C.CEO.Wealth += C.CEO.Salary;

            /*
             * Pay out workers
             */
            double MaxSF = 0;
            for (Employee E : C.Employees) {
                E.Wealth += (C.Salary * E.ActualSkillandWellbeing);
                E.Salary = (C.Salary * E.ActualSkillandWellbeing);
                Revenue -= (C.Salary * E.ActualSkillandWellbeing);

                /*
                 * Skill and Wellbeing factor can be affected
                 * 0.1% chance to have an emergency situation that can affect your productivity by 10%-50%
                 */
                if(R.nextDouble() < 0.001) E.ActualSkillandWellbeing /= R.nextDouble(1.1, 2);
                if(E.ActualSkillandWellbeing < E.PotentialSkillandWellbeing) E.ActualSkillandWellbeing += R.nextDouble(0, E.PotentialSkillandWellbeing - E.ActualSkillandWellbeing);
                if(E.ActualSkillandWellbeing < 0) E.ActualSkillandWellbeing = E.PotentialSkillandWellbeing / 2;
                if(E.ActualSkillandWellbeing > MaxSF) MaxSF = E.ActualSkillandWellbeing;
            }
            C.HighestSF = MaxSF;

            /*
             * If GreedMultiplier = 0.9 and the revenue wasn't sufficient to pay workers, increase price velocity by 0.006 - 0.009
             */
            if (TMP) C.PriceMultiplier += R.nextDouble(C.GreedMultiplier / 150, C.GreedMultiplier / 100);

            if (RPI) {
                /*
                 * If the (R)equire (P)rice (I)ncrement flag was true and GreedMultiplier = 0.9,
                 * Increase price velocity by 0.006 - 0.009, 
                 * and a 90% chance to decrease salaries by 0.6% - 0.9%
                 */
                C.PriceMultiplier += R.nextDouble(C.GreedMultiplier / 150, C.GreedMultiplier / 100);
                if (R.nextDouble() < C.GreedMultiplier) C.Salary *= 1 - R.nextDouble(C.GreedMultiplier / 150, C.GreedMultiplier / 100);
            } else if (RPD) {
                /*
                 * If the (R)equire (P)rice (D)ecrement flag was true and GreedMultiplier = 0.9,
                 * Decrease price velocity by 0.0006 - 0.001,
                 * and 10% chance to increase salary by 0.067% - 0.1%, otherwise decrease salary by 0.6% - 0.9%
                 */
                C.PriceMultiplier -= R.nextDouble((1-C.GreedMultiplier) / 150, (1-C.GreedMultiplier) / 100);
                if (R.nextDouble() < 1 - C.GreedMultiplier) C.Salary *= 1 + R.nextDouble((1 - C.GreedMultiplier) / 150, (1 - C.GreedMultiplier) / 100);
                else C.Salary *= 1 - R.nextDouble(C.GreedMultiplier / 150, C.GreedMultiplier / 100);
            }

            if (C.PriceMultiplier < LowestPriceMultiplier) C.PriceMultiplier = LowestPriceMultiplier;
            if (TMP) C.Wealth = Revenue;
            else C.Wealth += Revenue;
            if (C.PreviousUnitsProduced <= 0) {
                C.PreviousPrice = AverageProductAPrice;
                C.PriceMultiplier = 1;
            }

            /*
             * A 50/50 chance to: (GreedMultiplier = 0.9)
             * 1. Decrease salary if we have excess workers by 0.6% - 0.9%
             * 2. Increase salary if we do not have enough workers by 0.067% - 0.1%
             */
            int deltaEmployees = C.Employees.size() - EmployeesPerCompany;
            if (R.nextDouble() < 0.5) {
                if (deltaEmployees > 0 && !(RPI || RPD || TMP)) C.Salary *= 1 - R.nextDouble(C.GreedMultiplier / 150, C.GreedMultiplier / 100);
                else if (deltaEmployees < 0 && !(RPI || RPD || TMP)) C.Salary *= 1 + R.nextDouble((1 - C.GreedMultiplier) / 150, (1 - C.GreedMultiplier) / 100);
            }
        }
    }

    public static void EmployeeTransfer() {
        mSalary();

        /*
         * If the number of companies is 100, make 20 employee transfers
         * from random lower salary company to random higher salary company
         */
        Main: for (int i = 0; i < NumberOfCompanies / 5; i++) {
            int[] ft = FindCompanyBySalaryRange(medianSalary);
            int fromCMPID = ft[0];
            int toCMPID = ft[1];
            Employee EMPtoTransfer = null;
            if (fromCMPID == -1 || toCMPID == -1) break Main;

            if(Companies.get(fromCMPID).Employees.size() > 0 ) {
                int randIndex = R.nextInt(0, Companies.get(fromCMPID).Employees.size());
                EMPtoTransfer = Companies.get(fromCMPID).Employees.get(randIndex);
                Companies.get(fromCMPID).Employees.remove(randIndex);
            }
            if (EMPtoTransfer == null) {
                EMPtoTransfer = new Employee();
                EMPtoTransfer.Energy = Energy;
                EMPtoTransfer.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                EMPtoTransfer.AConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                EMPtoTransfer.PotentialSkillandWellbeing = Skew(1.0, 3.0, 1, -2);
                EMPtoTransfer.ActualSkillandWellbeing = EMPtoTransfer.PotentialSkillandWellbeing;
                EMPtoTransfer.Salary = Companies.get(toCMPID).Salary * EMPtoTransfer.ActualSkillandWellbeing;
                EMPtoTransfer.Wealth = EmployeeWealth;
            } else {
                EMPtoTransfer.Salary = Companies.get(toCMPID).Salary;
            }
            Companies.get(toCMPID).Employees.add(EMPtoTransfer);
        }
    }

    public static void Expand() {
        int numOfNewCMPS = R.nextDouble() > 0.5 ? 1 : 0;
        int highestID = 0;

        if(medianSalary == -1) mSalary();

        for (int i = 0; i < numOfNewCMPS; i++) {
            Company TempCMP = new Company();
            TempCMP.ID = highestID + i + 1;
            TempCMP.GreedMultiplier = R.nextDouble(LowestGreedMultiplier, HighestGreedMultiplier);
            boolean isGoingToBeA = R.nextDouble() <= AtoBCompanyRatio ? true : false;
            TempCMP.ProductName = isGoingToBeA ? "A" : "B";
            if (isGoingToBeA) ACorps++;
            TempCMP.PreviousPrice = isGoingToBeA ? AverageProductAPrice * LowestPriceMultiplier : AverageProductBPrice * LowestPriceMultiplier;
            TempCMP.PreviousUnitsProduced = 0;
            TempCMP.Salary = medianSalary * 1.05; // The new company salary is 5% higher than the market
            TempCMP.Wealth = CompanyWealth;
            TempCMP.PriceMultiplier = R.nextDouble(LowestPriceMultiplier, HighestPriceMultiplier);
            TempCMP.RawMaterials = RawMaterials;
            for (int j = 0; j < EmployeesPerCompany; j++) {
                Employee TempEMP = new Employee();
                TempEMP.Energy = Energy;
                TempEMP.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                TempEMP.AConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                TempEMP.Salary = TempCMP.Salary;
                TempEMP.PotentialSkillandWellbeing = Skew(1.0, 3.0, 1, -2);
                TempEMP.ActualSkillandWellbeing = TempEMP.PotentialSkillandWellbeing;
                TempEMP.Wealth = EmployeeWealth;
                TempCMP.Employees.add(TempEMP);
            }

            Companies.add(TempCMP);
            NumberOfCompanies++;
        }
    }

    public static Entry<Double, Integer> Purchase(String Product, int UnitsToPurchase, double ToSpend) { // Returns Key: SpentMoney, Value: UnitsBought
        Entry<Double, Integer> output = new AbstractMap.SimpleEntry<Double, Integer>(0d, 0);
        int OptimalIndex = 0, OptimalUnits = 0;
        if (offerList.HighestUnit == 0) {
            for (OfferList.Offer F : offerList.List) F.UnitsAvailable = -1;
            offerList.HighestUnit = -1;

        } else if (offerList.HighestUnit > 0) {
            for (OfferList.Offer F : offerList.List) {
                int MaxPurchasableUnits = (int) (ToSpend / F.Price);
                MaxPurchasableUnits = MaxPurchasableUnits > F.UnitsAvailable ? F.UnitsAvailable : MaxPurchasableUnits;
                if (OptimalUnits < MaxPurchasableUnits) {
                    OptimalIndex = F.CompanyID;
                    OptimalUnits = MaxPurchasableUnits;
                }
            }

            if (OptimalUnits == 0)
                output = Purchase(Product, ToSpend);
            else if (OptimalUnits >= UnitsToPurchase) {
                offerList.List.get(OptimalIndex).UnitsAvailable -= UnitsToPurchase;
                output = new AbstractMap.SimpleEntry<Double, Integer>(offerList.List.get(OptimalIndex).Price * UnitsToPurchase, UnitsToPurchase);
            } else {
                double SpentMoney = 0;
                int UnitsBought = 0;
                offerList.List.get(OptimalIndex).UnitsAvailable -= OptimalUnits;
                UnitsBought += OptimalUnits;
                SpentMoney += OptimalUnits * offerList.List.get(OptimalIndex).Price;
                UnitsToPurchase -= UnitsBought;
                ToSpend -= SpentMoney;
                Main: while (ToSpend > 0 && UnitsToPurchase > 0) {
                    Entry<Double, Integer> temp = Purchase(Product, ToSpend);
                    if (temp.getKey() == 0 || temp.getValue() == 0) break Main;
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
        if (offerList.HighestUnit == 0) {
            for (OfferList.Offer F : offerList.List) F.UnitsAvailable = -1;
            offerList.HighestUnit = -1;

        } else if (offerList.HighestUnit > 0) {
            int OptimalIndex = 0, OptimalUnits = 0;

            for (OfferList.Offer F : offerList.List) {
                int MaxPurchasableUnits = (int) (ToSpend / F.Price);
                MaxPurchasableUnits = MaxPurchasableUnits > F.UnitsAvailable ? F.UnitsAvailable : MaxPurchasableUnits;
                if (OptimalUnits < MaxPurchasableUnits) {
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

    private static void mSalary() {
        double[] allSalaries = new double[Companies.size()];
        int Ind = 0, highestID = 0;
        for (Company C : Companies) {
            allSalaries[Ind] = C.Salary;
            Ind++;
            if (highestID < C.ID) highestID = C.ID;
        }
        medianSalary = Median.median(allSalaries);
    }
}
