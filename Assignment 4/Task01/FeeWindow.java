
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class FeeService {
    private final String url, user, password;
    FeeService(String url, String user, String password) {
        this.url=url; this.user=user; this.password=password;
    }
    String pay(String regNo, String amountText) throws Exception {
        if (regNo == null || regNo.trim().isEmpty()) throw new IllegalArgumentException("Registration number is required");
        double amount;
        try { amount = Double.parseDouble(amountText.trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Enter a valid amount"); }
        if (!Double.isFinite(amount) || amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero");
        String sql = "INSERT INTO fees(reg_no, amount) VALUES (?, ?)";
        try (Connection c=DriverManager.getConnection(url,user,password);
             PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,regNo.trim()); ps.setDouble(2,amount); ps.executeUpdate();
        }
        return String.format("Payment of %.2f recorded for %s",amount,regNo.trim());
    }
}
public class FeeWindow extends JFrame {
    private final JTextField regNo=new JTextField(12), amount=new JTextField(10);
    private final JLabel status=new JLabel("Enter fee details");
    private final FeeService service=new FeeService(
        System.getenv().getOrDefault("FEE_DB_URL","jdbc:mysql://localhost:3306/campus"),
        System.getenv().getOrDefault("FEE_DB_USER","root"),
        System.getenv().getOrDefault("FEE_DB_PASSWORD",""));
    public FeeWindow() {
        super("Student Fee Payment");
        JPanel form=new JPanel(new GridLayout(2,2,8,8));
        form.add(new JLabel("Registration No.")); form.add(regNo);
        form.add(new JLabel("Amount")); form.add(amount);
        JButton pay=new JButton("Pay"), clear=new JButton("Clear"), receipt=new JButton("Receipt");
        JPanel actions=new JPanel(); actions.add(pay); actions.add(clear); actions.add(receipt);
        add(form,BorderLayout.NORTH); add(actions,BorderLayout.CENTER); add(status,BorderLayout.SOUTH);
        pay.addActionListener(e -> payFee());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Form cleared"); });
        receipt.addActionListener(e -> JOptionPane.showMessageDialog(this,status.getText(),"Payment Receipt",JOptionPane.INFORMATION_MESSAGE));
        addWindowListener(new WindowAdapter(){ @Override public void windowClosing(WindowEvent e){ dispose(); } });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); setSize(420,220); setLocationRelativeTo(null);
    }
    private void payFee() {
        final String id=regNo.getText(), value=amount.getText();
        status.setText("Processing...");
        new SwingWorker<String,Void>() {
            protected String doInBackground() throws Exception { return service.pay(id,value); }
            protected void done() {
                try {
                    String message=get();
                    status.setText(message);
                    JOptionPane.showMessageDialog(FeeWindow.this,message,"Payment",JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Throwable cause=ex.getCause()==null?ex:ex.getCause();
                    status.setText("Payment could not be completed");
                    JOptionPane.showMessageDialog(FeeWindow.this,cause.getMessage(),"Payment Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true)); }
}
