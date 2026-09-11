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
    public static int getUserInput(String[] allowedInputs){
        String[] allowedInputsStr = new String[allowedInputs.length];
        for (int i = 0; i < allowedInputs.length; i++){
            allowedInputsStr[i] = allowedInputs[i];
        }
        return Integer.parseInt(getUserInput(allowedInputsStr, 0));

    }
    private static void OpenLoginSession(LibraryManagement library){
        User user;
            System.out.println("To log in, please enter your username (min 5 characters): ");

        while (true){
            String username = getUserInput(new String[]{}, 5);
        }

    }
    private static void OpenAccountCreationSession(LibraryManagement library){
        System.out.println("Please enter your first name: ");
        String firstName = getUserInput(new String[]{}, 2);
        System.out.println("Please enter your last name: ");
        String lastName = getUserInput(new String[]{}, 2);
        System.out.println("Please enter your username (min 5 characters):  ");
        String username = getUserInput(new String[]{}, 5);
    }
    
    private static void OpenAdminSession(LibraryManagement library){
        System.out.println("Please enter the admin password");
        String password = getUserInput(new String[]{}, 5);
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
            System.out.print("| >> ");
            int i;
            Scanner scanner = new Scanner(System.in);
            try{
                // Scanner scanner = new Scanner(System.in);
                i = scanner.nextInt();
            }
            catch(Exception e){
                System.out.println("Your input wasn't a valid command. Please try again.");
                i = scanner.nextInt();
                continue;
            }
            switch (i){
                case 1:
                    // Log in
                    OpenLoginSession(library);
                    continue;
                case 2:
                    // Create new account
                    OpenAccountCreationSession(library);
                    return;
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