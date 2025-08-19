## SQL Script

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS FoodDelivery;
USE FoodDelivery;

-- Create Orders table
CREATE TABLE IF NOT EXISTS Orders (
    order_id INT PRIMARY KEY,
    order_time INT NOT NULL,
    travel_time INT NOT NULL
);

-- Create Drivers table
CREATE TABLE IF NOT EXISTS Drivers (
    driver_id INT PRIMARY KEY,
    free_at INT DEFAULT 0
);

-- Create Assignments table
CREATE TABLE IF NOT EXISTS Assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    driver_id INT,
    status VARCHAR(50),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (driver_id) REFERENCES Drivers(driver_id)
);

-- Display results
SELECT * FROM Orders;
SELECT * FROM Drivers;
SELECT * FROM Assignments;
