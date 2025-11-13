# A. Country Reports
## Use Case: UC04 - Top N Global Countries

**Goal in Context (User Story):** As an Analyst, I want to retrieve the top N populated countries in the world, where N is user-defined, so that I can quickly identify the most populous nations.

**Scope:** Population Reporting System (Application and Database)

**Level:** User Goal

**Primary Actor:** Analyst

**Trigger:** Analyst selects the "Top N Global Countries" option from the application interface.

**Preconditions:**
1. The System is running (Application container is 'Up').
2. The MySQL database (db service) is connected and populated with the world schema.
3. The Analyst has appropriate permissions (assumed by the system boundary).

**Success End Condition:**
The System displays the Country Report (Code, Name, Continent, Region, Population, Capital) showing only the top N countries globally, correctly ordered by Population (descending).

**Failed Condition:**
The System displays an error message (e.g., "Invalid N value," "Database connection failed") and returns to the report menu.

### Main Success Scenario

1.  **Analyst** selects the Top N Global Countries option.
2.  **System** prompts the Analyst to enter the integer value for N (the number of top countries to display).
3.  **Analyst** inputs the value N and submits.
4.  **System** validates N is a positive integer.
5.  **System** executes the connection check (connect() method).
6.  **System** runs the SQL query: `SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC LIMIT N;`
7.  **System** receives the result set and maps the data to Country objects.
8.  **System** calls the displayCountriesFiltered() method.
9.  **System** displays the Top N, formatted report table to the Analyst.

### Extensions (Alternative Flows)

* **3a. [Invalid Input N]:** If the Analyst enters N as non-numeric or less than 1, the System prints: "Error: N must be a positive integer." and returns to Step 2.
* **5a. [Connection Failure]:** If the database link is broken, the System prints "Failed to connect to database" (after 10 retries) and terminates the use case.
* **7a. [Fewer than N Results]:** If the query returns fewer than N results, the System displays all available results and prints a warning: "Warning: Only X countries were retrieved."
