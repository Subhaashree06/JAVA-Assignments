
import java.util.*;
class Doctor {
    final String id, name;
    Doctor(String id,String name){this.id=id;this.name=name;}
    public String toString(){return name;}
}
class AppointmentService {
    private final Map<String,List<Doctor>> doctorsByDepartment = new HashMap<>();
    AppointmentService(){
        doctorsByDepartment.put("Cardiology",Arrays.asList(new Doctor("C1","Dr Rao"),new Doctor("C2","Dr Iyer")));
        doctorsByDepartment.put("Orthopaedics",Arrays.asList(new Doctor("O1","Dr Kumar"),new Doctor("O2","Dr Das")));
        doctorsByDepartment.put("Paediatrics",Arrays.asList(new Doctor("P1","Dr Meena")));
    }
    List<Doctor> doctorsFor(String department){return doctorsByDepartment.getOrDefault(department,Collections.emptyList());}
    String book(String patientId,String department,String doctor,String slot){
        if(patientId==null||patientId.trim().isEmpty()) throw new IllegalArgumentException("Patient ID is required");
        if(department==null||department.isEmpty()||doctor==null||doctor.isEmpty()||slot==null||slot.isEmpty())
            throw new IllegalArgumentException("Select department, doctor and time slot");
        return "Appointment confirmed: "+patientId+" | "+department+" | "+doctor+" | "+slot;
    }
}
public class AppointmentDesk {
    public static void main(String[] args){
        AppointmentService s=new AppointmentService();
        System.out.println("Cardiology doctors: "+s.doctorsFor("Cardiology"));
        System.out.println(s.book("P1001","Cardiology","Dr Rao","10:30"));
        try{s.book("","Cardiology","Dr Rao","10:30");}
        catch(IllegalArgumentException e){System.out.println("Validation: "+e.getMessage());}
    }
}
