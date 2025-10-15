# Use a standard MySQL image
FROM mysql:8.0

# Set the root password and default database name
ENV MYSQL_ROOT_PASSWORD example
ENV MYSQL_DATABASE world

# Copy the local SQL file into the Docker initialization directory.
# The source path 'world-db/world.sql' is relative to the 'db/' folder.
COPY world-db/world.sql /docker-entrypoint-initdb.d/

# Set the host to accept all connections (good for Docker networking)
ENV MYSQL_ROOT_HOST=%