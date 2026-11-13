# Assignment 1 — Campus Smart-Parking System (CO1)

## Folder contents
- `Task01/` … `Task12/`: separate task folders. Each contains its Java source file(s), execution log, and `OutputScreenshots/` evidence images where a program was executed.
- `ExperimentalData/`: Java benchmark source and measured CSV results.
- `PerformanceGraphs/`: PNG performance graphs generated from the CSV.
- `AssignmentReport/`: assignment report PDF.
- `README.md`: this file.

## Java environment
Executed using OpenJDK 17. Google Colab was used as the execution environment.

## Compile and run
For a normal task folder:
```bash
cd Task03
javac ParkingFacility.java
java ParkingFacility
```
Use the matching source filename and public class name for each task.

Task 10 uses a package:
```bash
cd Task10
javac -d . parking/model/ParkingSlot.java ParkingSlotApp.java
java ParkingSlotApp
```

Benchmark:
```bash
cd ExperimentalData
javac LookupBenchmark.java
java LookupBenchmark
```
The benchmark writes comma-separated results to standard output. The included CSV is the captured measured run.

## Experimental data notes
The CSV records one local Java benchmark run comparing a linear scan with HashMap lookup for registration numbers. It is a measured experiment, not fabricated sample data. Timing is hardware/JVM dependent and should be repeated for a stronger statistical claim. The experiment is relevant to the Task 11 discussion of alternative active-ticket storage structures; it does not measure the complete parking system.

## Screenshot notes
`OutputScreenshots/` contains rendered evidence panels created from the Java execution logs in this package. They preserve the console output, but they are not direct screenshots of the Google Colab browser window. If the instructor specifically requires screenshots captured directly from Colab, replace or supplement these panels with screenshots from your own Colab run.

## GitHub submission
The assignment brief requires a repository named `CSA09-<RegisterNumber>-Assignment1`, a README, progressive commits, and a working repository link. This ZIP is prepared for upload, but no remote GitHub repository has been created or pushed from this session.

1. Sign in to GitHub and create a repository named `CSA09-<YourRegisterNumber>-Assignment1`.
2. Upload the contents of this `Assignment 1` folder (not the ZIP file itself).
3. Commit each task folder progressively; do not make only one final commit.
4. Add the faculty as a collaborator if the repository is private.
5. Paste the final repository URL into your report and submit it.

## Student details to complete
- Name: __________________________
- Register number: __________________________
- GitHub repository URL: __________________________

## Important academic note
Tasks 1 and 2 are primarily analysis/design tasks in the assignment brief, so their source files are small runnable demonstrations of the concepts, not substitutes for the required written discussion. Review the report and original question paper, add your own explanations where needed, and make sure you can explain every program during viva.
