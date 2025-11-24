import java.util.Scanner;

public class ConsoleMenu {

    public void showMainMenu() {
        System.out.println("=====STATISTICS=====");
    }

    public String getTargetAttribute() {
        System.out.println("Enter the target attribute: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public String getPathToFiles() {
        System.out.println("Enter the path to the files: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }


}
