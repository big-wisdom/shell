import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Shell {
    private static Scanner scanner = new Scanner(System.in);
    private static double timeWaiting = 0;

    public static void main(String[] args) {
        while(true){
            String[] userInput = splitCommand(prompt());

            if(userInput[0].equals("exit")) break; // If quit then quit

            if(!runExternalCommand(userInput)) { // Try to execute external command
                // try to run built in command
                switch (userInput[0]) {
                    case "list":
                        list();
                        break;
                    case "cd":
                        changeDirectory(userInput);
                        break;
                    case "ptime":
                        System.out.println("Total time in child processes: "+timeWaiting+" seconds");
                        break;
                    default:
                        System.out.println("Command Not Found!");
                }
                // End timer and add time to CPU time
            }
        }
    }

    private static void changeDirectory(String[] userInput){
        // If no arguments
        String directory;
        if(userInput.length <= 1) System.setProperty("user.dir",System.getProperty("user.home"));
        else {
            File f = new File(userInput[1]);
            // if .. given, go to parent directory
            if(userInput[1].equals("..")) {
                int chop = System.getProperty("user.dir").lastIndexOf("/");
                System.setProperty("user.dir", System.getProperty("user.dir").substring(0, chop));
            } else {
                if(f.isAbsolute()) directory = userInput[1]; // make absolute
                else directory = System.getProperty("user.dir")+"/"+userInput[1];
                f = new File(directory);
                // Check for existence
                if (f.exists()) {
                    // check if directory
                    if (f.isDirectory()){
                        System.setProperty("user.dir",directory);
                    } else System.out.println(directory+" is not a directory.");
                }
                else System.out.println("directory, "+directory+", does not exist.");
            }
        }
    }

    private static void list(){
        File folder = new File(System.getProperty("user.dir"));
        for(File file : folder.listFiles()){
            // Print Permissions
            String directory = file.isDirectory() ? "d" : "-";
            String read = file.canRead() ? "r" : "-";
            String write = file.canWrite() ? "w" : "-";
            String execute = file.canExecute() ? "x" : "-";
            System.out.print(directory+ read+ write+ execute+"  ");

            // Print size
            System.out.printf("%10d ",file.length());

            // print last modified date
            System.out.print(new Date(file.lastModified())+"  ");

            // Print title of the file
            System.out.println(file.getName());
        }
    }

    private static String prompt(){
        System.out.print("["+System.getProperty("user.dir")+"]: ");
        return scanner.nextLine();
    }

    private static Boolean runExternalCommand(String[] args) {
        if (args[0].equals("cd")) return false;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.directory(new File(System.getProperty("user.dir")));
            String s;
            BufferedReader br;

            // Start timer
            double start = java.lang.System.currentTimeMillis();
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

            double end = java.lang.System.currentTimeMillis();
            timeWaiting += (end - start);

            return true;

        } catch(IOException | InterruptedException e) {
            return false;
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