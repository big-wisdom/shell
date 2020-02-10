import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Shell {
    private static String workingDirectory = System.getProperty("user.dir");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while(true){
            String[] userInput = splitCommand(prompt());

            if(userInput[0] == "exit") break; // If quit then quit

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

    private static void runExternalCommand(String[] args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args[0]);
            String s;
            BufferedReader br;

            // Start timer
            // Start Process
            Process process = processBuilder.start();
            process.waitFor();
            // Check exit value
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

    public static String[] splitCommand(String command) {
        java.util.List<String> matchList = new java.util.ArrayList<>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }

        return matchList.toArray(new String[matchList.size()]);
    }
}