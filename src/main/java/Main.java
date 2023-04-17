import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public String readRawDataToString() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        String rawData = main.readRawDataToString();

        List<String> entries = new ArrayList<>();
        Pattern splitPattern = Pattern.compile("(.*?)(##|$)");
        Matcher splitMatcher = splitPattern.matcher(rawData);

        while (splitMatcher.find()) {
            entries.add(splitMatcher.group(1));
        }
    }
}
