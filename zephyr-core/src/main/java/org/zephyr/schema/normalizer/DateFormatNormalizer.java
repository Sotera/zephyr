/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.zephyr.schema.normalizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatNormalizer implements Normalizer {

    private final String incomingFormat;
    private final String outgoingFormat;
    private TimeZone timezone;

    private Locale incomingLocale;
    private Locale outgoingLocale;

    public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * If no timezone information is in <code>incomingFormat</code> than the systems default TimeZone will be used.
     *
     * @param incomingFormat
     * @param outgoingFormat
     */
    public DateFormatNormalizer(String incomingFormat, String outgoingFormat) {
        this.incomingFormat = incomingFormat;
        this.outgoingFormat = outgoingFormat;
    }

    public void setTimeZone(String timeZoneCode) {
        this.timezone = TimeZone.getTimeZone(timeZoneCode);
    }


    @Override
    public String normalize(String value) throws NormalizationException {
        try {
            DateFormat in = getLocaleDateFormat(incomingFormat, incomingLocale);

            if (this.timezone != null)
                in.setTimeZone(this.timezone);
            in.setLenient(false);
            Date date = in.parse(value);
            DateFormat out = getLocaleDateFormat(outgoingFormat, outgoingLocale);
            out.setTimeZone(TimeZone.getTimeZone("GMT"));
            return out.format(date);
        } catch (Throwable t) {
            throw new NormalizationException(t);
        }
    }

    /**
     * Returns the DateFormat
     *
     * @param incomingFormat
     * @param locale
     * @return
     */
    private DateFormat getLocaleDateFormat(String incomingFormat, Locale locale) {
        return (locale != null) ? new SimpleDateFormat(incomingFormat, locale) : new SimpleDateFormat(incomingFormat);
    }


    public void setIncomingLocale(Locale incomingLocale) {
        this.incomingLocale = incomingLocale;
    }

    public void setOutgoingLocale(Locale outgoingLocale) {
        this.outgoingLocale = outgoingLocale;
    }

    public String getIncomingFormat() {
        return incomingFormat;
    }

    public String getOutgoingFormat() {
        return outgoingFormat;
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public Locale getIncomingLocale() {
        return incomingLocale;
    }

    public Locale getOutgoingLocale() {
        return outgoingLocale;
    }


}
