# A. Country Reports
## Use Case: UC06 - Top N Region Countries

**Goal in Context (User Story):** As an Analyst, I want to retrieve the top N populated countries in a specified region, where N is user-defined, so that I can focus on key sub-regional demographic areas.

**Scope:** Population Reporting System (Application and Database)

**Level:** User Goal

**Primary Actor:** Analyst

**Trigger:** Analyst selects the "Global Country Report" option from the application interface.

**Preconditions:**
1. The System is running (Application container is 'Up').
2. The MySQL database (`db` service) is connected and populated with the `world` schema.
3. The Analyst has appropriate permissions (assumed by the system boundary).

**Success End Condition:**
The System displays the Country Report (Code, Name, Continent, Region, Population, Capital) showing the top N countries for the specified region, correctly ordered by Population (descending).

**Failed Condition:**
The System displays an error message (e.g., "Invalid N value," "Region not found," "Database connection failed") and returns to the report menu.

### Main Success Scenario

1. **Analyst** selects the Top N Region Countries option.
2. **System** prompts the Analyst to enter the Region Name and the integer value for N.
3. **Analyst** inputs the Region Name and value N and submits.
4. **System** validates N is a positive integer and verifies the Region Name exists.
5. **System** executes the connection check (connect() method).
6. **System** runs the SQL query: `SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region = '<Region Input>' ORDER BY Population DESC LIMIT N;`
7. **System** receives the result set and maps the data to Country objects.
8. **System** calls the displayCountriesFiltered() method.
9. **System** displays the Top N, formatted report table to the Analyst.

### Extensions (Alternative Flows)

* **4a. [Invalid Input N]:** If the Analyst enters N as non-numeric or less than 1, the System prints: "Error: N must be a positive integer." and returns to Step 2.
* **4b. [Invalid Region]:** If the Analyst enters a Region name that does not exist, the System prints: "Error: Invalid Region Name entered." and returns to Step 2.
* **5a. [Connection Failure]:** If the database link is broken, the System prints "Failed to connect to database" (after 10 retries) and terminates the use case.
* **7a. [Fewer than N Results]:** If the query returns fewer than N results for the region, the System displays all available results and prints a warning: "Warning: Only X countries were retrieved for [Region]."

