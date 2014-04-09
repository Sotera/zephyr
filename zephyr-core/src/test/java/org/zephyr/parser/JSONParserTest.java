package org.zephyr.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;

public class JSONParserTest {

    @Test
    public void test1() throws IOException {
    	JSONParserFactory factory = new JSONParserFactory("customers");
        Parser parser = factory.newParser(Thread.currentThread().getContextClassLoader().getResourceAsStream("json-parser-test1.json"));
        ProcessingResult<List<Pair>, byte[]> result = null;
        List<List<Pair>> goodResults = new ArrayList<List<Pair>>();
        while ((result = parser.parse()) != null) {
            if (result.wasProcessedSuccessfully()) {
                goodResults.add(result.getProcessedData());
            }
        }
        assertEquals(3, goodResults.size());
        assertEquals("date", goodResults.get(0).get(0).getKey());
        assertEquals("2012-03-04", goodResults.get(0).get(0).getValue());
        assertEquals("gate", goodResults.get(0).get(1).getKey());
        assertEquals("C", goodResults.get(0).get(1).getValue());
        assertEquals("customers.first_name", goodResults.get(0).get(2).getKey());
        assertEquals("vlad", goodResults.get(0).get(2).getValue());
        assertEquals("customers.last_name", goodResults.get(0).get(3).getKey());
        assertEquals("tepes", goodResults.get(0).get(3).getValue());
        assertEquals("customers.country_of_origin", goodResults.get(0).get(4).getKey());
        assertEquals("Romania", goodResults.get(0).get(4).getValue());
        
        assertEquals("date", goodResults.get(1).get(0).getKey());
        assertEquals("2012-03-04", goodResults.get(1).get(0).getValue());
        assertEquals("gate", goodResults.get(1).get(1).getKey());
        assertEquals("C", goodResults.get(1).get(1).getValue());
        assertEquals("customers.first_name", goodResults.get(1).get(2).getKey());
        assertEquals("anonymous", goodResults.get(1).get(2).getValue());
        assertEquals("customers.last_name", goodResults.get(1).get(3).getKey());
        assertEquals("frankenstein", goodResults.get(1).get(3).getValue());
        assertEquals("customers.country_of_origin", goodResults.get(1).get(4).getKey());
        assertEquals("Germany", goodResults.get(1).get(4).getValue());
        
        assertEquals("date", goodResults.get(2).get(0).getKey());
        assertEquals("2012-03-04", goodResults.get(2).get(0).getValue());
        assertEquals("gate", goodResults.get(2).get(1).getKey());
        assertEquals("C", goodResults.get(2).get(1).getValue());
        assertEquals("customers.first_name", goodResults.get(2).get(2).getKey());
        assertEquals("davros", goodResults.get(2).get(2).getValue());
        assertEquals("customers.country_of_origin", goodResults.get(2).get(3).getKey());
        assertEquals("Skaro", goodResults.get(2).get(3).getValue());

    }

}
