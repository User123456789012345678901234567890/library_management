import java.time.*;
import java.util.ArrayList;

/**
 * Represents a library user who can borrow, return, and track books.
 */
public class User {
    private final String firstName, lastName;
    private final String username; // Must be unique
    private final int userID;
    private static int latestID;
    private ArrayList<Book> borrowedBooks;
    private boolean penalty;
    private String password; 
    
    /**
     * Creates a new User with a given first name, last name, and username.
     * Precondition: The first name, last name, and username must be valid and unique.
     * Postcondition: A new User instance is created with the specified attributes.
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param username The username of the user
     */
    public User(String firstName, String lastName, String username, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        userID = ++latestID;
        this.borrowedBooks = new ArrayList<>();
        penalty = false;
        this.password = password;
    }

    /**
     * Creates a new User with a default password.
     * Precondition: The first name, last name, and username must be valid and unique.
     * Postcondition: A new User instance is created with a default password ("Pass123!").
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param username The username of the user
     */
    public User(String firstName, String lastName, String username){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        userID = ++latestID;
        this.borrowedBooks = new ArrayList<>();
        penalty = false;
        password = "Pass123!";
    }

    /**
     * Borrows the book under the user.
     * Precondition: The book must be valid and non-empty.
     * Postcondition: The book is added to the user's borrowed books list.
     * @param book The book to be borrowed by the user
     */
    public void borrowBook(Book book) {
        book.setBorrowedDate();
        borrowedBooks.add(book);
    }

    /**
     * Returns the book under the user.
     * Precondition: The book must be valid and non-empty.
     * Postcondition: The book is removed from the user's borrowed books list.
     * @param book The book to be returned by the user
     */
    public void unborrowBook(Book book){
        borrowedBooks.remove(book);
    }

    /**
     * Returns the user's full name formatted with last name first.
     * Precondition: None.
     * Postcondition: The user's name is returned in "LastName, FirstName" format.
     * @return The user's full name in the format "LastName, FirstName"
     */
    public String getName() {
        return lastName + ", " + firstName;
    }

    /**
     * Returns the user's username.
     * Precondition: None.
     * Postcondition: The user's username is returned.
     * @return The unique username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user's unique ID.
     * Precondition: None.
     * Postcondition: The user's unique ID is returned.
     * @return the user's unique ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Returns an ArrayList of the user's borrowed books.
     * Precondition: None.
     * Postcondition: An ArrayList of the user's borrowed books is returned.
     * @return an ArrayList of the user's borrowed books
     */
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    /**
     * Generates a readable summary of all currently borrowed books and their due dates.
     * Precondition: None.
     * Postcondition: An ArrayList of formatted strings detailing each borrowed book's title, borrow date, and due date is returned.
     * @return An ArrayList of strings summarizing each borrowed book and its due date
     */
    public ArrayList<String> getBorrowedSummary() {
        ArrayList<String> summary = new ArrayList<>();
        for (Book book : borrowedBooks) {
            summary.add(book.getTitle() + " - Borrowed " + book.getBorrowedDate() + " | Due " 
            + book.getBorrowedDate().plusDays(book.getMaxCheckoutDays().toDays()));
        }
        return summary;
    }

    /**
     * Identifies all borrowed books that are past their due dates.
     * Precondition: None.
     * Postcondition: Returns an ArrayList containing all books that have exceeded their maximum checkout duration.
     * @return An ArrayList of Book objects that are overdue
     */
    public ArrayList<Book> getOverdue() {
        LocalDate date = LocalDate.now();
        ArrayList<Book> overdueBooks = new ArrayList<>();
        for (Book book : borrowedBooks) {
            Duration diff = Duration.between(date.atStartOfDay(), book.getBorrowedDate().atStartOfDay()).abs();
            // System.out.println("Duration: " + diff.getSeconds());
            if (book.getMaxCheckoutDays().minus(diff).isNegative()) 
                overdueBooks.add(book);
        }
        return overdueBooks;
    }
    
    /**
     * Sets the penalty status of the user.
     * Precondition: None.
     * Postcondition: The user's penalty flag is updated to the specified boolean value.
     * @param bool True if the user has an active penalty, false otherwise
     */
    public void setPenalty(boolean bool) {
        penalty = bool;
    }

    /**
     * Checks if the user has borrowed a specific book.
     * Precondition: The book must be valid and non-empty.
     * Postcondition: Returns true if the user has borrowed the book, false otherwise.
     * @param book The book to check if the user has borrowed
     * @return true if the user has borrowed the book, false otherwise
     */
    public boolean hasBook(Book book) {
        for (Book b : borrowedBooks) {
            if (book.getISBN() == b.getISBN()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifies if the provided credentials match the user's username and password.
     * Precondition: None.
     * Postcondition: Returns true if both the username and password match, false otherwise.
     * @param username The username to verify
     * @param password The password to verify
     * @return True if authentication succeeds, false otherwise
     */
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}