# How Our Bank ATM Simulator Works

Hey there! So you want to understand how this project is structured? Let me break it down for you in a way that makes
sense.

## The Big Picture

Think of our application like a restaurant. You have:

- **Domain classes** = The actual food (the real things we're working with)
- **Repository classes** = The kitchen staff who handle storing and retrieving things
- **Service classes** = The waiters who coordinate everything and make sure business rules are followed

## Domain Classes - The Real Stuff

Domain classes are basically just Java objects that represent real-world things in our banking system. They're pretty
simple - just data holders with getters and setters.

**Account** - This is what you'd expect. It has an account number, a balance, what currency it's in (USD, EUR, etc.),
and timestamps for when it was created and last updated. Nothing fancy here, just the basic info about a bank account.

**Transaction** - Every time money moves around, we create a transaction. It tracks where the money came from (
`fromAccountId`), where it's going (`toAccountId`), how much, what currency, what type of transaction (deposit,
withdrawal, or transfer), and its status (pending, completed, failed, etc.). There's also a `processedAt` timestamp so
we know when it happened.

**ATM** - Represents an actual ATM machine. It has a location, a name, and whether it's currently active or not. Pretty
straightforward.

**Withdrawal** and **Deposit** - These are more specific transaction types. A withdrawal has an account ID, transaction
ID, ATM ID, currency, total amount, and a list of banknotes that were dispensed. Same idea for deposits - it tracks
which banknotes were deposited.

**Person** - Just basic customer info - name, email, phone number. Can be linked to an account.

**PaymentCard** - Represents a payment card linked to an account.

The domain classes also have some constants. Like `Withdrawal` has `MIN_AMOUNT` set to $5.00, meaning you can't withdraw
less than five bucks. Makes sense, right?

## Repository Classes - The Database Workers

Repositories are where we actually talk to the database. They're the ones writing SQL queries and handling all that JDBC
stuff.

Every repository implements an interface (like `IAccountRepository`, `ITransactionRepository`, etc.) that defines basic
CRUD operations: create, find by ID, find all, update, and delete. Some have extra methods specific to what they do -
like `IAccountRepository` can find accounts by account number or by currency.

The implementations (like `AccountRepositoryImpl`) are where the real work happens. They:

- Get a database connection from our connection pool
- Write SQL queries (INSERT, SELECT, UPDATE, DELETE)
- Map database rows to our domain objects
- Handle transactions (commit on success, rollback on failure)
- Close connections properly

For example, when you call `AccountRepositoryImpl.create()`, it:

1. Gets a connection from the pool
2. Prepares an INSERT statement
3. Sets all the values from the Account object
4. Executes the query
5. Gets the auto-generated ID back
6. Commits the transaction
7. Returns the Account object with its new ID

If something goes wrong, it rolls back and throws an exception with details about what happened.

The mapping part is important - databases return `ResultSet` objects with raw data, so we have methods like
`mapResultSetToAccount()` that take that raw data and turn it into a nice Account object with all the right types.

## Service Classes - The Business Logic Coordinators

Services are where the business rules live. They sit between the application code (like `BankATMApp`) and the
repositories.

A service class:

- Validates data before it goes to the database
- Coordinates multiple repository calls if needed
- Handles business logic (like checking if someone has enough balance before a withdrawal)
- Throws meaningful exceptions when things go wrong

For example, `AccountServiceImpl` has a `register()` method that:

1. Calls `validateForCreate()` to check if the account data is valid
2. Calls `repository.create()` to actually save it
3. Returns the created account

The validation methods check things like:

- Is the account number not empty?
- Is the balance not negative?
- Is the currency one we support?
- Does the account number already exist?

Services also have methods like `getById()` that not only call the repository but also check if the ID is valid and
throw a nice exception if the account doesn't exist, instead of returning null or an empty Optional.

Some services are more complex. Like `TransactionServiceImpl` might need to:

- Create a transaction record
- Update account balances
- Handle rollbacks if something fails

## How They Work Together

Here's a typical flow when you want to create an account:

1. **BankATMApp** calls `accountService.register(newAccount)`
2. **AccountServiceImpl** validates the account data
3. **AccountServiceImpl** calls `repository.create(account)`
4. **AccountRepositoryImpl** writes to the database
5. **AccountRepositoryImpl** returns the account with its new ID
6. **AccountServiceImpl** returns it back to **BankATMApp**

The service layer is like a safety net - it makes sure bad data never reaches the database, and it gives you helpful
error messages instead of cryptic SQL exceptions.

## Why This Structure?

This three-layer approach (domain, repository, service) is pretty standard because:

- **Separation of concerns** - Each layer has one job. Domain = data, Repository = database, Service = business logic
- **Easy to test** - You can mock repositories when testing services
- **Easy to change** - Want to switch from MySQL to PostgreSQL? Just change the repository implementations
- **Reusable** - Multiple services can use the same repository
- **Maintainable** - When something breaks, you know exactly where to look

## Validators - The Extra Safety Check

We also have validator classes (like `AccountValidator`, `WithdrawalValidator`) that provide static methods to check if
objects are valid. Services use these to make sure data is good before doing anything with it.

## Exceptions - When Things Go Wrong

Each domain has its own exception class (like `AccountException`, `TransactionException`). These provide factory methods
to create specific exceptions with helpful messages. Instead of throwing generic `RuntimeException`, we throw things
like `AccountException.notFound(id)` which is way more helpful when debugging.

## Connection Pool - The Database Manager

`ConnectionPool` uses HikariCP to manage database connections efficiently. Instead of creating a new connection every
time (which is slow), it keeps a pool of connections ready to use. When you need one, you grab it from the pool, use it,
and put it back. Much faster and more efficient.

---

