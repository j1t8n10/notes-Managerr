import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotesManager {
    private static final String NOTES_FILE = "notes.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        NotesManager manager = new NotesManager();

        while (true) {
            System.out.println("\n=== Notes Manager ===");
            System.out.println("1. Add Note");
            System.out.println("2. View All Notes");
            System.out.println("3. Search Notes");
            System.out.println("4. Delete Note");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manager.addNote();
                    break;
                case 2:
                    manager.viewAllNotes();
                    break;
                case 3:
                    manager.searchNotes();
                    break;
                case 4:
                    manager.deleteNote();
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void addNote() {
        try (FileWriter writer = new FileWriter(NOTES_FILE, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            System.out.print("Enter note title: ");
            String title = scanner.nextLine();

            System.out.print("Enter note content: ");
            String content = scanner.nextLine();

            String note = title + "|" + content + "|" + System.currentTimeMillis();
            bufferedWriter.write(note);
            bufferedWriter.newLine();

            System.out.println("Note added successfully!");

        } catch (IOException e) {
            System.err.println("Error writing note: " + e.getMessage());
        }
    }

    public void viewAllNotes() {
        List<String> notes = readNotesFromFile();

        if (notes.isEmpty()) {
            System.out.println("No notes found.");
            return;
        }

        System.out.println("\n=== All Notes ===");
        for (int i = 0; i < notes.size(); i++) {
            String[] parts = notes.get(i).split("\\|");
            if (parts.length >= 2) {
                System.out.println((i + 1) + ". Title: " + parts[0]);
                System.out.println("   Content: " + parts[1]);
                System.out.println("   Date: " + new java.util.Date(Long.parseLong(parts[2])));
                System.out.println();
            }
        }
    }

    public void searchNotes() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<String> notes = readNotesFromFile();
        List<String> matchingNotes = new ArrayList<>();

        for (String note : notes) {
            if (note.toLowerCase().contains(searchTerm)) {
                matchingNotes.add(note);
            }
        }

        if (matchingNotes.isEmpty()) {
            System.out.println("No notes found matching '" + searchTerm + "'");
            return;
        }

        System.out.println("\n=== Search Results ===");
        for (int i = 0; i < matchingNotes.size(); i++) {
            String[] parts = matchingNotes.get(i).split("\\|");
            if (parts.length >= 2) {
                System.out.println((i + 1) + ". Title: " + parts[0]);
                System.out.println("   Content: " + parts[1]);
                System.out.println("   Date: " + new java.util.Date(Long.parseLong(parts[2])));
                System.out.println();
            }
        }
    }

    public void deleteNote() {
        List<String> notes = readNotesFromFile();

        if (notes.isEmpty()) {
            System.out.println("No notes to delete.");
            return;
        }

        viewAllNotes();
        System.out.print("Enter the number of the note to delete: ");
        int noteNumber = scanner.nextInt();

        if (noteNumber < 1 || noteNumber > notes.size()) {
            System.out.println("Invalid note number.");
            return;
        }

        notes.remove(noteNumber - 1);

        try (FileWriter writer = new FileWriter(NOTES_FILE);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            for (String note : notes) {
                bufferedWriter.write(note);
                bufferedWriter.newLine();
            }

            System.out.println("Note deleted successfully!");

        } catch (IOException e) {
            System.err.println("Error deleting note: " + e.getMessage());
        }
    }

    private List<String> readNotesFromFile() {
        List<String> notes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(NOTES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    notes.add(line);
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            System.err.println("Error reading notes: " + e.getMessage());
        }

        return notes;
    }
}
