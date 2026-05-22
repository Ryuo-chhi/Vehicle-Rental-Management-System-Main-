# Team Execution Guide: Vehicle Rental Interface Refactoring

Here are **3 different ways** your team of 4 can split this work. Choose the one that best fits your team's style.

---

## Option 1: The "Specialist" Split (Role-Based)
*Best if you want to work independently with minimal file conflicts.*

- **Member 1 (The Architect)**:
  -   **Task**: Create `IStaff.java` and `ActionConstants.java`.
  -   **Why**: Defines the rules everyone else follows.
- **Member 2 (The Manager Lead)**:
  -   **Task**: Create `Admin.java` and `Manager.java`.
  -   **Why**: Implements the "Boss" logic.
- **Member 3 (The Staff Lead)**:
  -   **Task**: Create `Clerk.java` and update `Garage.java` data generation.
  -   **Why**: Implements the "Worker" logic and creates the test users.
- **Member 4 (The Integrator)**:
  -   **Task**: Refactor `Garage.java` to use the new list and add Login/Logout.
  -   **Why**: Connects all the pieces together.

---

## Option 2: The "Functional" Split (Layer-Based)
*Best if you have one person who is very good at logic and others who prefer data/UI.*

- **Member 1 (Data Layer)**:
  -   **Task**: Writes ALL new files (`IStaff`, `Admin`, `Manager`, `Clerk`).
  -   **Focus**: Pure Java classes, no logic complexity.
- **Member 2 (Logic Layer)**:
  -   **Task**: Modifies `Garage.java` to change the list info `ArrayList<IStaff>` and writes the `login()` method.
  -   **Focus**: Core system mechanics.
- **Member 3 (Security Layer)**:
  -   **Task**: Goes through `Garage.java` and wraps every method (addVehicle, removeCustomer, etc.) with `if (!user.can(...))`.
  -   **Focus**: Enforcing rules.
- **Member 4 (UI & Testing)**:
  -   **Task**: Updates `Main.java` loops and runs the manual tests to verified everything works.
  -   **Focus**: User experience and quality assurance.

---

## Option 3: Two-Pair Split (Collaborative)
*Best for learning and catching bugs early.*

- **Pair A (Member 1 & 2)**: **The Builders**
  -   Sit together (or share screen).
  -   Build `IStaff` and all 3 implementation classes (`Admin`, `Manager`, `Clerk`) together.
  -   **Result**: complete, bug-free data models.
- **Pair B (Member 3 & 4)**: **The Integrators**
  -   Sit together.
  -   Refactor `Garage` and `Main`. One types, one spots errors.
  -   **Result**: A working system with login.

---

## Technical Cheatsheet (For All Options)

### 1. The Interface (`IStaff.java`)
```java
public interface IStaff {
    String getId();
    String getUsername();
    String getRole();
    boolean can(String action);
}
```

### 2. The Constants (Action Names)
Use these exact strings to avoid spelling errors:
- `"ADD_VEHICLE"`, `"REMOVE_VEHICLE"`, `"UPDATE_VEHICLE"`
- `"ADD_CUSTOMER"`, `"REMOVE_CUSTOMER"`
- `"ADD_RENT"`, `"RETURN_VEHICLE"`

### 3. The Permission Check
In `Garage.java`, whenever a user tries to do something:
```java
public void addVehicle(Scanner scanner) {
    if (loggedInUser == null || !loggedInUser.can("ADD_VEHICLE")) {
        System.out.println("Access Denied.");
        return;
    }
    // ... rest of code
}
```
