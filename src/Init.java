public class Init {
    volatile public static int NumberOfCompanies = 100;
    public static int EmployeesPerCompany = 10;
    public static double ProductAEnergyMultiplier = 10;
    public static double ProductBEnergyMultiplier = ProductAEnergyMultiplier * 1.5;
    public static int Iterations = 500 * 12;
    public static int BDependencyOnA = 1;
    public static double AtoBCompanyRatio = 0.5;

    // Initalized values, might change throughout the simulation
    public static double ProductAPrice = 1;
    public static double ProductBPrice = 1.5;
    public static double LowestFoodIntake = 30;
    public static double LowestPriceMultiplier = 0.85;
    public static double HighestPriceMultiplier = 1.15;
    public static double LowestGreedMultiplier = 0.35;
    public static double HighestGreedMultiplier = 0.65; // Should NOT be higher than 1.0
    public static double LowestAConsumptionFactor = 0.35;
    public static double HighestAConsumptionFactor = 0.85;
    public static double LowestSalary = LowestFoodIntake * ProductAPrice * (HighestAConsumptionFactor + 1) * 1.5;
    public static double HighestSalary = LowestSalary * 1.01;
    public static double CompanyWealth = (HighestSalary * EmployeesPerCompany) * 150;
    public static double LowestFearFactor = 0.01;
    public static double HighestFearFactor = 0.15;
    public static double Energy = 1000;
    public static double EmployeeWealth = 0;
    public static double efficiencyCostAsA = 25;
    public static int RawMaterials = (int) (BDependencyOnA * Energy * EmployeesPerCompany * 120);

}
