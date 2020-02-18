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
            Command[] commands = commandFactory.getCommands(userInput, 0);
            Boolean log = false;
            // if multiple commands, pipe them together currently only supports two commands piped
            if (commands.length > 1) {
                // TODO: pipe
                System.out.println("Pipe commands here");
            } else if(commands.length == 1){
                // otherwise just run it
                log = commands[0].run();
            }
            // log commands if successful
            if(log) logCommand(userInput);
        }

    }

    public static void logCommand(String command) {
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

    private static String prompt() {
        System.out.print("[" + System.getProperty("user.dir") + "]: ");
        return scanner.nextLine();
    }
}
