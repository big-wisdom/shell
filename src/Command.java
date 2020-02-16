import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class Command{
    String[] command;
    String commandString;
    static double timeWaiting = 0;

    Command(){}

    Command(String[] userInput){
        command = userInput;
        StringBuilder stringBuilder = new StringBuilder();
        for(String piece: userInput)
            stringBuilder.append(piece);
        commandString = stringBuilder.toString();
    }

    public Boolean run(){
        if (command[0].equals("cd")) return false;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(System.getProperty("user.dir")));
            String s;
            BufferedReader br;

            // Start timer
            double start = System.currentTimeMillis();
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

            double end = System.currentTimeMillis();
            timeWaiting += (end - start);

            return true;

        } catch(IOException | InterruptedException e) {
            return false;
        }
    }
}