## Use Case:04 - Top N Global Countries

**Goal in Context (User Story):** As an Analyst, I want to retrieve the top N populated countries in the world, where N is user-defined, so that I can quickly identify the most populous nations.al analysis.

**Scope:** Population Reporting System (Application and Database)

**Level:** User Goal

**Primary Actor:** Analyst

**Trigger:** Analyst selects the "Top N Global Countries" option from the application interface.

**Preconditions:**
1. The System is running (Application container is 'Up').
2. The MySQL database (`db` service) is connected and populated with the `world` schema.
3. The Analyst has appropriate permissions (assumed by the system boundary).

**Success End Condition:**
The System displays the complete Country Report (Code, Name, Continent, Region, Population, Capital) for the specified region, correctly ordered by Population (descending).

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

* **2a. [Connection Failure]:** If the database link is broken, the System prints "Failed to connect to database" (after 10 retries) and terminates the use case.
* **4a. [No Data Found]:** If the SQL query returns an empty result set (0 rows), the System prints: "No countries were retrieved from the database."
* **6a. [No Data Found]:** If the SQL query returns an empty result set (0 rows) for the valid region, the System prints: "No countries were retrieved for the specified region."