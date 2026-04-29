DROP DATABASE IF EXISTS event_manager;
CREATE DATABASE event_manager;
USE event_manager;
CREATE TABLE participants (
    id int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COLLATE utf8mb4_general_ci,
    email VARCHAR(255) UNIQUE NOT NULL,
    participant_type VARCHAR(20) NOT NULL, -- This will store 'Individual' or 'Organization'
    representative_name VARCHAR(255)       -- This can be NULL for Individuals
);
CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0)
);
CREATE TABLE invitations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    participant_id INT NOT NULL,
    status ENUM('PENDING','ACCEPTED','DECLINED') DEFAULT 'PENDING',
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,     -- Link to other tables
    FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE,
    UNIQUE(event_id, participant_id)    -- Constraint: A participant cannot be invited to the same event twice
);
SELECT *  FROM invitations;
INSERT INTO invitations (participant_id, event_id, status) VALUES (1, 1, 'ACCEPTED');

TRUNCATE TABLE participants;


