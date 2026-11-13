
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;


public class Converter extends JFrame {
    private final JTextField amount=new JTextField(10);
    private final JComboBox<String> target=new JComboBox<>(new String[]{"USD","EUR"});
    private final JLabel result=new JLabel(" ");
    private static double rate(String currency){return currency.equals("USD")?0.012:0.011;}
    public Converter(){super("Currency Converter");setLayout(new FlowLayout());JButton convertButton=new JButton("Convert");add(new JLabel("INR"));add(amount);add(target);add(convertButton);add(result);
        ActionListener convertAction=e->convert(); convertButton.addActionListener(convertAction); amount.addActionListener(convertAction);
        addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();}});
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);pack();
    }
    private void convert(){try{double value=Double.parseDouble(amount.getText().trim());if(value<0||!Double.isFinite(value))throw new NumberFormatException();String currency=(String)target.getSelectedItem();String formatted=new DecimalFormat("0.00").format(value*rate(currency));result.setText(formatted+" "+currency);JOptionPane.showMessageDialog(this,formatted+" "+currency,"Conversion Result",JOptionPane.INFORMATION_MESSAGE);}catch(NumberFormatException ex){result.setText("Invalid amount");JOptionPane.showMessageDialog(this,"Invalid amount","Input Error",JOptionPane.ERROR_MESSAGE);}}
    public static void main(String[]args){SwingUtilities.invokeLater(()->new Converter().setVisible(true));}
}
