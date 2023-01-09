import java.util.ArrayList;
import java.util.Random;

public class GlobalENV extends Init {
    public static ArrayList<Company> Companies = new ArrayList<>();
    public static OfferList offerList = new OfferList();
    public static double AverageProductAPrice = 0;
    public static double AverageProductBPrice = 0;
    public static int ACorps = 0;
    static Random R = new Random();

    public static void Initalize() {
        for(int i = 0;i < NumberOfCompanies; i++) {
            Company TempCMP = new Company();
            TempCMP.ID = i;
            TempCMP.GreedMultiplier = R.nextDouble(LowestGreedMultiplier, HighestGreedMultiplier);
            boolean isGoingToBeA = R.nextDouble() <= AtoBCompanyRatio? true : false;
            TempCMP.ProductName = isGoingToBeA ? "A" : "B";
            if(isGoingToBeA) ACorps++;
            TempCMP.PreviousPrice = isGoingToBeA? ProductAPrice : ProductBPrice;
            TempCMP.PreviousUnitsProduced = 0;
            TempCMP.Salary = R.nextDouble(LowestSalary, HighestSalary);
            TempCMP.Wealth = CompanyWealth;
            TempCMP.PriceMultiplier = R.nextDouble(LowestPriceMultiplier, HighestPriceMultiplier);
            TempCMP.RawMaterials = RawMaterials;
            for(int j = 0; j < EmployeesPerCompany; j++) {
                Employee TempEMP = new Employee();
                TempEMP.Energy = Energy;
                TempEMP.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                TempEMP.FoodConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                TempEMP.Salary = TempCMP.Salary;
                TempEMP.Wealth = EmployeeWealth;
                TempCMP.Employees.add(TempEMP);
            }
            Companies.add(TempCMP);
        }
    }

    // returns random Company ID
    public static int FindCompanyBySalaryRange(boolean higherThan, double bound) {
        ArrayList<Company> tempCMP = new ArrayList<>();
        for(Company C : Companies) {
            if(higherThan) {
                if(C.Salary > bound) tempCMP.add(C);
            }
            else {
                if(C.Salary <= bound) tempCMP.add(C);
            }
        }

        if(!tempCMP.isEmpty()){
            int randIndex = R.nextInt(0, tempCMP.size());
            return tempCMP.get(randIndex).ID;
        }
        return -1;
    }
}
