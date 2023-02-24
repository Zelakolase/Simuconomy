import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalENV extends Init {
    volatile public static ArrayList<Company> Companies = new ArrayList<>();
    public static OfferList offerList = new OfferList();
    public static double AverageProductAPrice = 0;
    public static double AverageProductBPrice = 0;
    volatile public static int ACorps = 0;
    static Random R = new Random();
    public static ExecutorService ES = Executors.newCachedThreadPool();

    public static void Initalize() {
        for(int i = 0;i < NumberOfCompanies; i++) {
            double HighestSkill = 0;
            Company TempCMP = new Company();
            TempCMP.ID = i;
            TempCMP.GreedMultiplier = Skew(LowestGreedMultiplier, HighestGreedMultiplier, 1, -2);
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
                TempEMP.AConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                TempEMP.Salary = TempCMP.Salary;
                TempEMP.Wealth = EmployeeWealth;
                TempEMP.PotentialSkillandWellbeing = Skew(1.0, 3.0, 1, -2);
                TempEMP.ActualSkillandWellbeing = TempEMP.PotentialSkillandWellbeing;
                if(TempEMP.PotentialSkillandWellbeing > HighestSkill) HighestSkill = TempEMP.PotentialSkillandWellbeing;
                TempCMP.Employees.add(TempEMP);
            }
            Employee CEO = new Employee();
            CEO.Wealth = (CompanyWealth + EmployeeWealth) / 2;
            CEO.Salary = HighestSalary;
            CEO.Energy = Energy;
            CEO.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
            CEO.AConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);

            TempCMP.CEO = CEO;
            TempCMP.HighestSF = HighestSkill;
            Companies.add(TempCMP);
        }
    }

    // returns random Company ID
    public static int[] FindCompanyBySalaryRange(double bound) {
        ArrayList<Company> tempCMP0 = new ArrayList<>();
        ArrayList<Company> tempCMP1 = new ArrayList<>();
        for(Company C : Companies) {
            if(C.Salary > bound) tempCMP0.add(C);
            if(C.Salary <= bound) tempCMP1.add(C);
        }

        if(!tempCMP0.isEmpty() && !tempCMP1.isEmpty()){
            int randIndex = R.nextInt(0, tempCMP0.size());
            int randIndex2 = R.nextInt(0, tempCMP1.size());
            return new int[] {randIndex, randIndex2};
        }
        return new int[] {-1, -1};
    }

    /*
     * From: https://stackoverflow.com/a/13548135
     */
    static public double Skew(double min, double max, double skew, double bias) {
        double range = max - min;
        double mid = min + range / 2.0;
        double unitGaussian = R.nextGaussian();
        double biasFactor = Math.exp(bias);
        double retval = mid+(range*(biasFactor/(biasFactor+Math.exp(-unitGaussian/skew))-0.5));
        return retval;
    }
}
