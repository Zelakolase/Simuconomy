public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            double var = 0;
            for(Company C : Companies) {
                if(C.Salary < Salary) C.Salary = Salary; // Minimum wage
                var += C.PreviousUnitsProduced;
            }
            
            System.out.println(var);
        }
    }
}
