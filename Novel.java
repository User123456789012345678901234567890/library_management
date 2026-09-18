import java.time.*;
import java.util.*;
public class Novel extends Book {
    private Genre genre;
    private boolean fictional;
    
    public enum Genre{
        SCIENCE_FICTION,
        ROMANCE,
        CLASSIC,
        HISTORICAL_FICTION,
        BIOGRAPHY,
        MYSTERY,    
        HORROR,
        THRILLER,
        FANTASY,
        OTHER
    }
    /**
     * Creates a new Novel with the given title, page count, condition, author name, ISBN, genre, and fictional status.
     * Precondition: The title, author names, ISBN, genre, and fictional status must be valid and non-empty.
     * Postcondition: A new Novel instance is created with the specified attributes.
     * @param title The title of the novel
     * @param pageCount The number of pages in the novel
     * @param condition The condition of the novel (POOR, FAIR, GOOD, EXCELLENT, NEW)
     * @param authorFirstName The first name of the novel's author
     * @param authorLastName The last name of the novel's author
     * @param publicationDate the publication date of the book in YYYYMMDD format
     * @param isbn The ISBN of the novel
     * @param genre The genre of the novel
     * @param fictional Whether the novel is fictional or non-fictional
     */
    public Novel(String title, int pageCount, Condition condition, String authorFirstName, String authorLastName, int publicationDate, int isbn,
    Genre genre, boolean fictional) {
        super(title, pageCount, condition, authorFirstName, authorLastName, publicationDate, isbn);
        this.genre = genre;
        this.fictional = fictional;
    }
    /**
     * Returns the maximum number of days a novel can be checked out.
     * Precondition: None.
     * Postcondition: The maximum number of days a novel can be checked out - 21 - is returned
     * @return 21 days in integer, the maximum number of days a novel can be checked out
     */
    @Override 
    public Duration getMaxCheckoutDays() {
        Duration duration = Duration.ofDays(21);
        return duration;
    }

    /**
     * Returns a HashMap containing the novel's information, including its genre and fictional status.
     * Precondition: None.
     * Postcondition: A HashMap containing the novel's information is returned.
     * @return a HashMap containing the novel's information, including its genre and fictional status while also including the inherited information from the Book class (title, page count, condition, author name, and ISBN)
     */
    public Map getInfo() {
        Map<String, String> info = super._getInfo();
        info.put("Genre", genre.toString());
        info.put("Fictional", Boolean.toString(fictional));
        info.put("Type", "Novel");
        return info;
    }

    public String toString() {
        return super.toString() + "\nGenre: " + genre + "\nFictional? " + fictional; 
    }
}
