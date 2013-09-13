package org.zephyr.schema.normalizer;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

public class DateFormatNormalizerTest {

    private Normalizer dateNormalizer;

    @Before
    public void setup() {
        dateNormalizer = new DateFormatNormalizer("HH:mm:ss' on 'MM/dd/yy", DateFormatNormalizer.ISO_8601_FORMAT);
    }

    @Test
    public void normalizeValidSystemTimezone() throws NormalizationException {
        /* taken from http://stackoverflow.com/questions/308683/how-can-i-get-the-current-date-and-time-in-utc-or-gmt-in-java
		 * this will tell you how many hours your current time zone is off from GMT.
		 * Quick googling didn't help me find a better way to figure out that difference.
		 */
        int offset = new GregorianCalendar().get(Calendar.ZONE_OFFSET);
        int offsetHrs = offset / 1000 / 60 / 60;

        int gmtHours = 20;
        int localHours = gmtHours + offsetHrs;


        String gmtBeginningDateTime = "2013-02-24T";
        String gmtEndDateTime = ":13:44.000Z";

        String localEndDateTime = ":13:44 on 02/24/13";


        assertEquals(gmtBeginningDateTime + gmtHours + gmtEndDateTime, this.dateNormalizer.normalize(localHours + localEndDateTime));
    }

    @Test
    public void normalizeTwoTimzones() throws NormalizationException {
        String pstTime = "12:13:44 on 02/24/13";
        String estTime = "15:13:44 on 02/24/13";
        String gmtTime = "2013-02-24T20:13:44.000Z";

        DateFormatNormalizer pstDateNormalizer = new DateFormatNormalizer("HH:mm:ss' on 'MM/dd/yy", DateFormatNormalizer.ISO_8601_FORMAT);
        pstDateNormalizer.setTimeZone("GMT-08:00");
        DateFormatNormalizer estDateNormalizer = new DateFormatNormalizer("HH:mm:ss' on 'MM/dd/yy", DateFormatNormalizer.ISO_8601_FORMAT);
        estDateNormalizer.setTimeZone("GMT-05:00");

        assertEquals(gmtTime, pstDateNormalizer.normalize(pstTime));
        assertEquals(gmtTime, estDateNormalizer.normalize(estTime));

    }
	
	/*
	  @Test
	 
	public void normalizeWithoutTimezone() throws NormalizationException {
		DateFormatNormalizer dateNormalizer = new DateFormatNormalizer("HH:mm:ss' on 'MM/dd/yy", DateFormatNormalizer.ISO_8601_FORMAT);
		assertEquals("2013-02-24T20:13:44.000Z", dateNormalizer.normalize("20:13:44 on 02/24/13"));
		
	}
	*/

    @Test
    public void normalizeIgnoringTimezone() throws NormalizationException {
        DateFormatNormalizer normalizer = new DateFormatNormalizer("HH:mm:ss Z' on 'MM/dd/yy", DateFormatNormalizer.ISO_8601_FORMAT);

        assertEquals("2013-02-24T20:13:44.000Z", normalizer.normalize("15:13:44 -0500 on 02/24/13"));
        assertEquals("2013-02-24T20:13:44.000Z", normalizer.normalize("16:13:44 -0400 on 02/24/13"));
        assertEquals("2013-02-24T20:13:44.000Z", normalizer.normalize("12:13:44 -0800 on 02/24/13"));
    }


    @Test(expected = NormalizationException.class)
    public void normalizeInvalidIncomingFormat() throws NormalizationException {
        this.dateNormalizer.normalize("12:13:44 on 24/2/13");
    }

    @Test(expected = NormalizationException.class)
    public void provideInvalidFormatOnConstruction() throws NormalizationException {
        Normalizer testNormalizer = new DateFormatNormalizer("kk:mm:ssq bad data shouldn't work here", DateFormatNormalizer.ISO_8601_FORMAT);
        testNormalizer.normalize("This isn't even close");
    }

    @Test
    public void testTwitterFormat() throws NormalizationException {
        Normalizer normalizer = new DateFormatNormalizer("EEE MMM dd kk:mm:ss zzz yyyy", DateFormatNormalizer.ISO_8601_FORMAT);
        assertEquals("2012-09-27T03:28:05.000Z", normalizer.normalize("Wed Sep 26 23:28:05 EDT 2012"));
    }

    @Test
    public void testFlickrFormat() throws NormalizationException {
        DateFormatNormalizer normalizer = new DateFormatNormalizer("yyyy-MM-dd HH:mm:ss", DateFormatNormalizer.ISO_8601_FORMAT);
        normalizer.setTimeZone("GMT");
        try {
            assertEquals("2008-03-09T02:03:44.000Z", normalizer.normalize("2008-03-09 02:03:44"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
