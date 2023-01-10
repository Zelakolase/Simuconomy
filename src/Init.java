public class Init {
    public static int NumberOfCompanies = 5;
    public static int EmployeesPerCompany = 15;
    public static double ProductAEnergyMultiplier = 5;
    public static double ProductBEnergyMultiplier = ProductAEnergyMultiplier * 1.5;
    public static int Iterations = 1000;
    public static int BDependencyOnA = 2;
    public static double AtoBCompanyRatio = 0.5;

    // Initalized values, might change throughout the simulation
    public static double LowestPriceMultiplier = 0.85;
    public static double HighestPriceMultiplier = 1.05;
    public static double LowestGreedMultiplier = 0.25;
    public static double HighestGreedMultiplier = 0.75; // Should NOT be higher than 1.0
    public static double LowestAConsumptionFactor = 0.5;
    public static double HighestAConsumptionFactor = 0.75;
    public static double Energy = 100;
    public static double LowestSalary = 10;
    public static double HighestSalary = 20;
    public static double CompanyWealth = (HighestSalary * EmployeesPerCompany) * 12;
    public static double LowestFearFactor = 0.01;
    public static double HighestFearFactor = 0.2;
    public static double EmployeeWealth = HighestSalary * 12;
    public static double ProductAPrice = ((HighestSalary * EmployeesPerCompany) / (Energy * EmployeesPerCompany)) * 1.25;
    public static double ProductBPrice = ProductAPrice * 1.5;
    public static int RawMaterials = (int) (BDependencyOnA * Energy * EmployeesPerCompany * 12);
}
