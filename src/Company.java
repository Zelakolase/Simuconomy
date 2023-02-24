import java.util.ArrayList;

public class Company {
    public int ID;   
    public double Wealth;
    public double Salary;
    public int RawMaterials;
    public double PriceMultiplier;
    public double GreedMultiplier;
    public String ProductName;
    public double PreviousPrice;
    public int PreviousUnitsProduced;
    public double Efficiency = 1.0;
    public Employee CEO = new Employee();
    volatile public ArrayList<Employee> Employees = new ArrayList<>();
    public double HighestSF;
}