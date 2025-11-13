# A. Country Reports
## Use Case: UC02 - Continent Country Report

**Goal in Context (User Story):** As an Analyst, I want to retrieve a report of all countries in a specified continent sorted by population so that I can focus on regional demographics.

**Scope:** Population Reporting System (Application and Database)

**Level:** User Goal

**Primary Actor:** Analyst

**Trigger:** Analyst selects the "Continent Country Report" option from the application interface.

**Preconditions:**
1. The System is running (Application container is 'Up').
2. The MySQL database (`db` service) is connected and populated with the `world` schema.
3. The Analyst has appropriate permissions (assumed by the system boundary).

**Success End Condition:**
The System displays the complete Country Report (Code, Name, Continent, Region, Population, Capital) for the specified continent, correctly ordered by Population (descending).

**Failed Condition:**
The System displays an error message (e.g., "Invalid Continent name," "Database connection failed") and returns to the report menu.

### Main Success Scenario

1. Analyst selects the Continent Country Report option.
2. System prompts the Analyst to enter the desired Continent Name (e.g., 'Asia').
3. Analyst inputs the Continent Name and submits.
4. System executes the connection check (connect() method).
5. System runs the SQL query: SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Continent = '<Input>' ORDER BY Population DESC;
6. System receives the result set and maps the data to Country objects.
7. System calls the displayCountriesFiltered() method.
8. System displays the full, formatted report table to the Analyst.

### Extensions (Alternative Flows)

* **2a. [Invalid Input]:** If the Analyst enters a Continent name that does not exist in the database, the System prints: "Error: Invalid Continent Name entered." and returns to Step 2.
* **4a. [Connection Failure]:** If the database link is broken, the System prints "Failed to connect to database" (after 10 retries) and terminates the use case.
* **6a. [No Data Found]:** If the SQL query returns an empty result set (0 rows) for the valid continent, the System prints: "No countries were retrieved for the specified continent."