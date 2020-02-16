import java.io.File;

public class mdir extends Command{
    mdir(String[] userInput){
        command = userInput;
        StringBuilder stringBuilder = new StringBuilder();
        for(String piece: userInput)
            stringBuilder.append(piece);
        commandString = stringBuilder.toString();
    }

    @Override
    public Boolean run(){
        if (command.length > 1) {
            String path = System.getProperty("user.dir") + "/" + command[1];
            File folder = new File(path);
            if (!folder.mkdir()) {
                System.out.println("Unsuccessful, \"" + path + "\" already exists.");
            }
        } else {
            System.out.println("Usage: mdir <directory name>");
        }
        return true;
    }
}
