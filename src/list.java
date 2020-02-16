import java.io.File;
import java.util.Date;

public class list extends Command{

    list(String[] userInput){
        command = userInput;
        StringBuilder stringBuilder = new StringBuilder();
        for(String piece: userInput)
            stringBuilder.append(piece);
        commandString = stringBuilder.toString();
    }

    @Override
    public Boolean run(){
        try {
            File folder = new File(System.getProperty("user.dir"));
            for (File file : folder.listFiles()) {
                // Print Permissions
                String directory = file.isDirectory() ? "d" : "-";
                String read = file.canRead() ? "r" : "-";
                String write = file.canWrite() ? "w" : "-";
                String execute = file.canExecute() ? "x" : "-";
                System.out.print(directory + read + write + execute + "  ");

                // Print size
                System.out.printf("%10d ", file.length());

                // print last modified date
                System.out.print(new Date(file.lastModified()) + "  ");

                // Print title of the file
                System.out.println(file.getName());
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
