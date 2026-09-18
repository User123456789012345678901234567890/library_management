import java.time.*;
import java.util.*;

/**
 * Represents an individual book held by the library.
 * Precondition: Concrete subclasses must provide checkout duration and information behavior.
 * Postcondition: Each instance stores its identifying, descriptive, and borrowing information.
 */
public abstract class Book{
    private String title; 
    private int pageCount;
    private Condition condition;
    private String authorLastName, authorFirstName;
    private long isbn;
    private int publicationDate;
    private LocalDate borrowedDate;

    public enum Condition{
        POOR,
        FAIR,
        GOOD,
        EXCELLENT,
        NEW
    }
    /**
     * Creates a new Book instance.
     * Precondition: The title, author names, and ISBN must be valid and non-empty.
     * Postcondition: A new Book instance is created with the specified attributes.
     * @param title the title of the book
     * @param pageCount the number of pages in the book 
     * @param condition the condition of the book (POOR, FAIR, GOOD, EXCELLENT, NEW)
     * @param authorLastName the last name of the author
     * @param authorFirstName the first name of the author
     * @param publicationDate the publication date of the book in YYYYMMDD format
     * @param isbn the ISBN (unique identifier) of the book
     */
    public Book(String title, int pageCount, Condition condition, String authorFirstName, String authorLastName, int publicationDate, long isbn){
        this.title = title;
        this.pageCount = pageCount;
        this.condition = condition;
        this.authorLastName = authorLastName;
        this.authorFirstName = authorFirstName;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        borrowedDate = null;
    }
    
    /**
     * Updates the condition of the book.
     * Precondition: The condition must be a valid Condition enum value.
     * Postcondition: The condition of the book is updated to the specified value.
     * @param condition the new condition of the book (POOR, FAIR, GOOD, EXCELLENT, NEW)
     */
    public void updateCondition(Condition condition){
        this.condition = condition;
    }

    /**
     * Returns the title of the book.
     * Precondition: None.
     * Postcondition: The book title is returned unchanged.
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    } 

    /**
     * Returns the number of pages in the book.
     * Precondition: None.
     * Postcondition: The page count is returned unchanged.
     * @return the number of pages in the book
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Returns the condition of the book.
     * Precondition: None.
     * Postcondition: The current book condition is returned unchanged.
     * @return the condition of the book
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Returns the author's name in last-name-first format.
     * Precondition: None.
     * Postcondition: The author's last name and first name are returned as one string.
     * @return the author's name in the format "last name, first name"
     */
    public String getAuthor() {
        return authorLastName + ", " + authorFirstName;
    }

    /**
     * Returns the publication date of the book.
     * Precondition: None.
     * Postcondition: The publication date is returned unchanged.
     * @return the publication date in YYYYMMDD format
     */
    public int getPublicationDate() {
        return publicationDate;
    }

    /**
     * Returns the date on which the book was borrowed.
     * Precondition: None.
     * Postcondition: The current borrowed date is returned, or null if the book has not been borrowed.
     * @return the borrowed date, or null if the book has not been borrowed
     */
    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    /**
     * Records the current date as the date on which the book was borrowed.
     * Precondition: None.
     * Postcondition: The borrowed date is set to the current date.
     */
    public void setBorrowedDate() {
        LocalDate now = LocalDate.now();
        borrowedDate = now;
    }

    /**
     * Subtracts a number of days from the borrowed date for debugging purposes.
     * Precondition: The book must have a borrowed date, and days must be non-negative.
     * Postcondition: The borrowed date is moved earlier by the specified number of days.
     * @param days the number of days to subtract
     */
    public void subtractBorrowedDays(int days) {
        // System.out.println("Initial date: " + borrowedDate);
        borrowedDate = borrowedDate.minusDays(days);
        // System.out.println("Subtracted " + days + " days, new date: " + borrowedDate);
    } 
    
    /** 
     * Returns the ISBN of the book.
     * Precondition: None.
     * Postcondition: The ISBN of the book is returned.
     * @return the ISBN of the book
     */
    public long getISBN(){
        return isbn;
    }

    /**
     * Defines the abstract method for getting the maximum number of days a book can be checked out.
     * Precondition: None.
    * Postcondition: The maximum checkout duration for this book is returned.
    * @return the maximum checkout duration
     */
    public abstract Duration getMaxCheckoutDays();
    /**
     * Returns a HashMap containing the book's information.
     * Precondition: None.
     * Postcondition: A HashMap containing the book's information is returned. This includes the title, page count, condition, and author name.
     * @return a HashMap containing the book's information
     */
    protected Map<String, String> _getInfo(){
        Map<String, String> info = new TreeMap<>();
        info.put("Title", title);
        info.put("Page Count", Integer.toString(pageCount));
        info.put("Condition", condition.toString());
        info.put("Author", authorFirstName + " " + authorLastName);
        info.put("Publication Date", Integer.toString(publicationDate));
        return info;
    }
    /**
     * Defines the abstract method for getting the book's information as a HashMap.
     * Precondition: None.
    * Postcondition: A map containing this book's information is returned.
    * @return a map containing the book's information
     */
    public abstract Map getInfo();
    /**
     * Returns a string representation of the book.
     * Precondition: None.
     * Postcondition: A string representation of the book is returned, including the title, page count, condition, and author name.
     * @return a string representation of the book
     */
    
    public String toString(){
        return "Book: " + title + ", " + pageCount + " pages\nCondition: " + condition + "\nAuthor: " + authorLastName + ", " + authorFirstName;
    }

}

///testsgakjdflsdkfjkl