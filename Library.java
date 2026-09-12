import java.util.*;
public class Library{
    private static final Scanner SCANNER = new Scanner(System.in); 
    private static final int PAGE_SIZE = 10;
    private static final String ADMIN_PASSWORD = "libadmin";
    public static void main(String[] args){
        Library.OpenSession("Milpitas");
        
    }
    public static String getUserInput(String[] allowedInputs, int minLength){
        while (true){
            System.out.print("| >> ");
            String str;
            try{
                str = SCANNER.nextLine();
                // scanner.close();
                if (allowedInputs.length == 0){
                    return str;
                }
                if (str.length() < minLength){
                    System.out.println("Your input needs to be at least " + minLength + " characters. Please try again.");
                    continue;
                } 
                for (String allowedInput : allowedInputs){
                    if (str.equals(allowedInput)){
                        return str;
                    }
                }
                System.out.println("Your input wasn't a valid command. Please try again.");
            }
            catch(Exception e){
                System.out.println("Your input wasn't a valid command. Please try again.");
            }
        }
    }
    public static int getUserInput(int[] allowedInputs){
        if (allowedInputs.length == 0){
            return Integer.parseInt(getUserInput(new String[]{}, 1));
        }
        String[] allowedInputsStr = new String[allowedInputs.length];
        for (int i = 0; i < allowedInputs.length; i++){
            allowedInputsStr[i] = Integer.toString(allowedInputs[i]);
        }
        return Integer.parseInt(getUserInput(allowedInputsStr, 1));

    }
    private static void openLoginSession(LibraryManagement library){
        User user = null;
        while (true){
            System.out.println("To log in, please enter your username (min 5 characters). Type 0 to escape. ");
            String username = getUserInput(new String[]{}, 5);
            if (username.equals("0")){
                return;
            }
            while (true){
                System.out.println("Enter your password. Type 0 to escape, or type 1 to re-enter username. "); //
                String password = getUserInput(new String[]{}, 1);
                if (password.equals("0")){
                    return;
                }
                if (password.equals("1")){
                    break;
                }
                
                user = library.getUser(username, password);
                if (user != null){
                    break;
                }
                System.out.println("No account found with username \"" + username + "\", or password is incorrect. Try again, or type 1 to escape.");
            }
            if (user != null){
                break;
            }
        }
        System.out.println("Signed in as " + user.getName() + " (ID: " + user.getUserID() + ")."); //we have an id somewhere so add it in here or we can leave it out if u want
        ArrayList<Book> overdueBooks = user.getOverdue();
        user.setPenalty(!overdueBooks.isEmpty()); 
        if (!overdueBooks.isEmpty()){
            System.out.println("WARNING: You have " + overdueBooks.size() + " overdue books:");
            for (Book overdueBook : overdueBooks){
                System.out.println("  " + overdueBook.getTitle() + " - Borrowed on " + overdueBook.getBorrowedDate());
            }
            System.out.println("If you do not return overdue books, you will be barred from borrowing books until they are returned.");

        }
        while (true){
            System.out.println("| To borrow a book, type 1");
            System.out.println("| To see your borrowed books or to return a book, type 2"); //shouldn't you change this into two sepearte thing
            System.out.println("| To mark a book as lost, type 3");
            System.out.println("| To log out, type 4");
            int selection = getUserInput(new int[]{1,2,3,4});
            switch (selection){
                case 1:
                    openBorrowBookSession(library, user);
                    break;
                case 2:
                    openReturnBookSession(library, user);
                    break;
                case 3:
                    openLostBookSession(library, user);  
                    
                    break;
                case 4:
                    System.out.println("Logged out.");
                    return;
                
            }
        }

    }
    private static void openBorrowBookSession(LibraryManagement library, User user){
        if (!user.getOverdue().isEmpty()){
            System.out.println("You cannot borrow a book until you return all overdue books. Exiting out of borrowing books.");
            return;
        }
        while (true){
            System.out.println("Preparing to borrow book...");
            System.out.println("| To search for a book by name, type 1");
            System.out.println("| To browse through our list of books, type 2");
            System.out.println("| To exit out of borrowing mode, type 3");
            int selection = getUserInput(new int[]{1,2, 3});
            switch (selection){
                case 1: // search 
                    openSearchSession(library, user);
                    break;
                case 2: // browse
                    openBrowseSession(library, user);
                    break;
                case 3:
                    System.out.println("Exiting out of borrowing books...");
                    return;
                default:
                    System.out.println("Your input wasn't a valid choice. Please try again.");
                    continue;
            }
        }
    }

    private static void openSearchSession(LibraryManagement library, User user){
        System.out.println("Opening Book Searcher...");
        String bookName = getUserInput(new String[]{}, 0).trim();
        long[] bookISBNS = library.getBookISBNs();
        long foundBookISBN = -1;
        Map<?, ?> info = null;

        for (long ISBN : bookISBNS) {
            Map<?, ?> currentInfo = library.getBookInfo(ISBN);
            if (currentInfo != null && currentInfo.get("Title") != null) {
                String title = currentInfo.get("Title").toString();
                if (title.equalsIgnoreCase(bookName)) {
                    foundBookISBN = ISBN;
                    info = currentInfo;
                    break;
                }
            }
        }

        if (foundBookISBN == -1 || info == null) {
            System.out.println("Did not find book called " + bookName + ".");
        } 
        else{
            System.out.println("Book Found!");
            for (Map.Entry<?, ?> entry : info.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("\nCheck out book? 1 - Yes, 2 - No");
            int checkOutSelection = getUserInput(new int[]{1,2});
            if (checkOutSelection == 1){
                library.borrowBooks(user, foundBookISBN);
            }
        }
    }
    private static void openBrowseSession(LibraryManagement library, User user){ //time to debugg yippeee
        System.out.println("Opening Book Browser...");
        Map<Long, BookManagement> catalog = library.getBookManagers();
        Long[] keys = catalog.keySet().toArray(Long[]::new);
        int page = 0;
        int maxPage = (int)Math.ceil(keys.length / 10); 
        while (true){
            System.out.println("Page " + (page + 1) + "/" + (maxPage + 1));
            for (int i = page * 10; i < (int)Math.min(page * 10 + 10, keys.length); i++){
                long ISBN = keys[i].longValue();
                Map<?, ?> info = catalog.get(ISBN).getBookInfo(); //what does this do doesn't display any2
                System.out.println((i + 1) + ". " + info.get("Title") + " (ISBN: " + ISBN + ")"); // i added this
            }
            System.out.println("| To select a book, type its ISBN.");
            if (page != maxPage){
                System.out.println("| To go to the next page, type n.");
            }
            if (page != 0){
                System.out.println("| To go to the previous page, type p.");
            }
            System.out.println("| To exit, type 0.");
            String input = getUserInput(new String[]{}, 0).trim();
            switch (input){
                case "p":
                    if (page != 0){
                        page --;
                    }
                    break;
                case "n":
                    if (page != maxPage){
                        page ++;
                    }
                    break;
                case "0":
                    return;
                default:
                    try{
                        long ISBN = Long.parseLong(input);
                        if (catalog.containsKey(ISBN)){
                            System.out.println("Book Found!");
                            Map<?,?> bookInfo = catalog.get(ISBN).getBookInfo();
                            for (Map.Entry<?, ?> entry : bookInfo.entrySet()) {
                                System.out.println(entry.getKey() + ": " + entry.getValue());
                            }
                            System.out.println("Check out book? 1 - Yes, 2 - No");
                            int checkOutSelection = getUserInput(new int[]{1,2});
                            if (checkOutSelection == 1){
                                library.borrowBooks(user, ISBN);
                                System.out.println("Checked out " + bookInfo.get("Title") + ".");
                                System.out.println();
                            }
                        }
                        else{
                            System.out.println("Did not find book with ISBN " + ISBN + ".");
                        }

                    }
                    catch(Exception e){
                        System.out.println("Invalid Input.");
                    }
            }
        }
    }
    private static void openReturnBookSession(LibraryManagement library, User user){
        ArrayList<Book> borrowedBooks = user.getBorrowedBooks();
        if (borrowedBooks.isEmpty()){ 
            System.out.println("You have no borrowed books. Borrow a book to return it.");
        }
        else{
            System.out.println("Your borrowed books:"); // its better to do it as one because whether the user wants to see all their borrowed books or wants to return a book they'll be shown thei
            for (int i = 0; i < borrowedBooks.size(); i++){
                Book book = borrowedBooks.get(i);
                System.out.println((i+1) + ": " + book.getTitle() + " by " + book.getAuthor() + " - Borrowed " + book.getBorrowedDate());
            }
            System.out.println("To return a book, please enter the number to the left of the book title. Type 0 to escape.");
            while (true){
                int input = getUserInput(new int[]{});
                if (input == 0){
                    break;
                }
                else if (input > borrowedBooks.size() || input < 0){
                    System.out.println("You do not have a book borrowed at index " + input + ".");
                    continue;
                }
                Book bookToReturn = borrowedBooks.get(input - 1);
                library.returnBooks(user, bookToReturn.getISBN());
                System.out.println("You have returned " + bookToReturn.getTitle() + ".");
                break;
            }
        }
    }
    
    private static void openLostBookSession(LibraryManagement library, User user){
        ArrayList<Book> borrowedBooks = user.getBorrowedBooks();
        if (borrowedBooks.isEmpty()){ 
            System.out.println("You have no borrowed books");
        }
        else{
            System.out.println("Your borrowed books:"); // its better to do it as one because whether the user wants to see all their borrowed books or wants to return a book they'll be shown thei
            for (int i = 0; i < borrowedBooks.size(); i++){
                Book book = borrowedBooks.get(i-1);
                System.out.println(i + ":" + book.getTitle() + " by " + book.getAuthor() + " - Borrowed " + book.getBorrowedDate());
            } 
            System.out.println("To mark a book as lost, please enter the number to the left of the book title. Type 0 to escape."); 
            while (true){ 
                int input = getUserInput(new int[]{});
                if (input == 0){
                    break;
                }
                else if (input > borrowedBooks.size() || input < 0){
                    System.out.println("You do not have a book borrowed at index " + input + ".");
                    continue;
                }
                Book bookToRemove = borrowedBooks.get(input - 1);
                library.removeBook(user, bookToRemove);
                System.out.println("You have marked " + bookToRemove.getTitle() + " as lost. This removes the copy permanently.");
                user.setPenalty(!user.getOverdue().isEmpty());  
                break;
            }
            
        }

        
    }
     
    private static void openAccountCreationSession(LibraryManagement library){
        System.out.println("Beginning Account Creation.");
        System.out.println("Please enter your first name: ");
        String firstName = getUserInput(new String[]{}, 2);
        System.out.println("Please enter your last name: ");
        String lastName = getUserInput(new String[]{}, 2);
        while (true) {
            System.out.println("Please enter your username (min 5 characters):  ");
            String username = getUserInput(new String[]{}, 5);
            if (library.checkUnique(username)) {
                User newUser = new User(firstName, lastName, username);
                library.addUsers(newUser);
                System.out.println("Welcome to " + library.getName() + " library, " + newUser.getName() +"! Your user ID is " + newUser.getUserID() + ". Please remember this ID for future reference.");
                break;
        }       
        System.out.println("Username already taken! Please try again.");
        }
    }

    private static void OpenAdminSession(LibraryManagement library){
        System.out.println("Please enter the admin password");
        String password = getUserInput(new String[]{}, 5);
        if (!password.equals(ADMIN_PASSWORD)){
            System.out.println("Incorrect password. Returning to main menu.");
            return;
        }
        System.out.println("Welcome, admin!");
        while(true){
            System.out.println("\n| To view the full catalog, type 1");
            System.out.println(("| To view all waitlists, type 2"));
            System.out.println(("| To log out, type 3"));
            switch (getUserInput(new int[]{1,2,3})){
                case 1:
                    for (BookManagement manager : library.getBookManagers().values()){
                        Map<?, ?> info = manager.getBookInfo();
                        String title = (info == null) ? "(no copies)" : String.valueOf(info.get("Title"));
                        System.out.println("| ISBN " + manager.getISBN() + " - " + title + " (" + manager.getAvailableBooks().size()+ " of " + manager.getBookCount() + " available)");

                    }
                    break;
                case 2:
                    for (BookManagement manager : library.getBookManagers().values()){
                        if (!manager.getWaitlist().isEmpty()){
                            System.out.println("| ISBN " + manager.getISBN() + " - " + manager.getWaitlist().size() + " waiting");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Admin logged out.");
                    return;
        }
    }
    }
    /**
     * 
     * @param libraryName
     */
    public static void OpenSession(String libraryName){
        LibraryManagement library = new LibraryManagement(libraryName);
        User simon = new User("Simon", "Zu", "simonzu");
        User jerry = new User("Xiaoran", "Xiong", "madpleb");
        User jayden = new User("Jayden", "Ho", "lotusjayden");
        
        library.addUsers(new User[]{simon, jerry, jayden});
        library.addBooks(new Book[]{
            new Novel("The Great Gatsby", 180, Book.Condition.NEW, "F. Scott", "Fitzgerald", 19250410, 743273567, Novel.Genre.CLASSIC, true),
            new Novel("To Kill a Mockingbird", 281, Book.Condition.EXCELLENT, "Harper", "Lee", 19600711, 61120081, Novel.Genre.CLASSIC, true),
            new Novel("1984", 328, Book.Condition.GOOD, "George", "Orwell", 19490608, 451524934, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("Pride and Prejudice", 279, Book.Condition.FAIR, "Jane", "Austen", 18130128, 140003341, Novel.Genre.ROMANCE, true),
            new Novel("The Hobbit", 310, Book.Condition.POOR, "J.R.R.", "Tolkien", 19370921, 345339681, Novel.Genre.FANTASY, true),
            new Novel("The Great Gatsby", 180, Book.Condition.NEW, "F. Scott", "Fitzgerald", 19250410, 743273567, Novel.Genre.CLASSIC, true),
            new Novel("The Great Gatsby", 180, Book.Condition.NEW, "F. Scott", "Fitzgerald", 19250410, 743273567, Novel.Genre.CLASSIC, true),
            new ChildrensBook("Where the Wild Things Are", 48, Book.Condition.EXCELLENT, "Maurice", "Sendak", 1984, 97800602, 740),
            new ChildrensBook("Goodnight Moon", 32, Book.Condition.NEW, "Margaret", "Brown", 1984, 9780060, 360),
            new Novel("Moby-Dick", 635, Book.Condition.GOOD, "Herman", "Melville", 18511018, 55321311, Novel.Genre.CLASSIC, true),
            new Novel("The Catcher in the Rye", 234, Book.Condition.EXCELLENT, "J.D.", "Salinger", 19510716, 31676948, Novel.Genre.CLASSIC, true),
            new Novel("Lord of the Flies", 182, Book.Condition.GOOD, "William", "Golding", 19540917, 39950148, Novel.Genre.CLASSIC, true),
            new Novel("Frankenstein", 280, Book.Condition.POOR, "Mary", "Shelley", 18180101, 48628211, Novel.Genre.CLASSIC, true),
            new Novel("Crime and Punishment", 545, Book.Condition.GOOD, "Fyodor", "Dostoevsky", 18660101, 67978114, Novel.Genre.CLASSIC, true),
            new Novel("The Picture of Dorian Gray", 254, Book.Condition.EXCELLENT, "Oscar", "Wilde", 18900701, 48627841, Novel.Genre.CLASSIC, true),
            new Novel("Great Expectations", 544, Book.Condition.FAIR, "Charles", "Dickens", 18610801, 45153074, Novel.Genre.CLASSIC, true),
            new Novel("Of Mice and Men", 112, Book.Condition.POOR, "John", "Steinbeck", 19370101, 14017738, Novel.Genre.CLASSIC, true),
            new Novel("The Grapes of Wrath", 464, Book.Condition.GOOD, "John", "Steinbeck", 19390414, 14003993, Novel.Genre.CLASSIC, true),
            new Novel("Animal Farm", 141, Book.Condition.NEW, "George", "Orwell", 19450817, 45228424, Novel.Genre.CLASSIC, true),
            new Novel("Catch-22", 453, Book.Condition.EXCELLENT, "Joseph", "Heller", 19611110, 74327812, Novel.Genre.CLASSIC, true),
            new Novel("Beloved", 324, Book.Condition.GOOD, "Toni", "Morrison", 19870902, 14003392, Novel.Genre.CLASSIC, true),
            new Novel("One Hundred Years of Solitude", 417, Book.Condition.FAIR, "Gabriel", "García Márquez", 19670530, 60883488, Novel.Genre.CLASSIC, true),
            new Novel("Brave New World", 288, Book.Condition.FAIR, "Aldous", "Huxley", 19320204, 60803542, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("Dune", 412, Book.Condition.NEW, "Frank", "Herbert", 19650601, 44117276, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("Fahrenheit 451", 249, Book.Condition.GOOD, "Ray", "Bradbury", 19531019, 74324722, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("The War of the Worlds", 192, Book.Condition.FAIR, "H.G.", "Wells", 18980412, 14043954, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("The Handmaid's Tale", 311, Book.Condition.EXCELLENT, "Margaret", "Atwood", 19850901, 38572485, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("Ender's Game", 324, Book.Condition.NEW, "Orson Scott", "Card", 19850115, 81255070, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("Neuromancer", 271, Book.Condition.GOOD, "William", "Gibson", 19840701, 44156959, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("Foundation", 255, Book.Condition.EXCELLENT, "Isaac", "Asimov", 19510601, 55329338, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("I, Robot", 253, Book.Condition.GOOD, "Isaac", "Asimov", 19501202, 55329460, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("The Martian", 387, Book.Condition.NEW, "Andy", "Weir", 20140211, 55341987, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("Do Androids Dream of Electric Sheep?", 210, Book.Condition.GOOD, "Philip K.", "Dick", 19680101, 34524200, Novel.Genre.SCIENCE_FICTION, true),
            new Novel("A Game of Thrones", 694, Book.Condition.NEW, "George R.R.", "Martin", 19960801, 55357340, Novel.Genre.FANTASY, true),
            new Novel("The Name of the Wind", 662, Book.Condition.EXCELLENT, "Patrick", "Rothfuss", 20070327, 75640474, Novel.Genre.FANTASY, true),
            new Novel("The Lion, the Witch and the Wardrobe", 172, Book.Condition.GOOD, "C.S.", "Lewis", 19501016, 60594035, Novel.Genre.FANTASY, true),
            new Novel("A Wizard of Earthsea", 183, Book.Condition.FAIR, "Ursula K.", "Le Guin", 19681101, 55326732, Novel.Genre.FANTASY, true),
            new Novel("The Way of Kings", 1007, Book.Condition.NEW, "Brandon", "Sanderson", 20100831, 76532635, Novel.Genre.FANTASY, true),
            new Novel("Mistborn: The Final Empire", 541, Book.Condition.GOOD, "Brandon", "Sanderson", 20060717, 76531357, Novel.Genre.FANTASY, true),
            new Novel("The Fellowship of the Ring", 398, Book.Condition.EXCELLENT, "J.R.R.", "Tolkien", 19540729, 61826027, Novel.Genre.FANTASY, true),
            new Novel("The Hobbit", 310, Book.Condition.GOOD, "J.R.R.", "Tolkien", 19370921, 345339681, Novel.Genre.FANTASY, true),
            new Novel("Jane Eyre", 500, Book.Condition.EXCELLENT, "Charlotte", "Brontë", 18471016, 15930820, Novel.Genre.ROMANCE, true),
            new Novel("Wuthering Heights", 416, Book.Condition.FAIR, "Emily", "Brontë", 18471201, 14722573, Novel.Genre.ROMANCE, true),
            new Novel("The Notebook", 214, Book.Condition.NEW, "Nicholas", "Sparks", 19961001, 44652054, Novel.Genre.ROMANCE, true),
            new Novel("Outlander", 627, Book.Condition.GOOD, "Diana", "Gabaldon", 19910601, 44021253, Novel.Genre.ROMANCE, true),
            new Novel("Pride and Prejudice", 279, Book.Condition.POOR, "Jane", "Austen", 18130128, 140003341, Novel.Genre.ROMANCE, true),
            new Novel("Me Before You", 349, Book.Condition.EXCELLENT, "Jojo", "Moyes", 20121201, 73351163, Novel.Genre.ROMANCE, true),
            new Textbook("Calculus: Early Transcendentals", 1368, Book.Condition.GOOD, "James", "Stewart", 20150101, 128574155, Textbook.Subject.MATH, "AP Calculus BC"),
            new Textbook("Campbell Biology", 1488, Book.Condition.NEW, "Lisa", "Urry", 20200115, 135188741, Textbook.Subject.SCIENCE, "AP Biology"),
            new Textbook("Introduction to Algorithms", 1312, Book.Condition.EXCELLENT, "Thomas", "Cormen", 20090731, 262033844, Textbook.Subject.COMPUTER_SCIENCE, "Data Structures & Algorithms"),
            new Textbook("Principles of Economics", 888, Book.Condition.FAIR, "N. Gregory", "Mankiw", 20170101, 130558012, Textbook.Subject.ECONOMICS, "Intro to Microeconomics"),
            new Textbook("The American Pageant", 1152, Book.Condition.POOR, "David", "Kennedy", 20180101, 133761622, Textbook.Subject.HISTORY, "AP U.S. History"),
            new Textbook("Chemistry: The Central Science", 1248, Book.Condition.EXCELLENT, "Theodore", "Brown", 20170104, 134414232, Textbook.Subject.SCIENCE, "General Chemistry I"),
            new Textbook("Psychology", 864, Book.Condition.GOOD, "David", "Myers", 20201015, 131913210, Textbook.Subject.PSYCHOLOGY, "Introductory Psychology"),
            new Textbook("Linear Algebra and Its Applications", 576, Book.Condition.NEW, "David", "Lay", 20150103, 321982381, Textbook.Subject.MATH, "Linear Algebra"),
            new Textbook("Operating System Concepts", 976, Book.Condition.GOOD, "Abraham", "Silberschatz", 20180504, 111980036, Textbook.Subject.COMPUTER_SCIENCE, "Operating Systems"),
            new Textbook("Western Heritage", 1120, Book.Condition.FAIR, "Donald", "Kagan", 20130101, 205962401, Textbook.Subject.HISTORY, "AP European History"),
            new Textbook("Engineering Mechanics: Statics", 672, Book.Condition.EXCELLENT, "Russell", "Hibbeler", 20160212, 133918922, Textbook.Subject.ENGINEERING, "Mechanics I"),
            new Textbook("Robbins Basic Pathology", 952, Book.Condition.NEW, "Vinay", "Kumar", 20170411, 323353177, Textbook.Subject.MEDICINE, "Pathology Fundamentals"),
            new Textbook("The Norton Anthology of English Literature", 3072, Book.Condition.POOR, "Stephen", "Greenblatt", 20180611, 393603120, Textbook.Subject.LITERATURE, "British Literature Survey"),
            new Textbook("Business Law: Text and Cases", 1328, Book.Condition.GOOD, "Kenneth", "Clarkson", 20200101, 357129631, Textbook.Subject.LAW, "Legal Environment of Business"),
            new ChildrensBook("The Cat in the Hat", 61, Book.Condition.GOOD, "Dr.", "Seuss", 1957, 39480001, 5),
            new ChildrensBook("Green Eggs and Ham", 62, Book.Condition.EXCELLENT, "Dr.", "Seuss", 1960, 39480016, 5),
            new ChildrensBook("The Very Hungry Caterpillar", 26, Book.Condition.NEW, "Eric", "Carle", 1969, 39922674, 3),
            new ChildrensBook("Charlotte's Web", 192, Book.Condition.GOOD, "E.B.", "White", 1952, 60261398, 8),
            new ChildrensBook("Where the Sidewalk Ends", 176, Book.Condition.FAIR, "Shel", "Silverstein", 1974, 60257398, 9),
            new ChildrensBook("The Giving Tree", 64, Book.Condition.NEW, "Shel", "Silverstein", 1964, 60525666, 5),
            new ChildrensBook("Curious George", 64, Book.Condition.GOOD, "H.A.", "Rey", 1941, 39518939, 4),
            new ChildrensBook("Corduroy", 40, Book.Condition.EXCELLENT, "Don", "Freeman", 1968, 67024903, 3),
            new ChildrensBook("The Tale of Peter Rabbit", 56, Book.Condition.POOR, "Beatrix", "Potter", 1902, 72474646, 4),
            new ChildrensBook("Love You Forever", 32, Book.Condition.NEW, "Robert", "Munsch", 1986, 92068685, 4),
            new ChildrensBook("The Snowy Day", 40, Book.Condition.GOOD, "Ezra Jack", "Keats", 1962, 67086737, 4),
            new ChildrensBook("Harold and the Purple Crayon", 64, Book.Condition.FAIR, "Crockett", "Johnson", 1955, 60213801, 5)
        });
        while (true){
            System.out.println("\nWelcome to " + libraryName + " library!");
            System.out.println("| To log in, type 1");
            System.out.println("| To create a new account, type 2");
            System.out.println("| To log in as admin, type 3");
            System.out.println("| To leave the system, type 4");
            int i = getUserInput(new int[]{1,2,3,4});
            switch (i){
                case 1:
                    // Log in
                    openLoginSession(library);
                    continue;
                case 2:
                    // Create new account
                    openAccountCreationSession(library);
                    continue;
                case 3:
                    // Log in as admin
                    OpenAdminSession(library);
                    continue;
                case 4:
                    System.out.println("Thank you for using " + libraryName + " library management system. Goodbye!");
                    return;
                default:
                    System.out.println("Your input wasn't a valid command. Please try again.");
                    continue;
            }
        }
    }
}