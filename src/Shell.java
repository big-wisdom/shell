import java.io.*;
import java.util.Date;
import java.util.Scanner;

class Shell {
    private static Scanner scanner = new Scanner(System.in);
    private static double timeWaiting = 0;

    public static void main(String[] args) {
        CommandFactory commandFactory = new CommandFactory();
        while(true){
            String userInput = prompt();
            if(userInput.startsWith("exit")) break;
            commandFactory.getCommands(userInput);
        }
    }


    private static void history() {
        try {
            FileReader fileReader = new FileReader("History.txt");
            int i;
            while((i = fileReader.read()) != -1) System.out.print((char) i);
            fileReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void logCommand(String command) {
        try {
            FileWriter myWriter = new FileWriter("History.txt", true);
            myWriter.write(command+"\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void pipeCommands(String[] p1Cmd, String[] p2Cmd){
        ProcessBuilder pb1 = new ProcessBuilder(p1Cmd);
        ProcessBuilder pb2 = new ProcessBuilder(p2Cmd);
        pb1.directory(new File(System.getProperty("user.dir")));
        pb2.directory(new File(System.getProperty("user.dir")));

        pb1.redirectInput(ProcessBuilder.Redirect.INHERIT);
        //pb1.redirectOutput(ProcessBuilder.Redirect.PIPE);

        pb2.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        try {
            Process p1 = pb1.start();
            Process p2 = pb2.start();

            java.io.InputStream in = p1.getInputStream();
            java.io.OutputStream out = p2.getOutputStream();

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }

            out.flush();
            out.close();

            p1.waitFor();
            p2.waitFor();
            //TODO: Get standard error from commands
        }
        catch (Exception ex) {
        }
    }

    private static void rdir(String[] userInput){
        if(userInput.length > 1){
            File file = new File(System.getProperty("user.dir")+ "/"+userInput[1]);
            if(file.exists()) file.delete();
            else System.out.println("directory does not exist");
        } else {
            System.out.println("Usage: rdir <directory name>");
        }
    }

    private static void mdir(String[] userInput){
        if(userInput.length > 1) {
            String path = System.getProperty("user.dir") + "/" + userInput[1];
            File folder = new File(path);
            if (!folder.mkdir()) {
                System.out.println("Unsuccessful, \"" + path + "\" already exists.");
            }
        } else {
            System.out.println("Usage: mdir <directory name>");
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

}