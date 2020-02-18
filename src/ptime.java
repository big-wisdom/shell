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
        System.out.printf("Time spend in child processes %.4f seconds\n",Command.timeWaiting/1000);
        return true;
    }
}
