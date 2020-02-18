import java.io.FileReader;
import java.io.IOException;

public class history extends Command{
    history(String[] userInput){
        command = userInput;
        StringBuilder stringBuilder = new StringBuilder();
        for(String piece: userInput)
            stringBuilder.append(piece);
        commandString = stringBuilder.toString();
    }

    @Override
    public Boolean run(){
        try {
            FileReader fileReader = new FileReader("History.txt");
            int i;
            while ((i = fileReader.read()) != -1) System.out.print((char) i);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
