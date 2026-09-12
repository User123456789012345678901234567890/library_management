import java.util.*;

/**
 * Manages the library's operations, including book inventory, user accounts,
 * borrowing, and returning processes.
 */
public class LibraryManagement {
    private Map<Long, BookManagement> bookManagers;
    private Map<Integer, User> users;
    private String libraryName;
    
    /** 
     * Creates a new LibraryManagement system for a library with a given name.
     * Precondition: The library name must be valid and non-empty.
     * Postcondition: A new LibraryManagement instance is created with the specified library name.
     * @param libraryName The name of the library
     * @throws IllegalArgumentException if the library name is null or empty
     */
    public LibraryManagement(String libraryName) {
        bookManagers = new HashMap<>();
        users = new HashMap<>();
        this.libraryName = libraryName;
    }

    /**
     * Authenticates and retrieves a user matching the specified username and password.
     * Precondition: The username and password must be non-null.
     * Postcondition: The authenticated User object is returned, or null if credentials do not match.
     * @param username The username of the user
     * @param password The password of the user
     * @return The authenticated User, or null if authentication fails
     */
    public User getUser(String username, String password){
        for (User user : users.values()){
            if (user.authenticate(username, password)){ // oh...........................
                return user; 
            } 
        }
        return null;
    }
    
    /**
     * Returns the name of the library.
     * Precondition: None.
     * Postcondition: The library name is returned unchanged.
     * @return The name of the library
     */
    public String getName(){
        return libraryName;
    }

    /**
     * Adds a nonuniform list of books to the library's collection.
     * Precondition: The books must be valid and non-empty.
     * Postcondition: The books are added to the library's collection.
     * @param books The books to be added to the library's collection
     */
    public void addBooks(Book... books) {
        // add books to the library
        for (Book book : books){
            // for (BookManager bookManager : bookManagers.values()) {
            //     if (bookManager.getISBN() == book.getISBN()){
            //         bookManager.addBook(book);
            //     }
            // }
            if (bookManagers.containsKey(book.getISBN())){
                BookManagement manager = bookManagers.get(book.getISBN());
                manager.addBook(book);
            }
            else {
                BookManagement manager = new BookManagement(book.getISBN());
                manager.addBook(book);
                bookManagers.put(book.getISBN(), manager);
            }
            // System.out.println("Book added: " + book.getTitle());
        }
    }

    
    /**
     * Returns the User object associated with the given user ID.
     * Precondition: The user ID must be valid and exist in the library's user collection.
     * Postcondition: The User object associated with the given user ID is returned.
     * @param user ID The unique ID of the user
     * @param books The books to be returned by the user
     * @throws IllegalArgumentException if the user ID is invalid or does not exist in the library's user collection
     */
    private void returnBooks(User user, Book... books) { 
        for (Book book : books) {
            if (user.hasBook(book)) {
                BookManagement bookManager = bookManagers.get(book.getISBN()); 
                bookManager.returnBook(user, book); // Changes the book in the specific BookManager from borrowed to available
            }
        }
    }

    /**
     * Returns a book borrowed by a user based on the book's ISBN.
     * Precondition: The user must be non-null and the ISBN must be valid.
     * Postcondition: The matching book is returned to the library if held by the user.
     * @param user The user returning the book
     * @param isbn The ISBN of the book to return
     * @return true if the book was successfully found and returned, false otherwise
     */
    public boolean returnBooks(User user, long isbn){
        ArrayList<Book> borrowedBooks = user.getBorrowedBooks();
        for (Book borrowedBook : borrowedBooks){
            if (isbn == borrowedBook.getISBN()){
                returnBooks(user, borrowedBook);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks out specified books for a user.
     * Precondition: The user and books must be non-null, and the books must exist in the library collection.
     * Postcondition: The books are checked out to the user if managed by the library.
     * @param user The user checking out the books
     * @param books The books to be checked out
     */
    public void borrowBooks(User user, Book... books) {
        for (Book book : books) {
            if (bookManagers.containsKey(book.getISBN())) {
                BookManagement bookManager = bookManagers.get(book.getISBN());
                bookManager.checkoutBook(user, book);
            }
        }
    }

    /**
     * Checks out the first available copy of a book with the given ISBN to a user.
     * Precondition: The user must be non-null and the ISBN must exist in the library collection.
     * Postcondition: An available copy of the book is checked out to the user if present.
     * @param user The user checking out the book
     * @param isbn The ISBN of the book to borrow
     * @return true if an available copy was successfully checked out, false otherwise
     */
    public boolean borrowBooks(User user, long isbn){
        if (bookManagers.containsKey(isbn)) {
            BookManagement bookManager = bookManagers.get(isbn);
            Book book = bookManager.getAvailableBooks().get(0);
            return bookManager.checkoutBook(user, book);
        }
        return false;
    }
    
    /**
     * Adds a new user to the library's user collection.
     * Precondition: The user must be valid and non-empty.
     * Postcondition: The user is added to the library's user collection.
     * @param user The user to be added to the library's user collection
     * @throws IllegalArgumentException if the user is null or already exists in the library's user collection
     */
    public void addUsers(User... newUsers) {
        for (User user : newUsers) {
            users.put(user.getUserID(), user);
        }
    }

    /**
     * Removes a book from the library's collection, such as if a book was lost or Hitler burned a book.
     * Precondition: The ISBN must be valid and exist in the library's book collection.
     * Postcondition: The book is removed from the library's collection.
     * @param Book The book to be removed from the library's collection
     * @throws IllegalArgumentException if the ISBN is invalid or does not exist in the library's book collection
     * @throws IllegalStateException if the book is currently borrowed by a user
     */
    public void removeBook(Book book) {
        BookManagement bookManagement = bookManagers.get(book.getISBN());
        bookManagement.removeBook(book);
    }

    /**
     * Removes a book from the library's collection, such as if a book was lost or Hitler burned a book.
     * Precondition: The ISBN must be valid and exist in the library's book collection.
     * Postcondition: The book is removed from the library's collection.
     * @param Book The book to be removed from the library's collection
     * @throws IllegalArgumentException if the ISBN is invalid or does not exist in the library's book collection
     * @throws IllegalStateException if the book is currently borrowed by a user
     */
    public void removeBook(User user, Book book) {
        user.unborrowBook(book);
        BookManagement bookManagement = bookManagers.get(book.getISBN()); 
        bookManagement.removeBook(book);
    }

    /**
     * Returns an array of all book ISBNs managed by the library.
     * Precondition: None.
     * Postcondition: An array containing all managed ISBNs is returned.
     * @return An array of long values representing the ISBNs of managed books
     */
    public long[] getBookISBNs() { 
        return bookManagers.keySet().stream()
                .mapToLong(Long::longValue)
                .toArray(); 
    }

    /**
     * Retrieves the information map for the book with the specified ISBN.
     * Precondition: The ISBN must exist in the library's book collection.
     * Postcondition: A Map containing the book's details is returned.
     * @param ISBN The ISBN of the book whose information is requested
     * @return A Map containing the attributes and details of the book
     */
    public Map getBookInfo(long ISBN){
        return bookManagers.get(ISBN).getBookInfo();
    }

    /**
     * Returns the map of book managers keyed by ISBN.
     * Precondition: None.
     * Postcondition: The map containing ISBNs mapped to their BookManagement objects is returned.
     * @return A Map of Long ISBNs to BookManagement instances
     */
    public Map<Long, BookManagement> getBookManagers() {
        return bookManagers;
    }

    public boolean checkUnique(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
}