#!/bin/bash

set -e

SQLCMD="/opt/mssql-tools/bin/sqlcmd"

echo "Waiting for SQL Server..."

until "$SQLCMD" \
    -S sqlserver \
    -U sa \
    -P "$MSSQL_SA_PASSWORD" \
    -C \
    -Q "SELECT 1"
do
    echo "SQL Server is not ready..."
    sleep 2
done

echo "SQL Server is ready."

for DB in auth_db task_db notification_db
do
    echo "Checking database: $DB"

    "$SQLCMD" \
        -S sqlserver \
        -U sa \
        -P "$MSSQL_SA_PASSWORD" \
        -C \
        -Q "
        IF DB_ID('$DB') IS NULL
        BEGIN
            CREATE DATABASE [$DB];
            PRINT 'Created database $DB';
        END
        ELSE
        BEGIN
            PRINT 'Database $DB already exists';
        END
        "
done

echo "Database initialization completed."