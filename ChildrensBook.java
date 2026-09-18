import java.time.*;
import java.util.*;
public class ChildrensBook extends Book {
    private int lexile;
    /**
     * Creates a new ChildrensBook with the given title, page count, condition, author name, ISBN, and lexile score.
     * Precondition: The title, author names, page count,ISBN, and lexile score must be valid and non-empty.
     * Postcondition: A new ChildrensBook instance is created with the specified attributes.
     * @param title The title of the book
     * @param pageCount The number of pages in the book
     * @param condition The condition of the book (POOR, FAIR, GOOD, EXCELLENT, NEW)
     * @param authorFirstName The first name of the book's author
     * @param authorLastName The last name of the book's author
     * @param isbn The ISBN of the book
     * @param lexile The lexile score of the book
     */
    public ChildrensBook(String title, int pageCount, Condition condition, String authorFirstName, String authorLastName, int publicationDate, long isbn,
    int lexile) {
        super(title, pageCount, condition, authorFirstName, authorLastName, publicationDate, isbn);
        this.lexile = lexile;
    }
    /**
     * Returns the maximum number of days a children's book can be checked out.
     * Precondition: None.
     * Postcondition: The maximum number of days a children's book can be checked out - 14 - is returned
     * @return 14 days in integer, the maximum number of days a children's book can be checked out
     */
    @Override
    public Duration getMaxCheckoutDays() {
        Duration duration = Duration.ofDays(14);
        return duration;
    }
    
    /**
     * Returns the information of the children's book.
     * Precondition: None.
     * Postcondition: The information of the children's book is returned.
     * @return A HashMap containing the information of the children's book, including its lexile score, while also including the inherited information from the Book class (title, page count, condition, author name, and ISBN)
     */
    public Map getInfo() {
        Map<String, String> info = super._getInfo();
        info.put("Lexile", Integer.toString(lexile));
        info.put("Type", "Childrens Book");
        return info;
    }

    public String toString() {
        return super.toString();
    }
}