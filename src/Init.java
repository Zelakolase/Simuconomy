public class Init {
    public static int NumberOfCompanies = 1000;
    public static int EmployeesPerCompany = 50;
    public static double ProductAEnergyMultiplier = 10;
    public static double ProductBEnergyMultiplier = ProductAEnergyMultiplier * 2.75;
    public static int Iterations = 100;
    public static int BDependencyOnA = 2;
    public static double AtoBCompanyRatio = 0.75;

    // Initalized values, might change throughout the simulation
    public static double LowestPriceMultiplier = 0.8;
    public static double HighestPriceMultiplier = 1.2;
    public static double LowestGreedMultiplier = 0.3;
    public static double HighestGreedMultiplier = 0.65;
    public static double LowestAConsumptionFactor = 0.5;
    public static double HighestAConsumptionFactor = 0.75;
    public static double Energy = 10;
    public static double Salary = 100;
    public static double CompanyWealth = (Salary * EmployeesPerCompany) * 250;
    public static double LowestFearFactor = 0.01;
    public static double HighestFearFactor = 0.2;
    public static double EmployeeWealth = Salary * 12;
    public static double ProductAPrice = 1;
    public static double ProductBPrice = ProductAPrice * 2.75;
    public static int RawMaterials = (int) (BDependencyOnA * Energy * EmployeesPerCompany * 36);
}
