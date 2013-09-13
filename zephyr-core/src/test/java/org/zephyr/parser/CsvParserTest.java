package org.zephyr.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;

import com.google.common.collect.Lists;

public class CsvParserTest {

    private List<String> headers = Lists.newArrayList("header1", "header2", "header3", "header4");

    private List<Pair> expectedNormal = Lists.newArrayList(new Pair("header1", "field1"), new Pair("header2", "field2"), new Pair("header3", "field3"), new Pair("header4", "field4"));
    private List<Pair> expectedComma = Lists.newArrayList(new Pair("header1", "field,1"), new Pair("header2", "field,2"), new Pair("header3", "field,3"), new Pair("header4", "field,4"));

    @Test
    public void testGoodRows() throws IOException {
        String goodRows = "field1,field2,field3,field4\nfield1,field2,field3,field4";
        CsvParserFactory factory = new CsvParserFactory(headers);

        Parser parser = factory.newParser(new ByteArrayInputStream(goodRows.getBytes()));
        ProcessingResult<List<Pair>, byte[]> result1 = parser.parse();
        ProcessingResult<List<Pair>, byte[]> result2 = parser.parse();
        assertTrue(result1.wasProcessedSuccessfully());
        assertTrue(result2.wasProcessedSuccessfully());
        assertResultsTrue(expectedNormal, result1.getProcessedData());
        assertResultsTrue(expectedNormal, result2.getProcessedData());

    }

    @Test
    public void testSpacesBetweenFields() throws IOException {
        String spacesBetweenFields = " field1      , field2, field3, field4\n  field1, field2, field3, field4";
        CsvParserFactory factory = new CsvParserFactory(headers);
        factory.setTrimWhitespace(true);

        Parser parser = factory.newParser(new ByteArrayInputStream(spacesBetweenFields.getBytes()));
        ProcessingResult<List<Pair>, byte[]> result1 = parser.parse();
        ProcessingResult<List<Pair>, byte[]> result2 = parser.parse();
        assertTrue(result1.wasProcessedSuccessfully());
        assertTrue(result2.wasProcessedSuccessfully());
        assertResultsTrue(expectedNormal, result1.getProcessedData());
        assertResultsTrue(expectedNormal, result2.getProcessedData());

    }

    @Test
    public void testQuotes() throws IOException {
        String usesQuotes = "\"field,1\",\"field,2\",\"field,3\",\"field,4\"\n\"field1\",\"field2\",\"field3\",\"field4\"";
        CsvParserFactory factory = new CsvParserFactory(headers);

        Parser parser = factory.newParser(new ByteArrayInputStream(usesQuotes.getBytes()));
        ProcessingResult<List<Pair>, byte[]> result1 = parser.parse();
        ProcessingResult<List<Pair>, byte[]> result2 = parser.parse();
        assertTrue(result1.wasProcessedSuccessfully());
        assertTrue(result2.wasProcessedSuccessfully());
        assertResultsTrue(expectedComma, result1.getProcessedData());
        assertResultsTrue(expectedNormal, result2.getProcessedData());

    }

    @Test
    public void testDifferentQuotes() throws IOException {
        String usesDifferentQuotes = "|field1|,|field2|,|field3|,|field4|\n|field1|,|field2|,|field3|,|field4|";
        CsvParserFactory factory = new CsvParserFactory(headers);
        factory.setQuoteChar('|');

        Parser parser = factory.newParser(new ByteArrayInputStream(usesDifferentQuotes.getBytes()));
        ProcessingResult<List<Pair>, byte[]> result1 = parser.parse();
        ProcessingResult<List<Pair>, byte[]> result2 = parser.parse();
        assertTrue(result1.wasProcessedSuccessfully());
        assertTrue(result2.wasProcessedSuccessfully());
        assertResultsTrue(expectedNormal, result1.getProcessedData());
        assertResultsTrue(expectedNormal, result2.getProcessedData());

    }

    @Test
    public void testDifferentSeparator() throws IOException {
        String usesTabs = "field1\tfield2\tfield3\tfield4\nfield1\tfield2\tfield3\tfield4";
        CsvParserFactory factory = new CsvParserFactory(headers);
        factory.setSeparator('\t');

        Parser parser = factory.newParser(new ByteArrayInputStream(usesTabs.getBytes()));
        ProcessingResult<List<Pair>, byte[]> result1 = parser.parse();
        ProcessingResult<List<Pair>, byte[]> result2 = parser.parse();
        assertTrue(result1.wasProcessedSuccessfully());
        assertTrue(result2.wasProcessedSuccessfully());
        assertResultsTrue(expectedNormal, result1.getProcessedData());
        assertResultsTrue(expectedNormal, result2.getProcessedData());

    }

    @Test
    public void testBadData() throws IOException {
        String badData = "\"thisisthefield,thatgoeson,forever,butthere'ssupposed\",tobe4\n";
        CsvParserFactory factory = new CsvParserFactory(headers);

        Parser parser = factory.newParser(new ByteArrayInputStream(badData.getBytes()));
        ProcessingResult<List<Pair>, byte[]> result = parser.parse();
        assertTrue(!result.wasProcessedSuccessfully());
        result.getError().printStackTrace();

    }

    private void assertResultsTrue(List<Pair> expected, List<Pair> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getKey(), actual.get(i).getKey());
            assertEquals(expected.get(i).getValue(), actual.get(i).getValue());
        }
    }

}
