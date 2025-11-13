# B. City Reports & City Sub-Reports
## Use Case: UC09 - Region City Report

**Goal in Context (User Story):** As an Analyst, I want to retrieve a report of all cities in a specified region sorted by largest population to smallest so that I can analyze urban centers in that are

**Scope:** Population Reporting System (Application and Database)

**Level:** User Goal

**Primary Actor:** Analyst

**Trigger:** Analyst selects the "Region City Report" option from the application interface.

**Preconditions:**
1. The System is running (Application container is 'Up').
2. The MySQL database (db service) is connected and populated with the world schema.
3.The Analyst has appropriate permissions (assumed by the system boundary).

**Success End Condition:**
The System displays the complete City Report (Name, Country, District, Population) for cities in the specified region, correctly ordered by Population (descending).

**Failed Condition:**
The System displays an error message (e.g., "Invalid Region name," "Database connection failed") and returns to the report menu.

### Main Success Scenario

1.  **Analyst** selects the Global Country Report option.
2.  **System** executes the connection check (`connect()` method).
3.  **System** runs the SQL query: `SELECT Code, Name, Continent, Region, Population, Capital FROM country ORDER BY Population DESC;`
4.  **System** receives the complete result set and maps the data to `Country` objects.
5.  **System** calls the `displayCountries()` method.
6.  **System** displays the full, formatted report table to the Analyst.



### Extensions (Alternative Flows)

* **3a. [Invalid Input]:** If the Analyst enters a Region name that does not exist, the System prints: "Error: Invalid Region Name entered." and returns to Step 2.
* **4a. [Connection Failure]:** If the database link is broken, the System prints "Failed to connect to database" (after 10 retries) and terminates the use case.
* **6a. [No Data Found]:** If the SQL query returns an empty result set (0 rows), the System prints: "No cities were retrieved for the specified region."