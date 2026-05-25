# Community Center Event Management System

A Java-based console application designed for community centers to organize workshops, meetups, and trainings. This project manages participants, event scheduling, and invitation tracking using a robust OOP architecture and SQL integration.

## 📋 Scenario
The application allows staff members to create events with specific capacities and locations. Participants (individuals or organizations) can be invited, and the system tracks their response status (PENDING, ACCEPTED, DECLINED) while ensuring no overbooking or duplicate invitations occur.

## ✨ Key Features
- **Participant Management:** Register individuals and organizational representatives.
- **Event Coordination:** Manage titles, descriptions, time ranges, and locations.
- **Invitation Logic:** 
    - Track invitation statuses.
    - Prevent duplicate invites for the same participant/event.
    - Automatic capacity validation (prevents accepting more participants than the limit).
- **Dynamic Views:** Filter upcoming events and attendance lists using Java Streams and Lambdas.
- **Data Persistence:** Full SQL + JDBC integration for reliable data storage.

## 🛠️ Tech Stack
- **Language:** Java 17+
- **Database:** MySQL 8.0+ (via JDBC)
- **API:** JDBC (Java Database Connectivity)
- **Version Control:** Git (using feature branching strategy)

## 📁 Project Structure
![Project Structure](ProjectStructure.jpg)

## ✅ Why I Built 
Built this to practice real-world backend patterns after noticing most event apps don't handle overbooking gracefully

```mermaid
classDiagram
    class Participant {
        <<abstract>>
        -id : int
        -name : String
        -email : String
    }

    class Individual

    class Organization {
        -representativeName : String
    }

    class Event {
        -id : int
        -title : String
        -description : String
        -startDateTime : LocalDateTime
        -endDateTime : LocalDateTime
        -location : String
        -capacity : int
        +isFull() boolean
    }

    class Invitation {
        -eventId : int
        -participantId : int
        -status : Status
    }

    class Status {
        <<enumeration>>
        PENDING
        ACCEPTED
        DECLINED
    }

    Participant <|-- Individual
    Participant <|-- Organization
    Event "1" --> "0..*" Invitation
    Participant "1" --> "0..*" Invitation
    Invitation --> Status
```

## 🚀 How to Run
**Prerequisites:** Java 17+, MySQL 8.0+, any IDE(IntelliJ recommended)
1. Clone the repo
2. Create a MySQL database and run `schema.sql` 
3. Set your DB credentials in `DatabaseConfiguration.java`
4. Run `Main.java` - a console menu will launch 

## 📸 Application Preview

![Screenshot of the application output](First_Output.jpg)
![Screenshot of the application output](Output1.jpg)
![Screenshot of the application output](Output2.jpg)
![Screenshot of the application output](Output3.jpg)

