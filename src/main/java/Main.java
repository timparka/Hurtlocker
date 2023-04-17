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

        //splits the string by ## and adds it to an array called entries
        List<String> entries = new ArrayList<>();
        Pattern splitPattern = Pattern.compile("(.*?)(##|$)");//from the start of anything till ## which is signified by $
        Matcher splitMatcher = splitPattern.matcher(rawData);

        while (splitMatcher.find()) {
            if (splitMatcher.hitEnd() && splitMatcher.group(1).isEmpty()) {
                break;
            }
            entries.add(splitMatcher.group(1));
        }

        //new class for pattern recognition
        Map<String, Map<Double, Integer>> items = new LinkedHashMap<>();
        //counts errors
        int errorCounter = 0;

        Pattern namePattern = Pattern.compile("(?i)(name:)(.+?)([;@^*%])");
        Pattern pricePattern = Pattern.compile("(?i)(price:)(.+?)([;@^*%])");

        Pattern milkPattern = Pattern.compile("(?i)milk");
        Pattern breadPattern = Pattern.compile("(?i)bread");
        Pattern cookiesPattern = Pattern.compile("(?i)co[o0]kies");
        Pattern applesPattern = Pattern.compile("(?i)apples");

        for (String entry : entries) {
            Matcher nameMatcher = namePattern.matcher(entry);
            Matcher priceMatcher = pricePattern.matcher(entry);

            if (nameMatcher.find() && priceMatcher.find()) {
                String name = nameMatcher.group(2);
                String names;

                if (milkPattern.matcher(name).matches()) {
                    names = "Milk";
                } else if (breadPattern.matcher(name).matches()) {
                    names = "Bread";
                } else if (cookiesPattern.matcher(name).matches()) {
                    names = "Cookies";
                } else if (applesPattern.matcher(name).matches()) {
                    names = "Apples";
                } else {
                    errorCounter++;
                    continue;
                }

                Double price;

                try {
                    price = Double.parseDouble(priceMatcher.group(2));
                } catch (NumberFormatException e) {
                    errorCounter++;
                    continue;
                }

                if (!names.isEmpty()) {
                    items.putIfAbsent(names, new LinkedHashMap<>());
                    Map<Double, Integer> prices = items.get(names);
                    prices.put(price, prices.getOrDefault(price, 0) + 1);
                } else {
                    errorCounter++;
                }
            } else {
                errorCounter++;
            }
        }

    }
}
