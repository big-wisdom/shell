import java.io.File;

public class rdir extends Command{
    rdir(String[] userInput){
        command = userInput;
        StringBuilder stringBuilder = new StringBuilder();
        for(String piece: userInput)
            stringBuilder.append(piece);
        commandString = stringBuilder.toString();
    }

    @Override
    public Boolean run() {
        if (command.length > 1) {
            File file = new File(System.getProperty("user.dir") + "/" + command[1]);
            if (file.exists()) file.delete();
            else System.out.println("directory does not exist");
        } else {
            System.out.println("Usage: rdir <directory name>");
        }
        return true;
    }
}
