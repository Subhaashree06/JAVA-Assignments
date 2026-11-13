
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Registration {
    final int id; final String studentId, studentName, eventName, email;
    Registration(int id,String studentId,String studentName,String eventName,String email){this.id=id;this.studentId=studentId;this.studentName=studentName;this.eventName=eventName;this.email=email;}
}
class RegistrationDAO {
    private final String url=System.getenv().getOrDefault("EVENT_DB_URL","jdbc:mysql://localhost:3306/campus_events");
    private final String user=System.getenv().getOrDefault("EVENT_DB_USER","root");
    private final String password=System.getenv().getOrDefault("EVENT_DB_PASSWORD","");
    private Connection connect() throws SQLException {return DriverManager.getConnection(url,user,password);}
    void insert(String sid,String name,String event,String email) throws SQLException {
        String sql="INSERT INTO registrations(student_id,student_name,event_name,email) VALUES(?,?,?,?)";
        try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,sid);p.setString(2,name);p.setString(3,event);p.setString(4,email);p.executeUpdate();}
    }
    java.util.List<Registration> find(String event,int limit,int offset) throws SQLException {
        String sql=event.equals("All")?
          "SELECT id,student_id,student_name,event_name,email FROM registrations ORDER BY id LIMIT ? OFFSET ?":
          "SELECT id,student_id,student_name,event_name,email FROM registrations WHERE event_name=? ORDER BY id LIMIT ? OFFSET ?";
        java.util.List<Registration> out=new ArrayList<>();
        try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){
            int i=1;if(!event.equals("All"))p.setString(i++,event);p.setInt(i++,limit);p.setInt(i,offset);
            try(ResultSet r=p.executeQuery()){while(r.next())out.add(new Registration(r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5)));}
        }return out;
    }
}
class RegistrationService {
    private final RegistrationDAO dao=new RegistrationDAO();
    void register(String sid,String name,String event,String email) throws Exception {
        if(sid.isBlank()||name.isBlank()||event.isBlank()||email.isBlank())throw new IllegalArgumentException("All fields are required");
        if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))throw new IllegalArgumentException("Enter a valid email");
        dao.insert(sid.trim(),name.trim(),event.trim(),email.trim());
    }
    java.util.List<Registration> filter(String event,int limit,int offset)throws SQLException{return dao.find(event,limit,offset);}
}
public class EventRegistrationApp extends JFrame {
    private final JTextField sid=new JTextField(10),name=new JTextField(12),email=new JTextField(16);
    private final JComboBox<String> event=new JComboBox<>(new String[]{"Cultural Fest","Music Night","Dance Competition","Tech Expo"});
    private final JComboBox<String> filter=new JComboBox<>(new String[]{"All","Cultural Fest","Music Night","Dance Competition","Tech Expo"});
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"ID","Student ID","Name","Event","Email"},0);
    private final JTable table=new JTable(model); private final JLabel status=new JLabel("Ready");
    private final RegistrationService service=new RegistrationService(); private int page=0; private static final int PAGE_SIZE=20;
    public EventRegistrationApp(){
        super("Campus Event Registration");setLayout(new BorderLayout(8,8));
        JMenuBar bar=new JMenuBar();JMenu file=new JMenu("File");JMenuItem refresh=new JMenuItem("Refresh"),export=new JMenuItem("Export CSV"),exit=new JMenuItem("Exit");file.add(refresh);file.add(export);file.addSeparator();file.add(exit);bar.add(file);setJMenuBar(bar);
        JPanel form=new JPanel(new GridLayout(2,4,6,6));form.setBorder(BorderFactory.createTitledBorder("Student Registration"));
        form.add(new JLabel("Student ID"));form.add(sid);form.add(new JLabel("Student Name"));form.add(name);form.add(new JLabel("Event"));form.add(event);form.add(new JLabel("Email"));form.add(email);
        JButton register=new JButton("Register"),filterButton=new JButton("Filter"),prev=new JButton("Previous"),next=new JButton("Next");
        JPanel tools=new JPanel();tools.add(register);tools.add(new JLabel("Filter event"));tools.add(filter);tools.add(filterButton);tools.add(prev);tools.add(next);
        JPanel north=new JPanel(new BorderLayout());north.add(form,BorderLayout.NORTH);north.add(tools,BorderLayout.SOUTH);
        add(north,BorderLayout.NORTH);add(new JScrollPane(table),BorderLayout.CENTER);add(status,BorderLayout.SOUTH);
        register.addActionListener(e->registerAsync());filterButton.addActionListener(e->{page=0;loadAsync();});refresh.addActionListener(e->loadAsync());prev.addActionListener(e->{if(page>0){page--;loadAsync();}});next.addActionListener(e->{page++;loadAsync();});exit.addActionListener(e->dispose());
        export.addActionListener(e->exportCsv());setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(1000,600);setLocationRelativeTo(null);loadAsync();
    }
    private void registerAsync(){
        String a=sid.getText(),b=name.getText(),c=(String)event.getSelectedItem(),d=email.getText();
        new SwingWorker<Void,Void>(){protected Void doInBackground()throws Exception{service.register(a,b,c,d);return null;}
            protected void done(){try{get();status.setText("Registration saved");loadAsync();}catch(Exception ex){status.setText("Registration failed");JOptionPane.showMessageDialog(EventRegistrationApp.this,ex.getCause()==null?ex.getMessage():ex.getCause().getMessage(),"Registration Error",JOptionPane.ERROR_MESSAGE);}}}.execute();
    }
    private void loadAsync(){
        String selected=(String)filter.getSelectedItem();int offset=page*PAGE_SIZE;status.setText("Loading...");
        new SwingWorker<java.util.List<Registration>,Void>(){protected java.util.List<Registration> doInBackground()throws Exception{return service.filter(selected,PAGE_SIZE,offset);}
            protected void done(){try{model.setRowCount(0);for(Registration r:get())model.addRow(new Object[]{r.id,r.studentId,r.studentName,r.eventName,r.email});status.setText("Page "+(page+1)+" loaded");}catch(Exception ex){status.setText("Unable to load registrations: "+ex.getMessage());}}}.execute();
    }
    private void exportCsv(){
        JFileChooser chooser=new JFileChooser();if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        try(PrintWriter out=new PrintWriter(chooser.getSelectedFile())){for(int r=0;r<model.getRowCount();r++){for(int c=0;c<model.getColumnCount();c++){if(c>0)out.print(",");out.print("\""+String.valueOf(model.getValueAt(r,c)).replace("\"","\"\"")+"\"");}out.println();}status.setText("CSV exported");}
        catch(IOException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Export Error",JOptionPane.ERROR_MESSAGE);}
    }
    public static void main(String[]a){SwingUtilities.invokeLater(()->new EventRegistrationApp().setVisible(true));}
}
