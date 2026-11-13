
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;


public class ConverterFrameListener extends JFrame implements ActionListener {
    JTextField amount=new JTextField(10); JComboBox<String> target=new JComboBox<>(new String[]{"USD","EUR"});
    JLabel result=new JLabel(" "); JButton button=new JButton("Convert");
    ConverterFrameListener(){super("Converter – Frame Listener");setLayout(new FlowLayout());add(new JLabel("INR"));add(amount);add(target);add(button);add(result);button.addActionListener(this);amount.addActionListener(this);addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){dispose();}});
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);pack();}
    public void actionPerformed(ActionEvent e){convert();}
    private void convert(){try{double v=Double.parseDouble(amount.getText().trim());if(v<0||!Double.isFinite(v))throw new NumberFormatException();String c=(String)target.getSelectedItem();String s=new DecimalFormat("0.00").format(v*(c.equals("USD")?0.012:0.011));result.setText(s+" "+c);JOptionPane.showMessageDialog(this,result.getText());}catch(NumberFormatException ex){result.setText("Invalid amount");JOptionPane.showMessageDialog(this,"Invalid amount","Error",JOptionPane.ERROR_MESSAGE);}}
    public static void main(String[]a){SwingUtilities.invokeLater(()->new ConverterFrameListener().setVisible(true));}
}
