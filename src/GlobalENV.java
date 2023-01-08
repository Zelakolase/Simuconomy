import java.util.ArrayList;
import java.util.Random;

public class GlobalENV extends Init {
    public static ArrayList<Company> Companies = new ArrayList<>();
    public static OfferList offerList = new OfferList();
    public static double AverageProductAPrice = 0;
    public static double AverageProductBPrice = 0;
    static Random R = new Random();
    public static void Initalize() {
        for(int i = 0;i < NumberOfCompanies; i++) {
            Company TempCMP = new Company();
            TempCMP.ID = i;
            TempCMP.GreedMultiplier = R.nextDouble(LowestGreedMultiplier, HighestGreedMultiplier);
            boolean isGoingToBeA = R.nextDouble(0, 1) <= AtoBCompanyRatio? true : false;
            TempCMP.ProductName = isGoingToBeA? "A" : "B";
            TempCMP.PreviousPrice = isGoingToBeA? ProductAPrice : ProductBPrice;
            TempCMP.PreviousUnitsProduced = 0;
            TempCMP.Salary = Salary;
            TempCMP.Wealth = CompanyWealth;
            TempCMP.PriceMultiplier = R.nextDouble(LowestPriceMultiplier, HighestPriceMultiplier);
            TempCMP.RawMaterials = RawMaterials;
            for(int j = 0; j < EmployeesPerCompany; j++) {
                Employee TempEMP = new Employee();
                TempEMP.ID = j;
                TempEMP.Energy = Energy;
                TempEMP.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                TempEMP.FoodConsumptionFactor = R.nextDouble(LowestFoodConsumptionFactor, HighestFoodConsumptionFactor);
                TempEMP.Salary = Salary;
                TempEMP.Wealth = EmployeeWealth;
                TempCMP.Employees.add(TempEMP);
            }
            Companies.add(TempCMP);
        }

    }
}
