import java.time.*;
import java.util.*;
// Individual Books 
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

    public String getTitle() {
        return title;
    } 

    public int getPageCount() {
        return pageCount;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getAuthor() {
        return authorLastName + ", " + authorFirstName;
    }

    public int getPublicationDate() {
        return publicationDate;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
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
     * Postcondition: Not defined yet.
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
        return info;
    }
    /**
     * Defines the abstract method for getting the book's information as a HashMap.
     * Precondition: None.
     * Postcondition: Not defined yet.
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