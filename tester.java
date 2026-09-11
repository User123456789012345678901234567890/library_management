public class tester{
    public static void main(String[] args){
        LibraryManagement library = new LibraryManagement("Milpitas Library");
        
        // Test adding users
        User simon = new User("Simon", "Zu", "simonzu");
        User jerry = new User("Xiaoran", "Xiong", "madpleb");
        User jayden = new User("Jayden", "Ho", "lotusjayden");
        library.addUsers(new User[]{simon, jerry, jayden});
        System.out.println("User1: " + simon.getName() + ", ID: " + simon.getUserID());
        System.out.println("User2: " + jerry.getName() + ", ID: " + jerry.getUserID());
        System.out.println("User3: " + jayden.getName() + ", ID: " + jayden.getUserID());
        
        // Test adding books
        
        Book book1 = new Novel("The Great Gatsby", 180, Book.Condition.NEW, "F. Scott", "Fitzgerald", 19250410, 743273567, Novel.Genre.CLASSIC, true);
        Book book2 = new Novel("To Kill a Mockingbird", 281, Book.Condition.EXCELLENT, "Harper", "Lee", 19600711, 61120081, Novel.Genre.CLASSIC, true);
        Book book3 = new Novel("1984", 328, Book.Condition.GOOD, "George", "Orwell", 19490608, 451524934, Novel.Genre.SCIENCE_FICTION, true);
        Book book4 = new Novel("Pride and Prejudice", 279, Book.Condition.FAIR, "Jane", "Austen", 18130128, 140003341, Novel.Genre.ROMANCE, true);
        Book book5 = new Novel("The Hobbit", 310, Book.Condition.POOR, "J.R.R.", "Tolkien", 19370921, 345339681, Novel.Genre.FANTASY, true);
        Book book6 = new Novel("The Great Gatsby", 180, Book.Condition.NEW, "F. Scott", "Fitzgerald", 19250410, 743273567, Novel.Genre.CLASSIC, true);
        Book book7 = new Novel("The Great Gatsby", 180, Book.Condition.NEW, "F. Scott", "Fitzgerald", 19250410, 743273567, Novel.Genre.CLASSIC, true);
        library.addBooks(new Book[]{book1, book2, book3, book4, book5});
        System.out.println(book1.getInfo());
        System.out.println(book1);
        
        // Test borrowing books
        library.borrowBooks(jerry, book1);
        library.borrowBooks(jerry, book2);
        System.out.println(jerry.getBorrowedBooks());
        System.out.println("Working");
    }  
}