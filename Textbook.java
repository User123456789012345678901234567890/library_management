import java.time.*;
import java.util.*;
class Textbook extends Book {
    private Subject subject;
    private String course;
    public enum Subject{
        MATH, 
        SCIENCE, 
        ENGLISH, 
        HISTORY,
        ART,
        MUSIC,
        COMPUTER_SCIENCE,
        FOREIGN_LANGUAGE,
        ECONOMICS,
        PSYCHOLOGY,
        PHILOSOPHY,
        SOCIOLOGY,
        POLITICAL_SCIENCE,
        RELIGION,
        GEOGRAPHY,
        LITERATURE,
        ENGINEERING,
        MEDICINE,
        LAW,
        BUSINESS,
        OTHER
    }
    /**
     * Creates a new Textbook with the given title, page count, condition, author name, ISBN, subject, and course.
     * Precondition: The title, author names, ISBN, subject, and course must be valid and non-empty.
     * Postcondition: A new Textbook instance is created with the specified attributes.
     * @param title The title of the textbook
     * @param pageCount The number of pages in the textbook
     * @param condition The condition of the textbook (POOR, FAIR, GOOD, EXCELLENT, NEW)
     * @param authorFirstName The first name of the textbook's author
     * @param authorLastName The last name of the textbook's author
     * @param publicationDate the publication date of the book in YYYYMMDD format
     * @param isbn The ISBN of the textbook
     * @param subject The subject of the textbook
     * @param course The course for which the textbook is used
     */
    public Textbook(String title, int pageCount, Condition condition, String authorFirstName, String authorLastName, int publicationDate, int isbn,
    Subject subject, String course) {
        super(title, pageCount, condition, authorFirstName, authorLastName, publicationDate, isbn);
        this.subject = subject;
        this.course = course;
    }
    /**
     * Returns the maximum number of days a textbook can be checked out.
     * Precondition: None.
     * Postcondition: The maximum number of days a textbook can be checked out - 365 - is returned
     * @return 365 days in integer, the maximum number of days a textbook can be checked out
     */
    @Override 
    public Duration getMaxCheckoutDays() {
        Duration duration = Duration.ofDays(365);
        return duration;
    }
    /**
     * Returns a HashMap containing the textbook's information, including its subject and course.
     * Precondition: None.
     * Postcondition: A HashMap containing the textbook's information is returned.
     * @return a HashMap containing the textbook's information, including its subject and course while also including the inherited information from the Book class (title, page count, condition, author name, and ISBN)
     */
    public Map getInfo() {
        Map<String, String> info = super._getInfo();
        info.put("Subject", subject.toString());
        info.put("Course", course);
        return info;
    }
    
}