import java.io.IOException;
class ExamException extends Exception {
 ExamException(String m){super(m);}
 ExamException(String m,Throwable c){super(m,c);}
}
class SubmissionStorageException extends ExamException {
 SubmissionStorageException(String m,Throwable c){super(m,c);}
}
class Storage {
 void save(String fileName,long sizeKb) throws SubmissionStorageException {
  try {
   if(sizeKb>500) throw new IOException("Disk quota exceeded");
   System.out.println("Saved "+fileName);
  } catch(IOException e) {
   throw new SubmissionStorageException("Could not store "+fileName,e);
  }
 }
}
public class StorageDemo {
 public static void main(String[] args) {
  Storage s=new Storage();
  try {s.save("answer01.pdf",120);s.save("answer02.pdf",800);}
  catch(ExamException e) {
   System.out.println("Error: "+e.getMessage());
   System.out.println("Cause: "+e.getCause().getMessage());
   System.out.println("Cause type: "+e.getCause().getClass().getSimpleName());
  }
 }
}
