
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;


public class ConverterNamedInner extends JFrame {
    JTextField amount=new JTextField(10); JComboBox<String> target=new JComboBox<>(new String[]{"USD","EUR"}); JLabel result=new JLabel(" ");
    ConverterNamedInner(){super("Converter – Named Inner");setLayout(new FlowLayout());JButton b=new JButton("Convert");add(new JLabel("INR"));add(amount);add(target);add(b);add(result);Handler h=new Handler();b.addActionListener(h);amount.addActionListener(h);addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();}});
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);pack();}
    private class Handler implements ActionListener { public void actionPerformed(ActionEvent e){convert();} }
    private void convert(){try{double v=Double.parseDouble(amount.getText().trim());if(v<0||!Double.isFinite(v))throw new NumberFormatException();String c=(String)target.getSelectedItem();result.setText(new DecimalFormat("0.00").format(v*(c.equals("USD")?0.012:0.011))+" "+c);JOptionPane.showMessageDialog(this,result.getText());}catch(NumberFormatException ex){result.setText("Invalid amount");JOptionPane.showMessageDialog(this,"Invalid amount","Error",JOptionPane.ERROR_MESSAGE);}}
    public static void main(String[]a){SwingUtilities.invokeLater(()->new ConverterNamedInner().setVisible(true));}
}
