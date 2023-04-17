import org.apache.commons.io.IOUtils;
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

        System.out.println(patterns.printResults());
    }
}
