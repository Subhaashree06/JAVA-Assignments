# CSA09 Java Assignment 2 — CO2

## Student details
- Name: J.Subhaashree
- Register Number: 192424008
- Course: B.Tech Artificial Intelligence and Data Science
- Java version used in Colab: OpenJDK 17 (as installed by the notebook)

## Project structure
- `src/Assignment2.java` — Java source for Q1–Q7
- `data/` — CSV benchmark data extracted from the uploaded executed Colab notebook
- `graphs/` — performance graphs generated from the available benchmark data
- `screenshots/` — seven output evidence panels reconstructed from the notebook's recorded execution output
- `reports/` — assignment report in DOCX and PDF
- `Java_CO2_Assignment2_Complete.ipynb` — executed Google Colab notebook supplied by the student

## Compile and run
In Google Colab, run the notebook cells in order. For a local Java 17 environment:
```bash
javac src/Assignment2.java
java -cp src Assignment2
```
The program writes per-question CSV files into the current working directory.

## Task results (from the uploaded notebook execution)
- Q1: StringBuilder was faster than String and StringBuffer in the three recorded workloads.
- Q2: List structure timings were displayed for a preview of the first 15 rows only; download the full `q2_measurements.csv` from the Colab runtime to replace the preview CSV before final submission.
- Q3: Broken equals/hashCode example retained two equal-ID objects; corrected Student class produced 2 unique students.
- Q4: HashMap, TreeMap and LinkedHashMap lookup/report timings were measured at 1,000, 5,000 and 10,000 medicines.
- Q5: Ranking output matched the expected order.
- Q6: PriorityQueue produced the required severity-first, arrival-order treatment sequence.
- Q7: ArrayDeque front insertion was faster than ArrayList in the four recorded sizes.

## Important evidence note
The uploaded notebook contains recorded execution output and rendered tables. The Q2 table in the notebook displays `head(15)` only, so `data/Q2_List_Benchmarks_PREVIEW.csv` is explicitly a preview, not the complete Q2 raw dataset. Download the full Q2 CSV from the Colab Files pane and replace it before submission. The screenshot PNGs are reconstructed output evidence panels based on recorded notebook text, not literal screen captures of the browser UI. For a strict screenshot requirement, take screenshots directly in Colab and replace these panels.

## GitHub submission
Create a repository named `CSA09-<RegisterNumber>-Assignment2`, then run:
```bash
git init
git add .
git commit -m "Add Q1 Java string benchmark"
git add .
git commit -m "Add Q2 collection benchmark"
git add .
git commit -m "Add Q3 hashCode equals experiment"
git add .
git commit -m "Add Q4 map benchmark"
git add .
git commit -m "Add Q5 generic leaderboard"
git add .
git commit -m "Add Q6 priority queue triage"
git add .
git commit -m "Add Q7 redesign validation and report"
git branch -M main
git remote add origin https://github.com/<your-username>/CSA09-<RegisterNumber>-Assignment2.git
git push -u origin main
```
Use meaningful progressive commits while doing the work; do not create all commits on the final day merely to imitate progress. Replace placeholders and share the repository link with faculty.
