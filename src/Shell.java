import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

class Shell {
    private static String workingDirectory = System.getProperty("user.dir");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while(true){
            String userInput = prompt();

            if(userInput == "exit") break; // If quit then quit

            runExternalCommand(userInput); // Try to execute external command

            // try to run built in command
            // Execute command
            // End timer and add time to CPU time

            // If the command is invalid
        }
    }

    private static String prompt(){
        System.out.print("["+workingDirectory+"]: ");
        return scanner.nextLine();
    }

    private static void runExternalCommand(String args) {
        try {
            String[] userInput = args.split(" ");
            ProcessBuilder processBuilder = new ProcessBuilder(userInput[0]);
            String s;
            BufferedReader br;

            // Start timer
            // Start Process
            Process process = processBuilder.start();
            process.waitFor();
            // Check exit value
            System.out.println("Eli's Here");
            if(process.exitValue() != 0) {
                br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            }
            // Print output
            while ((s = br.readLine()) != null)
                System.out.println(s);

        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}