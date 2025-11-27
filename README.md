# Bank ATM Simulator

A simplified banking application that simulates accounts and transactions for a small user base.

## Features

- Account management with multiple currencies
- Transaction processing (deposits, withdrawals, transfers)
- Money conservation: total sum across all accounts remains constant
- Atomic and consistent transactions
- ATM with specific currencies and banknotes
- Banknote selection for withdrawals

## Project Structure

```
src/main/java/com/solvd/bankatmsimulator/
├── domain/          # Domain models (Account, Transaction, Banknote, etc.)
├── persistence/     # Data access layer (DAOs, ConnectionPool)
├── service/         # Business logic layer
└── BankATMApp.java  # Main application class
```

## Team Members

- Tornike Aladashvili (Team Lead)
- Kenan Iusubov
- Saba Morchilashvili

