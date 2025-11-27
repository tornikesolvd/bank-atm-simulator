
### 1. `create(T entity)` → `T`
**Purpose:** Creates a new entity in the database.

**Behavior:**
- Inserts the entity into the database
- Automatically generates and sets the `id` field if it's auto-generated
- Commits the transaction on success
- Rolls back the transaction on error
- Returns the entity with the generated `id` set

**Example Usage:**

Person person = new Person();
person.setFullName("John Doe");
person.setEmail("john@example.com");
person.setPhoneNumber("1234567890");

PersonRepositoryImpl personRepo = new PersonRepositoryImpl();
Person createdPerson = personRepo.create(person);
// createdPerson.getId() will now contain the database-generated ID
```

---

### 2. `findById(Long id)` → `Optional<T>`
**Purpose:** Retrieves an entity by its primary key (ID).

**Behavior:**
- Returns `Optional.empty()` if no entity with the given ID exists
- Returns `Optional.of(entity)` if found
- Does not modify the database (read-only operation)

**Example :**

PersonRepositoryImpl personRepo = new PersonRepositoryImpl();
Optional<Person> personOpt = personRepo.findById(1L);

if (personOpt.isPresent()) {
    Person person = personOpt.get();
    // Use person...
} else {
    // Handle not found case
}
```

**Common Pattern:**

Person person = personRepo.findById(id)
    .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
```

---

### 3. `findAll()` → `List<T>`
**Purpose:** Retrieves all entities of the given type from the database.

**Behavior:**
- Returns an empty list if no entities exist
- Returns a list containing all entities
- Does not modify the database (read-only operation)

**Example Usage:**

PersonRepositoryImpl personRepo = new PersonRepositoryImpl();
List<Person> allPersons = personRepo.findAll();

for (Person person : allPersons) {
    // Process each person...
}
```

---

### 4. `update(T entity)` → `T`
**Purpose:** Updates an existing entity in the database.

**Behavior:**
- Updates the entity based on its `id` field
- The entity must have a valid `id` set (not null)
- Commits the transaction on success
- Rolls back the transaction on error
- Returns the updated entity

**Example:**

PersonRepositoryImpl personRepo = new PersonRepositoryImpl();
Optional<Person> personOpt = personRepo.findById(1L);

if (personOpt.isPresent()) {
    Person person = personOpt.get();
    person.setEmail("newemail@example.com");
    Person updatedPerson = personRepo.update(person);
}
```

---

### 5. `delete(Long id)` → `void`
**Purpose:** Deletes an entity from the database by its ID.

**Behavior:**
- Deletes the entity with the given ID
- Commits the transaction on success
- Rolls back the transaction on error
- Does not throw an exception if the entity doesn't exist (silently succeeds)

**Example Usage:**
```java
PersonRepositoryImpl personRepo = new PersonRepositoryImpl();
personRepo.delete(1L);
```

---

## Repository Usage Pattern

### Basic Pattern for Service Layer

```java
public class PersonService {
    private final IPersonRepository personRepository;
    
    public PersonService() {
        this.personRepository = new PersonRepositoryImpl();
    }
    
    public Person createPerson(Person person) {
        // Validation logic here
        return personRepository.create(person);
    }
    
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    }
    
    public Person updatePerson(Long id, Person updatedPerson) {
        Person existingPerson = getPersonById(id);
        // Update fields
        existingPerson.setFullName(updatedPerson.getFullName());
        existingPerson.setEmail(updatedPerson.getEmail());
        return personRepository.update(existingPerson);
    }
    
    public void deletePerson(Long id) {
        getPersonById(id); // Verify exists
        personRepository.delete(id);
    }
}
```

---

## Repository-Specific Methods

Each repository may have additional methods beyond the standard CRUD. Here are the most commonly used ones:

### PersonRepository
- `findByEmail(String email)` → `Optional<Person>` - Find person by email address
- `findByPhoneNumber(String phoneNumber)` → `Optional<Person>` - Find person by phone number

### AccountRepository
- `findByAccountNumber(String accountNumber)` → `Optional<Account>` - Find account by account number
- `findByPersonId(Long personId)` → `List<Account>` - Find all accounts for a person
- `findByCurrency(String currency)` → `List<Account>` - Find all accounts with specific currency

### TransactionRepository
- `findByAccountId(Long accountId)` → `List<Transaction>` - Find all transactions for an account
- `findByType(Transaction.TransactionType type)` → `List<Transaction>` - Find transactions by type
- `findByStatus(Transaction.TransactionStatus status)` → `List<Transaction>` - Find transactions by status

### PaymentCardRepository
- `findByCardNumber(String cardNumber)` → `Optional<PaymentCard>` - Find card by card number
- `findByAccountId(Long accountId)` → `List<PaymentCard>` - Find all cards for an account
- `findByStatus(PaymentCard.CardStatus status)` → `List<PaymentCard>` - Find cards by status
- `findExpiredCards()` → `List<PaymentCard>` - Find all expired cards


---

## Example: Complete Service Layer Method

public class AccountService {
    private final IAccountRepository accountRepository;
    private final IPersonRepository personRepository;
    
    public AccountService() {
        this.accountRepository = new AccountRepositoryImpl();
        this.personRepository = new PersonRepositoryImpl();
    }
    
    public Account createAccount(Long personId, Account account) {
        // 1. Verify person exists
        Person person = personRepository.findById(personId)
            .orElseThrow(() -> new EntityNotFoundException("Person not found"));
        
        // 2. Set person reference
        account.setPersonId(personId);
        
        // 3. Create account (transaction handled automatically)
        Account createdAccount = accountRepository.create(account);
        
        // 4. Return created account with ID set
        return createdAccount;
    }
    
    public List<Account> getAccountsByPerson(Long personId) {
        // Read-only operation, no transaction needed
        return accountRepository.findByPersonId(personId);
    }
    
    public Account updateAccountBalance(Long accountId, BigDecimal newBalance) {
        // 1. Get existing account
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        
        // 2. Update balance
        account.setBalance(newBalance);
        
        // 3. Save (transaction handled automatically)
        return accountRepository.update(account);
    }
}
```




