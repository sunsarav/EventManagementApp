package se.lexicon.ui;

import se.lexicon.dao.EventDAO;
import se.lexicon.dao.ParticipantDAO;
import se.lexicon.model.Event;
import se.lexicon.model.Participant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CommunityCenterApp {
    private final ParticipantDAO participantDAO;
    private final EventDAO eventDAO;
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CommunityCenterApp(ParticipantDAO participantDAO, EventDAO eventDAO)
    {
        this.participantDAO = participantDAO;
        this.eventDAO = eventDAO;
        this.scanner = new Scanner(System.in);
    }
    public void start() {
        while (true) {
            System.out.println("\n-------WELCOME TO COMMUNITY CENTER APP-------");
            System.out.println("1. Register Participant");
            System.out.println("2. Create Event");
            System.out.println("3. View All Events");
            System.out.println("0. Exit");
            System.out.println("Choose your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> registerParticipant();
                case 2 -> createEvent();
                case 3 -> viewAllEvents();
                case 0 -> {
                    System.out.println("Exiting.... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again!");
            }
        }
    }
    // methods
    private void registerParticipant() {
        System.out.println("\n-------Register new Participant-------");

        System.out.println("Enter Name (Company Name): ");
        String name = scanner.nextLine();

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
            System.err.println("❌ Failed to register participant: " + e.getMessage());
        }
    }
    private void createEvent() {
        System.out.println("Enter Title: ");
        String title = scanner.nextLine();

        System.out.println("Enter Description: ");
        String desc = scanner.nextLine();

        System.out.println("Enter Start Date & Time (yyyy-MM-dd HH:mm:ss): ");
        LocalDateTime start = LocalDateTime.parse(scanner.nextLine(), formatter);

        System.out.println("Enter End Date & Time (yyyy-MM-dd HH:mm:ss): ");
        LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);

        System.out.println("Enter Location: ");
        String location = scanner.nextLine();

        System.out.println("Enter Capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());

        Event newEvent = new Event(title, desc, start, end, location, capacity);
        eventDAO.save(newEvent);
        System.out.println("✅ Event created with ID: " +  newEvent.getId());
    }
    private void viewAllEvents() {
        System.out.println("\n------- \uD83D\uDCC5 Upcoming Events -------");
        List<Event> events = eventDAO.findAll();

        if (events.isEmpty()) {
            System.out.println("No events scheduled yet");
            return;
        }

        events.forEach(event ->
                System.out.printf("ID: %d | Title: %s | Desc: %s | Start: %s | " +
                                "End: %s | Loc: %s | Cap: %d%n",
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartDateTime(),
                        event.getEndDateTime(),
                        event.getLocation(),
                        event.getCapacity())
                );
    }
}
