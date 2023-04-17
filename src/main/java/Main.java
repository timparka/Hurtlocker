import org.apache.commons.io.IOUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public String readRawDataToString() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        String rawData = main.readRawDataToString();

        PatternMatching patterns = new PatternMatching(rawData);

        List<String> entries = patterns.splitEntries();
        patterns.processEntries(entries);
        writeToFile(patterns.printResults());

        System.out.println(patterns.printResults());
    }
    public static void writeToFile(String input) {
        try {
            FileWriter writer = new FileWriter("output-test.txt");
            writer.write(input);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
