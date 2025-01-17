import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PatternMatchingTest {
    private Main main = new Main();
    String rawData;
    PatternMatching pm;
    String rawDataFormatted = "naMe:Milk:price:3.23:type:Food:expiration:1/25/2016##naME:BreaD:price:1.23:type:Food:expiration:1/02/2016##NAMe:BrEAD:price:1.23:type:Food:expiration:2/25/2016##naMe:MiLK:price:3.23:type:Food:expiration:1/11/2016##naMe:Cookies:price:2.25:type:Food:expiration:1/25/2016##naMe:CoOkieS:price:2.25:type:Food:expiration:1/25/2016##naMe:COokIes:price:2.25:type:Food:expiration:3/22/2016##naMe:COOkieS:price:2.25:type:Food:expiration:1/25/2016##NAME:MilK:price:3.23:type:Food:expiration:1/17/2016##naMe:MilK:price:1.23:type:Food!expiration:4/25/2016##naMe:apPles:price:0.25:type:Food:expiration:1/23/2016##naMe:apPles:price:0.23:type:Food:expiration:5/02/2016##NAMe:BrEAD:price:1.23:type:Food:expiration:1/25/2016##naMe::price:3.23:type:Food:expiration:1/04/2016##naMe:Milk:price:3.23:type:Food:expiration:1/25/2016##naME:BreaD:price:1.23:type:Food:expiration:1/02/2016##NAMe:BrEAD:price:1.23:type:Food:expiration:2/25/2016##naMe:MiLK:priCe::type:Food:expiration:1/11/2016##naMe:Cookies:price:2.25:type:Food:expiration:1/25/2016##naMe:Co0kieS:pRice:2.25:type:Food:expiration:1/25/2016##naMe:COokIes:price:2.25:type:Food:expiration:3/22/2016##naMe:COOkieS:Price:2.25:type:Food:expiration:1/25/2016##NAME:MilK:price:3.23:type:Food:expiration:1/17/2016##naMe:MilK:priCe::type:Food:expiration:4/25/2016##naMe:apPles:prIce:0.25:type:Food:expiration:1/23/2016##naMe:apPles:pRice:0.23:type:Food:expiration:5/02/2016##NAMe:BrEAD:price:1.23:type:Food:expiration:1/25/2016##naMe::price:3.23:type:Food:expiration:1/04/2016##";

    @Before
    public void setup() throws Exception {
        rawData = main.readRawDataToString();
        pm = new PatternMatching(rawData);
    }

    @Test
    public void splitTest() {
        List<String> given = pm.splitEntries();

        int expected = 28;

        Assert.assertEquals(given.size(), expected);
    }

    @Test
    public void process() {
        String given = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##";
        pm = new PatternMatching(given);
        Map<String, Map<Double, Integer>> testing = pm.processEntries(pm.splitEntries());

        String expected = "Milk";

        Assert.assertTrue(testing.containsKey(expected));
    }

    @Test
    public void error() {
        int expected = 4;

        pm.processEntries(pm.splitEntries());
        int actual = pm.getErrorCounter();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void printTest() {
        List<String> entries = pm.splitEntries();
        pm.processEntries(entries);


        String expected = "name:    Milk \t\t seen: 6 times\n" +
                "============= \t\t =============\n" +
                "Price: \t 3.23 \t\t seen: 5 times\n" +
                "------------- \t\t -------------\n" +
                "Price: \t 1.23 \t\t seen: 1 time\n" +
                "------------- \t\t -------------\n" +
                "\n" +
                "name:   Bread \t\t seen: 6 times\n" +
                "============= \t\t =============\n" +
                "Price: \t 1.23 \t\t seen: 6 times\n" +
                "------------- \t\t -------------\n" +
                "\n" +
                "name: Cookies \t\t seen: 8 times\n" +
                "============= \t\t =============\n" +
                "Price: \t 2.25 \t\t seen: 8 times\n" +
                "------------- \t\t -------------\n" +
                "\n" +
                "name:  Apples \t\t seen: 4 times\n" +
                "============= \t\t =============\n" +
                "Price: \t 0.25 \t\t seen: 2 times\n" +
                "------------- \t\t -------------\n" +
                "Price: \t 0.23 \t\t seen: 2 times\n" +
                "------------- \t\t -------------\n" +
                "\n" +
                "Errors \t\t\t\t seen: 4 times\n";

        Assert.assertEquals(expected, pm.printResults());
    }
}