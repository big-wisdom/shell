public class ptime extends Command{
    ptime(String[] userInput){
        command = userInput;
        StringBuilder stringBuilder = new StringBuilder();
        for(String piece: userInput)
            stringBuilder.append(piece);
        commandString = stringBuilder.toString();
    }

    @Override
    public Boolean run(){
        System.out.println("Time spend in child processes "+(Command.timeWaiting/1000)+" seconds");
        return true;
    }
}
