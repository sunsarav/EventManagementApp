package se.lexicon.ui;

import se.lexicon.dao.EventDAO;
import se.lexicon.dao.InvitationDAO;
import se.lexicon.dao.InvitationDAOImpl;
import se.lexicon.dao.ParticipantDAO;
import se.lexicon.model.Event;
import se.lexicon.model.Invitation;
import se.lexicon.model.Participant;
import se.lexicon.model.Status;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CommunityCenterApp {
    private final InvitationDAO invitationDAO;
    private final ParticipantDAO participantDAO;
    private final EventDAO eventDAO;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Scanner scanner = new Scanner(System.in);
    private final List<Invitation> invitations = new ArrayList<>();
    private final int MAX_PARTICIPANTS = 10;

    public CommunityCenterApp(Connection connection, ParticipantDAO participantDAO, EventDAO eventDAO,
                              InvitationDAO invitationDAO)
    {
        this.participantDAO = participantDAO;
        this.eventDAO = eventDAO;
        this.invitationDAO = invitationDAO;
    }
    public void start() {
        while (true) {
            System.out.println("\n-------WELCOME TO COMMUNITY CENTER APP-------");
            System.out.println("1. Register Participant");
            System.out.println("2. Create Event");
            System.out.println("3. View All Events");
            System.out.println("4. Invite Participant to Event");
            System.out.println("5. Update Invitation Status");
            System.out.println("6. View Sorted Events");
            System.out.println("7. View Attendance List");
            System.out.println("0. Exit");
            System.out.println("Choose your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> registerParticipant();
                    case 2 -> createEvent();
                    case 3 -> viewAllEvents();
                    case 4 -> inviteParticipant();
                    case 5 -> updateInvitation();
                    case 6 -> viewSortedEvents();
                    case 7 -> {
                        System.out.println("Enter Event Title: ");
                        String title = scanner.nextLine();
                        viewAttendanceList(title);

                    }
                    case 0 -> {
                        System.out.println("Exiting.... Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number (0-7) instead of alphabets");
            }
        }
    }
    // Register Participants (Individuals / Organizations)

    private void registerParticipant() {
        System.out.println("\n-------Register new Participant-------");

        System.out.println("Enter Name (Company Name): ");
        String name = scanner.nextLine().trim();

        System.out.println("Enter Email Address: ");
        String email = scanner.nextLine();

        System.out.println("Participant Type (Individual / Organization): ");
        String type = scanner.nextLine();

        System.out.println("Enter Representative Name (Press Enter if none): ");
        String repName = scanner.nextLine();

        Participant participant = new Participant(name, email, type, repName);

        try {
            Participant savedParticipant = participantDAO.save(participant);
            System.out.println("✅ Participant registered successfully with ID: "
                    + savedParticipant.getId());
        } catch (Exception e) {
            // Check if error message mentions a "Duplicate Entry"
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("❌ Registration failed: A Participant with this email already exists!");
            } else {
                System.err.println("❌ Failed to register participant: " + e.getMessage());
            }
        }
    }
    // Create and manage Events

    private void createEvent() {
        System.out.println("Enter Title: ");
        String title = scanner.nextLine();

        System.out.println("Enter Description: ");
        String desc = scanner.nextLine();

        System.out.println("Enter Start Date & Time (yyyy-MM-dd HH:mm): ");
        LocalDateTime start = LocalDateTime.parse(scanner.nextLine(), formatter);

        System.out.println("Enter End Date & Time (yyyy-MM-dd HH:mm): ");
        LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);

        System.out.println("Enter Location: ");
        String location = scanner.nextLine();

        System.out.println("Enter Capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());

        Event newEvent = new Event(title, desc, start, end, location, capacity);
        eventDAO.save(newEvent);
        System.out.println("✅ Event created with ID: " +  newEvent.getId());
        System.out.println("Invitations: " + newEvent.getInvitations().size());
    }
    // View all upcoming events

    private void viewAllEvents() {
        System.out.println("\n------- \uD83D\uDCC5 Upcoming Events -------");
        List<Event> events = eventDAO.findAll();

        if (events.isEmpty()) {
            System.out.println("No events scheduled yet");
            return;
        }

        for (Event event : events) {
            // Fetch actual invitations from the database for the specific eventID
            List<Invitation> dbInvitations = invitationDAO.findByEventId(event.getId());

            // Update the event object's internal list with the database data
            event.getInvitations().clear();
            event.getInvitations().addAll(dbInvitations);

            System.out.printf("ID: %d | Title: %s | Desc: %s | Start: %s | " +
                            "End: %s | Loc: %s | Cap: %d%n",
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getStartDateTime(),
                    event.getEndDateTime(),
                    event.getLocation(),
                    event.getCapacity());

            // Checks the list of Event class
            if (event.getInvitations() == null || event.getInvitations().isEmpty()) {
                System.out.println("No invitations found.");
            } else {
                System.out.println("Participants: " + event.getInvitations().size());
            }
            System.out.println("---------------------------------------------------");
        }

    }
    // Invite Participants to an event

    public void inviteParticipant() {
        // 1. Get the Participant
        System.out.println("Enter Participant Name: ");
        String name = scanner.nextLine().trim();

        System.out.println("DEBUG: Searching for Participant with Name: [" + name + "]");

        // Logic to get the Participant object from DAO
        Participant participant = participantDAO.findByName(name);
        if (participant == null) {
            System.out.println("❌ Error: Participant not found.");
            return;
        }
        // 2. Get the Event
        System.out.println("Enter Event Name: ");
        String eventName = scanner.nextLine();

        Event event = eventDAO.findByName(eventName);
        if (event == null) {
            System.out.println("❌ Error: Event not found.");
            return;
        }
        // 3. Prevent Overbooking  (Java Streams) - Counts how many ACCEPTED invitations exist for this specific event
        long acceptedCount = invitations.stream()
                .filter(invitation -> invitation.getEvent().getId() == event.getId())
                .filter(invitation -> invitation.getStatus() == Status.ACCEPTED)
                .count();

        // 4. Set Status based on Capacity
        Status initialStatus = (acceptedCount >= event.getCapacity()) ? Status.PENDING : Status.ACCEPTED;

        //5. Create and add to the main app List
        Invitation newInvitation = new Invitation(event, participant, initialStatus);
        invitations.add(newInvitation);

        invitationDAO.save(newInvitation);

        // Ensures the Event object "knows" it has a new invitation
        event.getInvitations().add(newInvitation);

        System.out.println("✅ Success: " + name + " added to " + eventName
                + " with Status " + initialStatus);
    }
    // Update Invitation Status (accept/decline)

    public void updateInvitation() {
        if (invitations.isEmpty()) {
            System.out.println("No invitations to update");
            return;
        }
        // Display current list for user to choose from an index
        System.out.println("\n------- Current Events -------");
        for (int i =0; i < invitations.size(); i++) {
            Invitation invitation = invitations.get(i);

            System.out.println(i + " . [ " + invitation.getStatus() + " ]" +
                    invitation.getParticipant().getName() + " @ " +
                    invitation.getEvent().getTitle());
        }
        System.out.println("\nSelect Index to Update: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());

            if (index >= 0 && index < invitations.size()) {
                System.out.println("Enter new status (ACCEPTED, PENDING, DECLINED): ");
                String statusInput = scanner.nextLine().toUpperCase();

                Status newStatus = Status.valueOf(statusInput);
                invitations.get(index).setStatus(newStatus);
                System.out.println("✅ Status updated to " + newStatus);
            } else {
                System.out.println("❌ Invalid index.");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input.");
        }
    }
    // Attendance List (Accepted Participants)

    public void viewAttendanceList(String eventName) {
        // 1. Find the event first
        Event event = eventDAO.findByName(eventName);

        // 2. Check if event exists
        if (event == null) {
            System.out.println("❌ Event " + eventName + " not found.");
            return;
        }
        System.out.println("------- Attendance List for: " + event.getTitle() + " -------");

        // 3. Calling the method on the INSTANCE (invitationDAO)
        List<Invitation> list = invitationDAO.findByEventId(event.getId());

        // 4. Stream to filter only ACCEPTED
        list.stream()
                .filter(invitation -> invitation.getStatus() == Status.ACCEPTED)
                .forEach(invitation -> System.out.println(invitation.getParticipant().getName()));
        }
    // View Sorted Events

    public void viewSortedEvents() {
        System.out.println("\n------- Upcoming Events (Sorted By Date) -------");

        // 1. Fetch from Database
        List<Event> events = eventDAO.findAll();

        if (events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        // 2. Sort & Display using Streams
        events.stream()
                .sorted(Comparator.comparing(Event::getStartDateTime))
                .forEach(event -> System.out.println(event.getStartDateTime() + " | "
                        + event.getTitle()));
    }
}
