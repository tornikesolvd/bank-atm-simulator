# Bank ATM Simulator

A simplified banking application that simulates accounts and transactions for a small user base.

## Features

- Account management with multiple currencies
- Transaction processing (deposits, withdrawals, transfers)
- Money conservation: total sum across all accounts remains constant
- Atomic and consistent transactions
- ATM with specific currencies and banknotes
- Banknote selection for withdrawals

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Database Setup

1. Make sure MySQL is running on your machine
2. Update database credentials in `src/main/resources/config.properties`:
   ```
   url=jdbc:mysql://localhost:3306/bank_atm_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   username=your_username
   password=your_password
   ```
3. **Option 1: Automatic Setup (Recommended)**
   Run the database setup utility from IntelliJ:
   - Right-click on `DatabaseSetup.java` → Run 'DatabaseSetup.main()'
   - Or use Maven: `mvn exec:java -Dexec.mainClass="com.solvd.bankatmsimulator.persistence.DatabaseSetup"`

4. **Option 2: Manual Setup**
   Run the SQL script manually:
   ```sql
   mysql -u your_username -p < src/main/resources/database-schema.sql
   ```
   Or execute the SQL script in MySQL Workbench or your preferred MySQL client.

## Building the Project

```bash
mvn clean compile
```

## Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.solvd.bankatmsimulator.BankATMApp"
```

## Project Structure

```
src/main/java/com/solvd/bankatmsimulator/
├── domain/          # Domain models (Account, Transaction, Banknote, etc.)
├── persistence/     # Data access layer (DAOs, ConnectionPool)
├── service/         # Business logic layer
└── BankATMApp.java  # Main application class
```

## Database Connection

The project uses a custom connection pool. Database configuration is managed through:
- `src/main/resources/config.properties` - Database connection settings
- `com.solvd.bankatmsimulator.persistence.Config` - Configuration enum
- `com.solvd.bankatmsimulator.persistence.ConnectionPool` - Connection pool manager (singleton pattern)
- `com.solvd.bankatmsimulator.persistence.DatabaseSetup` - Database setup utility

## Team Members

- Tornike (Team Lead)

