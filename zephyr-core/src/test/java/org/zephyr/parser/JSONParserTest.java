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
    /**
     * Test a good JSON file
     * @throws IOException
     */
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
    
    @Test
    /**
     * Test a null field in the JSON input.
     * The test should still run through and pass.
     * @throws IOException
     */
    public void test2() throws IOException {
    	JSONParserFactory factory = new JSONParserFactory("customers");
        Parser parser = factory.newParser(Thread.currentThread().getContextClassLoader().getResourceAsStream("json-parser-test2.json"));
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
    
    @Test(expected=IOException.class)
    /**
     * Test on a null stream.
     * Should throw an Exception.
     * @throws IOException
     */
    public void test3() throws IOException {
    	JSONParserFactory factory = new JSONParserFactory("goingtofail");
        Parser parser = factory.newParser(null);
        ProcessingResult<List<Pair>, byte[]> result = null;
        List<List<Pair>> goodResults = new ArrayList<List<Pair>>();
        while ((result = parser.parse()) != null) {
            if (result.wasProcessedSuccessfully()) {
                goodResults.add(result.getProcessedData());
            }
        }
    }
    
    @Test
    /**
     * Test on a bad repeatingQName
     * Should return a goodResults of Size 0.
     * @throws IOException
     */
    public void test4() throws IOException {
    	JSONParserFactory factory = new JSONParserFactory("foo");
        Parser parser = factory.newParser(Thread.currentThread().getContextClassLoader().getResourceAsStream("json-parser-test1.json"));
        ProcessingResult<List<Pair>, byte[]> result = null;
        List<List<Pair>> goodResults = new ArrayList<List<Pair>>();
        while ((result = parser.parse()) != null) {
            if (result.wasProcessedSuccessfully()) {
                goodResults.add(result.getProcessedData());
            }
        }
        assertEquals(0, goodResults.size());
    }
    
    @Test
    /**
     * Test an empty JSON file.
     */
    public void test5() throws IOException{
    	JSONParserFactory factory = new JSONParserFactory("test");
        Parser parser = factory.newParser(Thread.currentThread().getContextClassLoader().getResourceAsStream("json-parser-test5.json"));
        ProcessingResult<List<Pair>, byte[]> result = null;
        List<List<Pair>> goodResults = new ArrayList<List<Pair>>();
        while ((result = parser.parse()) != null) {
            if (result.wasProcessedSuccessfully()) {
                goodResults.add(result.getProcessedData());
            }
        }
        assertEquals(0, goodResults.size());
    }
    @Test
    /**
     * Test a JSON file where repeating is the root.
     */
    public void test6() throws IOException{
    	JSONParserFactory factory = new JSONParserFactory("customers");
        Parser parser = factory.newParser(Thread.currentThread().getContextClassLoader().getResourceAsStream("json-parser-test6.json"));
        ProcessingResult<List<Pair>, byte[]> result = null;
        List<List<Pair>> goodResults = new ArrayList<List<Pair>>();
        while ((result = parser.parse()) != null) {
            if (result.wasProcessedSuccessfully()) {
                goodResults.add(result.getProcessedData());
            }
        }
        
        assertEquals("customers.first_name", goodResults.get(0).get(0).getKey());
        assertEquals("vlad", goodResults.get(0).get(0).getValue());
        assertEquals("customers.last_name", goodResults.get(0).get(1).getKey());
        assertEquals("tepes", goodResults.get(0).get(1).getValue());
        assertEquals("customers.country_of_origin", goodResults.get(0).get(2).getKey());
        assertEquals("Romania", goodResults.get(0).get(2).getValue());
        
        assertEquals("customers.first_name", goodResults.get(1).get(0).getKey());
        assertEquals("anonymous", goodResults.get(1).get(0).getValue());
        assertEquals("customers.last_name", goodResults.get(1).get(1).getKey());
        assertEquals("frankenstein", goodResults.get(1).get(1).getValue());
        assertEquals("customers.country_of_origin", goodResults.get(1).get(2).getKey());
        assertEquals("Germany", goodResults.get(1).get(2).getValue());
        
        assertEquals("customers.first_name", goodResults.get(2).get(0).getKey());
        assertEquals("davros", goodResults.get(2).get(0).getValue());
        assertEquals("customers.country_of_origin", goodResults.get(2).get(1).getKey());
        assertEquals("Skaro", goodResults.get(2).get(1).getValue());

    }

}
