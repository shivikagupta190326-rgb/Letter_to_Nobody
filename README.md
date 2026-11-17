# Message in a Bottle — The Digital Ocean

A Java Swing desktop experience for releasing anonymous words into a shared ocean, discovering other people's notes, and keeping private letters for a future date.

## Features

- Anonymous public bottles, category selection, live character count, preview, confirmation, and random discovery.
- Discovery avoids repeating a bottle during a session until the other available bottles have been explored.
- Private future letters stored locally and readable once their target date arrives.
- Local sign-in keeps future letters separated by account; passwords are salted and hashed with PBKDF2. Public bottles are never linked to an account.
- Ocean statistics, sample bottles on first launch, animated ocean backdrop, and responsive Swing screens.
- SQLite persistence at `~/.message-in-a-bottle/ocean.db`; no identity fields are collected.

## Technology and structure

Java 17, Swing, JDBC, SQLite, Maven. Source packages separate models, database, services, UI, and validation utilities. Prepared statements are used for all user supplied values.

## Java concepts demonstrated

- Classes and objects: `Message`, `Bottle`, and `FutureLetter` represent application data.
- Encapsulation: model fields are private and accessed through getters.
- Inheritance: `Bottle` extends `Message`.
- Interfaces: the small `Service` interface gives services a common initialization method.
- Collections: `List`, `Set`, and `Map` hold bottles, visited IDs, and statistics.
- Exception handling: database and validation errors are shown as readable dialogs.
- Threads: open **Threads & deadlock** from the home screen. The demos show `synchronized`, consistent lock ordering, and a bounded producer-consumer queue using guarded `wait()` blocks and `notifyAll()`.
- JDBC and SQL: `DatabaseConnection` initializes SQLite; services use `DriverManager`, `Connection`, `PreparedStatement`, `executeQuery`, `executeUpdate`, `ResultSet`, and try-with-resources.
- Lambdas and streams: the category report filters, sorts, and maps collection entries before export.
- Files and collections: **Export category report as CSV** writes a UTF-8 report with exception-safe resource handling.

The concurrency examples are in `ocean.service.ConcurrencyDemo` and `ocean.service.ProducerConsumerDemo`; both are available from the Threads & deadlock screen. The CSV export is handled by `ocean.service.ReportService`. Existing future letters are assigned to the first local account created after this update.

## Run

Install JDK 17+ and Maven, then from this directory run:

```sh
mvn clean package
java -jar target/message-in-a-bottle-1.0.0.jar
```

The SQLite JDBC driver is bundled into the executable jar by Maven Shade. Database tables are created at startup. The first launch seeds three sample public messages when the bottle table is empty.

## Open and run in IntelliJ IDEA

1. Install **JDK 17 or newer** and IntelliJ IDEA.
2. In IntelliJ, choose **File → Open**, then select this project folder (the one containing `pom.xml`).
3. If IntelliJ asks how to open the project, choose **Open as Project** and allow Maven to load the dependencies.
4. Set the project SDK to JDK 17+: choose **File → Project Structure → Project**, then select the JDK.
5. Open `src/main/java/ocean/Main.java` and click the green run arrow next to `main`, or select the shared **Message in a Bottle** run configuration.

IntelliJ downloads the SQLite JDBC dependency from `pom.xml` during Maven sync. You can also run `mvn clean package` from IntelliJ's Terminal and launch the generated jar as described above.

## Privacy note

Public notes never store an account ID. Future letters are associated with the local account that saved them. Passwords are stored as salted PBKDF2 hashes. The database itself is not encrypted, so anyone with access to the same operating system account and application data folder can read its contents.
