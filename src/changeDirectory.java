import java.io.File;

public class changeDirectory extends Command{
    changeDirectory(String[] userInput){
        command = userInput;
        StringBuilder stringBuilder = new StringBuilder();
        for(String piece: userInput)
            stringBuilder.append(piece);
        commandString = stringBuilder.toString();
    }

    @Override
    public Boolean run(){
        // If no arguments
        String directory;
        if(command.length <= 1) System.setProperty("user.dir",System.getProperty("user.home"));
        else {
            File f = new File(command[1]);
            // if .. given, go to parent directory
            if(command[1].equals("..")) {
                int chop = System.getProperty("user.dir").lastIndexOf("/");
                System.setProperty("user.dir", System.getProperty("user.dir").substring(0, chop));
            } else {
                if(f.isAbsolute()) directory = command[1]; // make absolute
                else directory = System.getProperty("user.dir")+"/"+command[1];
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
        return true;
    }
}
