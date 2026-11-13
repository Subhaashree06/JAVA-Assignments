# Programming in Java – Assignment 4 (CO4)
**Name:** J. Subhaashree  
**Register Number:** 192424008  
**Course:** Programming in Java (CSA09)  
**Course Outcome:** CO4 – Event-Driven GUI Design

## Java version
Java version used for compilation: see `logs/compile_results.txt` and the Colab runtime output. Desktop execution requires a JDK with AWT/Swing support.

## Task folders
- Task01 – Fee-payment GUI design review and repaired FeeWindow/FeeService.
- Task02 – Appointment desk event model, component hierarchy, pseudocode and appointment service.
- Task03 – Campus library search GUI and layout design.
- Task04 – DrawPad event repair, persistent painting and @Override compile-error demonstration.
- Task05 – Currency converter in frame listener, named inner class, anonymous class and lambda styles.
- Task06 – Layered Swing–JDBC event registration application and MySQL SQL script.

## Compile and run (desktop)
Run commands from the corresponding task folder:
- Task01: `javac FeeWindow.java` then `java FeeWindow`
- Task02: `javac AppointmentDesk.java` then `java AppointmentDesk`
- Task03: `javac LibrarySearch.java` then `java LibrarySearch`
- Task04: `javac DrawPad.java` then `java DrawPad`
- Task05: compile the selected file and run its class:
  - `javac ConverterFrameListener.java` / `java ConverterFrameListener`
  - `javac ConverterNamedInner.java` / `java ConverterNamedInner`
  - `javac ConverterAnonymous.java` / `java ConverterAnonymous`
  - `javac Converter.java` / `java Converter`
- Task06: configure MySQL and Connector/J, execute `sql/campus_events.sql`, set `EVENT_DB_URL`, `EVENT_DB_USER`, `EVENT_DB_PASSWORD`, then compile and run `EventRegistrationApp.java`.

## Database setup
The SQL script creates `campus_events.registrations` and a UNIQUE(student_id,event_name) constraint to prevent duplicate registration.

## Execution notes
AWT/Swing GUI source files are compiled in the notebook. Colab is headless, so interactive windows and screenshots must be captured on a desktop. Task02 service and Task05 conversion logic have console verification tests. Task04 includes the expected compile-time error caused by misspelling `mouseClicked` when @Override is used. Task06 requires a running MySQL server and MySQL Connector/J at runtime.
