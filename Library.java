import java.util.*;
public class Library{
    public static void main(String[] args){
        Library.OpenSession("Milpitas");
    }
    public static String getUserInput(String[] allowedInputs, int minLength){
        while (true){
            System.out.print("| >> ");
            String str;
            try{
                Scanner scanner = new Scanner(System.in);
                str = scanner.nextLine();
                // scanner.close();
                if (str.length() <= minLength){
                    System.out.println("Your input needs to be at least " + minLength + " characters. Please try again.");
                    continue;
                }
                if (allowedInputs.length == 0){
                    return str;
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
        String[] allowedInputsStr = new String[allowedInputs.length];
        for (int i = 0; i < allowedInputs.length; i++){
            allowedInputsStr[i] = Integer.toString(allowedInputs[i]);
        }
        return Integer.parseInt(getUserInput(allowedInputsStr, 0));

    }
    private static void openLoginSession(LibraryManagement library){
        User user;
        System.out.println("To log in, please enter your username (min 5 characters). Type 1 to escape. ");
        while (true){
            String username = getUserInput(new String[]{}, 5);
            if (username.equals("1")){
                return;
            }
            user = library.getUser(username);
            if (user != null){
                break;
            }
            System.out.println("No account found with username \"" + username + "\". Try again, or type 1 to escape.");
        }
        System.out.println("Signed in as " + user + ".");
        ArrayList<Book> overdueBooks = user.getOverdue();
        user.setPenalty(overdueBooks.size() > 0);
        if (overdueBooks.size() > 0){
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
                    System.out.println("Your borrowed books: " + user.getBorrowedBooks());
                    if (user.getBorrowedBooks().size() == 0){
                        System.out.println("You have no borrowed books. Borrow a book to return it.");
                        break; // is this break? want them to go to case 1
                    }
                    else{
                        System.out.println("To return a book, please enter the title of the book. Type 1 to escape.");
                        while (true){
                            String bookTitle = getUserInput(new String[]{}, 1);
                            if (bookTitle.equals("1")){
                                break;
                            }
                            Book bookToReturn = null;
                            for (Book borrowedBook : user.getBorrowedBooks()){
                                if (borrowedBook.getTitle().equals(bookTitle)){
                                    bookToReturn = borrowedBook;
                                    user.getBorrowedBooks().remove(borrowedBook);
                                    library.returnBooks(user, borrowedBook);
                                    break;
                                }
                            }
                            if (bookToReturn == null){
                                System.out.println("You have not borrowed a book with that title. Please try again.");
                                continue;
                            }
                            library.returnBooks(user, bookToReturn);
                            System.out.println("You have returned " + bookToReturn.getTitle() + ".");
                            break;
                        }
                    }
                    break;
                case 3:
                    break;
                case 4:
                    System.out.println("Logged out.");
                    return;
                
            }
        }

    }
    private static void openBorrowBookSession(LibraryManagement library, User user){
        if (user.getOverdue().size() > 0){
            System.out.println("You cannot borrow a book until you return all overdue books. Exiting out of borrowing books.");
            return;
        }
        System.out.println("| To search for a book by name, type 1");
        System.out.println("| To browse through our list of books, type 2");
        System.out.println("| To exit out of borrowing mode, type 3");
        int selection = getUserInput(new int[]{1,2, 3});
        switch (selection){
            case 1:
                String bookName = getUserInput(new String[]{}, 0);
                long[] bookIDs = library.getBookISBNs();
                for (long bookID : bookIDs){
                    Map info = library.getBookInfo(bookID);
                    if (((String)info.get("Title")).toLowerCase().equals(bookName)){
                        // SHOW IT TODO: FINISH THIS 
                    }
                }
                
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
    private static void openAccountCreationSession(LibraryManagement library){
        System.out.println("Please enter your first name: ");
        String firstName = getUserInput(new String[]{}, 2);
        System.out.println("Please enter your last name: ");
        String lastName = getUserInput(new String[]{}, 2);
        System.out.println("Please enter your username (min 5 characters):  ");
        String username = getUserInput(new String[]{}, 5);
        System.out.println("Account created! Your user ID is " + new User(firstName, lastName, username).getUserID() + ". Please remember this ID for future reference.");
    }
    
    private static void OpenAdminSession(LibraryManagement library){
        System.out.println("Please enter the admin password");
        String password = getUserInput(new String[]{}, 5);
        System.out.println("Welcome, admin!");
    }
    /**
     * 
     * @param libraryName
     */
    public static void OpenSession(String libraryName){
        LibraryManagement library = new LibraryManagement(libraryName);
        
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