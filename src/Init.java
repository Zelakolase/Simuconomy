public class Init {
    public static int NumberOfCompanies = 100;
    public static int EmployeesPerCompany = 15;
    public static double ProductAEnergyMultiplier = 10;
    public static double ProductBEnergyMultiplier = ProductAEnergyMultiplier * 1.5;
    public static int Iterations = 2000;
    public static int BDependencyOnA = 2;
    public static double AtoBCompanyRatio = 0.75;

    // Initalized values, might change throughout the simulation
    public static double LowestPriceMultiplier = 0.8;
    public static double HighestPriceMultiplier = 1.05;
    public static double LowestGreedMultiplier = 0.35;
    public static double HighestGreedMultiplier = 0.65; // Should NOT be higher than 1.0
    public static double LowestAConsumptionFactor = 0.5;
    public static double HighestAConsumptionFactor = 0.75;
    public static double Energy = 10;
    public static double LowestSalary = 10;
    public static double HighestSalary = 11;
    public static double CompanyWealth = (HighestSalary * EmployeesPerCompany) * 50;
    public static double LowestFearFactor = 0.01;
    public static double HighestFearFactor = 0.2;
    public static double EmployeeWealth = HighestSalary * 50;
    public static double ProductAPrice = ((HighestSalary * EmployeesPerCompany) / (Energy * EmployeesPerCompany)) * 0.85;
    public static double ProductBPrice = ProductAPrice * 1.5;
    public static int RawMaterials = (int) (BDependencyOnA * Energy * EmployeesPerCompany * 36);
}
