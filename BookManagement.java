import java.util.*;
public class BookManagement {
    private long isbn;
    private int bookCount;
    private ArrayList<Book> available;
    private ArrayList<Book> borrowed;
    private Queue<User> waitlist;
    
    /**
     * Manages each title of book in the library.
     * Precondition: The ISBN must be valid and non-empty.
     * Postcondition: A new BookManagement instance is created with the specified ISBN and empty available and borrowed lists.
     * @param isbn The ISBN of the book to be managed
     */
    public BookManagement(long isbn) {
        this.isbn = isbn;
        available = new ArrayList<>();
        borrowed = new ArrayList<>();
        waitlist = new LinkedList<>();
        bookCount = 0;
    }

    /**
     * Returns the ISBN of the book being managed.
     * Precondition: None.
     * Postcondition: The ISBN of the book being managed is returned.
     * @return the ISBN of the book being managed
     */
    public long getISBN() {
        return isbn;
    }

    public int getBookCount(){
        return bookCount;
    }
    
    public Map getBookInfo(){
        if (!available.isEmpty()){
            return available.get(0).getInfo();
        }
        else if (!borrowed.isEmpty()){
            return borrowed.get(0).getInfo();
        }
        else{
            return null;
        }
    }

    public Queue<User> getWaitlist() {
        return waitlist;
    }

    public ArrayList<Book> getAvailableBooks() {
        return available;
    }

    /**
     * Adds books to the available catalog.
     * Precondition: The book must be valid and non-empty.
     * Postcondition: The book is added to the available catalog.
     * @param book The book to be added to the available catalog
     */
    public void addBook(Book book){
        // Add books to the available list
        available.add(book);
        bookCount ++;
    }
    
    /**
     * Checks out the first available book to the user.
     * Precondition: The user must be valid and non-empty, and the book must be available for checkout.
     * Postcondition: The book is checked out to the user and moved from the available list to the borrowed list.
     * @param user The user who is checking out the book
     * @return returns true if the user successfully borrowed the book and false if the user was placed in the waitlist
     */
    public boolean checkoutBook(User user, Book book) {
        // Store book under User
        if (!available.isEmpty() && !borrowed.contains(book)) {
            user.borrowBook(book);
            borrowed.add(book);
            available.remove(book);
            return true;
        }
        else if (!available.isEmpty()) {
            user.borrowBook(available.get(0));
            borrowed.add(available.get(0));
            available.remove(0); 
            return true;
        }
        else if (user.hasBook(book)) { // user renews a book
            book.setBorrowedDate();
            return true;
        }
        else {
            waitlist.add(user);
            return false;
        }
    }
    
    /**
     * Returns a book from the user and moves it back to the available catalog.
     * Precondition: The user must be valid and non-empty, and the book must be currently borrowed by the user.
     * Postcondition: The book is returned from the user and moved back to the available catalog.
     * @param user The user who is returning the book
     * @param book The book to be returned by the user
     * @return True if book was returned; false otherwise
     */
    public boolean returnBook(User user, Book book) {
        if (!borrowed.contains(book)){
            return false;
        }
        user.unborrowBook(book);
        borrowed.remove(borrowed.indexOf(book));
        available.add(book);
        if (!waitlist.isEmpty()) {
            checkoutBook(waitlist.poll(), book);
        } 
        return true;
    }
    
    public boolean removeBook(Book book) {
        if (available.contains(book)) {
            available.remove(available.indexOf(book));
            bookCount--;
            return true;
        }
        else if (borrowed.contains(book)) {
            borrowed.remove(borrowed.indexOf(book)); // should it just remove the first item in the arraylist?
            bookCount--;
            return true; 
        }
        return false;
    }
}