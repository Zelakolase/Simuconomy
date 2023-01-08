public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        for(int i = 0;i < 1; i++) { // Debug. In normal situations, i < Iterations
            Work();
            System.out.println(offerList.List.get(0)); // Debug
            Demand();
            Revenue();
            // We can monitor metrics here
        }
    }
}
