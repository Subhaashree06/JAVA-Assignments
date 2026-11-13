
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibrarySearch extends JFrame {
    private final JTextField query=new JTextField(22);
    private final JComboBox<String> category=new JComboBox<>(new String[]{"All","Title","Author","Subject"});
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"Title","Author","Category","Availability"},0);
    private final JTable results=new JTable(model);
    private final JLabel status=new JLabel("Enter a search term.");
    public LibrarySearch(){
        super("Campus Library Search");
        setLayout(new BorderLayout(8,8));
        JPanel search=new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Search")); search.add(query); search.add(category);
        JButton searchButton=new JButton("Search"); search.add(searchButton);
        JPanel bottom=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev=new JButton("Prev"), next=new JButton("Next"), reserve=new JButton("Reserve");
        bottom.add(prev); bottom.add(next); bottom.add(reserve);
        JPanel south=new JPanel(new BorderLayout()); south.add(bottom,BorderLayout.CENTER); south.add(status,BorderLayout.SOUTH);
        add(search,BorderLayout.NORTH); add(new JScrollPane(results),BorderLayout.CENTER); add(south,BorderLayout.SOUTH);
        searchButton.addActionListener(e -> searchBooks());
        query.addActionListener(e -> searchBooks());
        prev.addActionListener(e -> status.setText("Previous results requested"));
        next.addActionListener(e -> status.setText("Next results requested"));
        reserve.addActionListener(e -> status.setText("Select a result to reserve"));
        setDefaultCloseOperation(EXIT_ON_CLOSE); setMinimumSize(new Dimension(520,320)); setSize(760,480); setLocationRelativeTo(null);
    }
    private void searchBooks(){
        String q=query.getText().trim();
        if(q.isEmpty()){status.setText("Enter a search term.");return;}
        model.setRowCount(0);
        model.addRow(new Object[]{"Java Programming","Herbert Schildt","Computer Science","Available"});
        status.setText("Search complete for: "+q);
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new LibrarySearch().setVisible(true));}
}
