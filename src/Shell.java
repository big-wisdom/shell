import java.util.Scanner;

class Shell {
    private static String workingDirectory = System.getProperty("user.dir");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while(true){
            prompt();
        }
    }

    private static void prompt(){
        System.out.print("["+workingDirectory+"] ");
        String response = scanner.nextLine();
        System.out.println(response);
    }
}