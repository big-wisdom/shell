import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CommandFactory{

    public void getCommands(String userInput) {
        String[] input = splitCommand(userInput);
        // Generate an arraylist of command string arrays
        ArrayList<String[]> commandStrings = seperateCommands(input);

        // create default external command object unless build in command recognized
        ArrayList<Command> commands = new ArrayList<>();
        for(String[] commandString: commandStrings){
            switch (commandString[0]){
                case "list":
                    System.out.println("list");
                    break;
                case "cd":
                    System.out.println("cd");
                    break;
                case "ptime":
                    System.out.println("ptime");
                    break;
                case "mdir":
                    System.out.println("mdir");
                    break;
                case "rdir":
                    System.out.println("rdir");
                    break;
                case "history":
                    System.out.println("history");
                    break;
                case "^":
                    System.out.println("^");
                    break;
                default:
                    System.out.println("Command Not Found!");
            }
        }
    }

    private ArrayList<String[]> seperateCommands(String[] input){
        ArrayList<String[]> commandArrays = new ArrayList<>();
        Boolean piped = false;
        for(int x=0; x<input.length; x++)
            if(input[x].equals("|")) {
                commandArrays.add(Arrays.copyOfRange(input, 0, x));
                commandArrays.add(Arrays.copyOfRange(input, x + 1, input.length));
                piped = true;
            }
        if(!piped) commandArrays.add(input);
        return commandArrays;
    }

    private static String[] splitCommand(String command) {
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