import java.time.*;
import java.util.ArrayList;
public class User {
    private final String firstName, lastName;
    private final String username; // Must be unique
    private final int userID;
    private static int latestID;
    private ArrayList<Book> borrowedBooks;
    private boolean penalty;
    
    /**
     * Creates a new User with a given first name, last name, and username.
     * Precondition: The first name, last name, and username must be valid and unique.
     * Postcondition: A new User instance is created with the specified attributes.
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
    }
    /**
     * Borrows the book under the user.
     * Precondition: The book must be valid and non-empty.
     * Postcondition: The book is added to the user's borrowed books list.
     * @param book The book to be borrowed by the user
     */
    public void borrowBook(Book book) {
        book.setBorrowedDays();
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

    public String getName() {
        return lastName + ", " + firstName;
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
    public ArrayList getBorrowedBooks() {
        return borrowedBooks;
    }

    public ArrayList getOverdue() {
        LocalDate date = LocalDate.now();
        ArrayList<Book> overdueBooks = new ArrayList<>();
        for (Book book : borrowedBooks) {
            Duration diff = Duration.between(date.atStartOfDay(), book.getBorrowedDate().atStartOfDay()).abs();
            if (diff.minus(book.getMaxCheckoutDays()).isNegative()) 
                overdueBooks.add(book);
        }
        return overdueBooks;
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
}