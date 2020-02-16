import java.io.*;
import java.util.Scanner;

class Shell {
    private static Scanner scanner = new Scanner(System.in);
    private static double timeWaiting = 0;

    public static void main(String[] args) {
        CommandFactory commandFactory = new CommandFactory();
        String userInput;

        // main loop
        while (!(userInput = prompt()).startsWith("exit")) {

            Command[] commands = commandFactory.getCommands(userInput);
            // if multiple commands, pipe them together currently only supports two commands piped
            if (commands.length > 1) {
                //TODO: pipe
                System.out.println("Pipe commands here");
            } else if(commands.length == 1){
                // otherwise just run it
                commands[0].run();
            }
            // log commands if successful
        }

    }


    private static void history() {
        try {
            FileReader fileReader = new FileReader("History.txt");
            int i;
            while ((i = fileReader.read()) != -1) System.out.print((char) i);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logCommand(String command) {
        try {
            FileWriter myWriter = new FileWriter("History.txt", true);
            myWriter.write(command + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void pipeCommands(String[] p1Cmd, String[] p2Cmd) {
        ProcessBuilder pb1 = new ProcessBuilder(p1Cmd);
        ProcessBuilder pb2 = new ProcessBuilder(p2Cmd);
        pb1.directory(new File(System.getProperty("user.dir")));
        pb2.directory(new File(System.getProperty("user.dir")));

        pb1.redirectInput(ProcessBuilder.Redirect.INHERIT);

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
        } catch (Exception ex) {
        }
    }

    private static void rdir(String[] userInput) {
        if (userInput.length > 1) {
            File file = new File(System.getProperty("user.dir") + "/" + userInput[1]);
            if (file.exists()) file.delete();
            else System.out.println("directory does not exist");
        } else {
            System.out.println("Usage: rdir <directory name>");
        }
    }

    private static void mdir(String[] userInput) {
        if (userInput.length > 1) {
            String path = System.getProperty("user.dir") + "/" + userInput[1];
            File folder = new File(path);
            if (!folder.mkdir()) {
                System.out.println("Unsuccessful, \"" + path + "\" already exists.");
            }
        } else {
            System.out.println("Usage: mdir <directory name>");
        }
    }

    private static String prompt() {
        System.out.print("[" + System.getProperty("user.dir") + "]: ");
        return scanner.nextLine();
    }
}
