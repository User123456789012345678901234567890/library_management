import java.util.*;
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

    public User getUser(String username){
        for (User user : users.values()){
            if (user.getName().equals(username)){
                return user;
            }
        }
        return null;
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
    public void returnBooks(User user, Book... books) { 
        for (Book book : books) {
            if (user.hasBook(book)) {
                BookManagement bookManager = bookManagers.get(book.getISBN()); 
                bookManager.returnBook(user, book); // Changes the book in the specific BookManager from borrowed to available
            }
        }
    }

    public void borrowBooks(User user, Book... books) {
        for (Book book : books) {
            if (bookManagers.containsKey(book.getISBN())) {
                BookManagement bookManager = bookManagers.get(book.getISBN());
                bookManager.checkoutBook(user, book);
            }
        }
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

    public long[] getBookISBNs() { 
        return bookManagers.keySet().stream()
                .mapToLong(Long::longValue)
                .toArray(); 
    }

    public Map getBookInfo(Integer ISBN){
        return bookManagers.get(ISBN).getBookInfo();
    }

    public Map<Long, BookManagement> getBookManagers() {
        return bookManagers;
    }
}