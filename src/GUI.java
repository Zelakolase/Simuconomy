import javax.swing.*;
import java.awt.*;;
/*
 * Under development
 */
public class GUI extends JFrame {
    JButton SimulateButton = new JButton();
    JProgressBar SimulationIterationsCompletion = new JProgressBar();
    JCheckBox InflationReport = new JCheckBox();
    JCheckBox WealthReport = new JCheckBox();
    JTextField MinGreedRange = new JTextField();
    JTextField MaxGreedRange = new JTextField();

    public void init() {
        this.setTitle("Simuconomy GUI");
        JPanel P = new JPanel();
        P.setLayout(new BorderLayout(5, 5));
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        this.add(P);

    }

    public static void main(String[] args) {
        GUI G = new GUI();
        G.init();    
    }
}