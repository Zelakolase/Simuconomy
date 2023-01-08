public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            // We can monitor metrics here
        }
    }
}
