import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CommandFactory{
    public Command[] getCommands(String userInput) {
        String[] input = splitCommand(userInput);
        // Generate an arraylist of command string arrays
        ArrayList<String[]> commandStrings = seperateCommands(input);

        // create default external command object unless build in command recognized
        ArrayList<Command> commands = new ArrayList<>();
        for(String[] commandString: commandStrings){
            switch (commandString[0]){
                case "list":
                    commands.add(new list(commandString));
                    break;
                case "cd":
                    commands.add(new changeDirectory(commandString));
                    break;
                case "ptime":
                    System.out.println(Command.timeWaiting/1000);
                    break;
                case "mdir":
                    commands.add(new mdir(commandString));
                    break;
                case "rdir":
                    commands.add(new rdir(commandString));
                    break;
                case "history":
                    commands.add(new history(commandString));
                    break;
                case "^":
                    System.out.println("^");
                    break;
                default:
                    commands.add(new Command(commandString));
            }
        }
        return commands.toArray(new Command[commands.size()]);
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