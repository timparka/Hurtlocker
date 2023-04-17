import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {
    private String rawData;
    private int errorCounter;
    private Map<String, Map<Double, Integer>> items;

    public Patterns(String rawData) {
        this.rawData = rawData;
        this.errorCounter = 0;
        this.items = new LinkedHashMap<>();
    }

    public List<String> splitEntries() {
        List<String> entries = new ArrayList<>();
        Pattern splitPattern = Pattern.compile("(.*?)(##|$)");
        Matcher splitMatcher = splitPattern.matcher(rawData);

        while (splitMatcher.find()) {
            if (splitMatcher.hitEnd() && splitMatcher.group(1).isEmpty()) {
                break;
            }
            entries.add(splitMatcher.group(1));
        }
        return entries;
    }

    public Map<String, Map<Double, Integer>> processEntries(List<String> entries) {
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
        return items;
    }

    public int getErrorCounter() {
        return errorCounter;
    }

    public void printResults() {
        for (String name : items.keySet()) {
            Map<Double, Integer> prices = items.get(name);
            int totalCount = 0; //to count the total amount of times seen of named product
            for (Integer count : prices.values()) {
                totalCount += count;
            }

            System.out.printf("name: %-8s \t\t seen: %d times\n", name, totalCount);
            System.out.println("============= \t\t =============");

            for (Map.Entry<Double, Integer> priceEntry : prices.entrySet()) {
                double price = priceEntry.getKey();
                int count = priceEntry.getValue();
                System.out.printf("Price: \t %.2f \t\t seen: %d %s\n", price, count, count > 1 ? "times" : "time");
                System.out.println("------------- \t\t -------------");
            }
            System.out.println();
        }

        System.out.println("Errors \t\t\t\t seen: " + errorCounter + " times");
    }
}

