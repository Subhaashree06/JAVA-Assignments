# Java Programming Assignment 5 (CO5)
## Data Persistence Engineering – Files and Databases

**Student:** J.Subhaashree  
**Register Number:** 192424008  
**Platform used in submitted notebook:** Google Colab, Java 17, MySQL 8, MySQL Connector/J 9.5.0

## Folder structure
- `Java_CO5_Assignment5_Complete.ipynb` — original Colab notebook with recorded outputs.
- `src/` — complete Java source files extracted from every `%%writefile` cell.
- `docs/` — complete assignment report PDF.
- `screenshots/` — named output evidence images from saved notebook outputs.
- `data/` — sample, migration and malformed-record CSV inputs.
- `sql/` — database schema and setup SQL.
- `results/` — benchmark charts.
- `lib/` — place MySQL Connector/J JAR here if running locally.

## Run in Google Colab
1. Upload `Java_CO5_Assignment5_Complete.ipynb` to Google Colab.
2. Select Runtime → Run all.
3. The notebook installs Java 17 and MySQL, downloads Connector/J, creates `transport_db`, compiles the Java programs and runs the experiments.
4. Outputs in the notebook are the primary execution record.

## Run locally (Windows)
Requirements: JDK 17, MySQL 8, and MySQL Connector/J 9.5.0.
1. Start MySQL and create the database using `sql/schema.sql`.
2. Download MySQL Connector/J and place the JAR at `lib/mysql-connector-j.jar`.
3. Open Command Prompt in this folder.
4. Compile/run file-only tasks:
   `javac -d out src\PassStore.java src\BufferBenchmark.java src\GrowthBenchmark.java`
   `java -cp out PassStore`
   `java -cp out BufferBenchmark`
   `java -cp out GrowthBenchmark`
5. Compile/run JDBC tasks:
   `javac -cp "lib\mysql-connector-j.jar" -d out src\LoginSecurity.java src\JdbcBenchmarks.java src\MigrationAndMetadata.java`
   `java -cp "out;lib\mysql-connector-j.jar" LoginSecurity`
   `java -cp "out;lib\mysql-connector-j.jar" JdbcBenchmarks`
   `java -cp "out;lib\mysql-connector-j.jar" MigrationAndMetadata`

The JDBC programs use the Colab example credentials `root` / `Colab@12345`. Change the URL/user/password constants in the Java source to match your local MySQL configuration before running locally. Do not use these example credentials in a real system.

## Evidence and measurements
The screenshots folder contains image renderings of the output text saved in the submitted Colab notebook. The benchmark charts are in `results/`. Runtime timings depend on the machine and database environment and may differ on rerun.
