/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.zephyr.parser;

import de.odysseus.staxon.json.JsonXMLInputFactory;

import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;

import javax.xml.stream.XMLStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The DepthFirstJSONParser will parse a JSON file using STAXON, which is based off of STAX streaming.
 * This works the same as the XML parser, but with a JSON file, so the same rules apply.  
 * If there are tags above the repeatingObjectName, they will be returned with each repeating value.
 */
public class DepthFirstJSONParser extends Parser{

    private String repeatingObjectName;
    private XMLStreamReader reader;
    private Exception exceptionCreatingStreamReader;
    private Stack<String> nameStack;
    private List<Pair> pairsArchetype;

    public DepthFirstJSONParser(String repeatingObjectName, InputStream inputStream){
        this.repeatingObjectName = repeatingObjectName;
        nameStack = new Stack<String>();
        this.pairsArchetype = new ArrayList<Pair>();
        JsonXMLInputFactory factory = new JsonXMLInputFactory();
        try {
            this.reader = factory.createXMLStreamReader(inputStream);
        } catch (Exception e) {
            this.exceptionCreatingStreamReader = e;
            this.reader = null;
        }
    }
    @Override
    public ProcessingResult<List<Pair>, byte[]> parse() throws IOException {
        if (this.reader == null)
            throw new IOException("There was an exception thrown in reading the XML", this.exceptionCreatingStreamReader);

        boolean atRepeat = false;
        List<Pair> pairs = new ArrayList<Pair>();

        try {
            while (this.reader.hasNext()) {
            	this.reader.next();
                if (this.reader.isStartElement()) {
                    if (this.reader.getLocalName().equals(repeatingObjectName)) {
                        atRepeat = true;
                        if (pairsArchetype.size() > 0) {
                            pairs.addAll(pairsArchetype);
                        }
                    }
                    nameStack.push(this.reader.getLocalName());
                } else if (this.reader.hasText() && !this.reader.isWhiteSpace()) {
                    List<Pair> currentPairsToWriteTo = null;
                    if (atRepeat) {
                        currentPairsToWriteTo = pairs;
                    } else {
                        currentPairsToWriteTo = pairsArchetype;
                    }
                    currentPairsToWriteTo.add(new Pair(printStackName(nameStack), this.reader.getText()));
                } else if (this.reader.isEndElement()) {
                    nameStack.pop();
                    if (this.reader.getLocalName().equals(repeatingObjectName)) {
                        // If we have a sourceElementIdentifier, such as a filename specified, we shall return it.
                        if (getSourceElementIdentifier() != null) {
                            pairs.add(getSourceElementPair());
                        }
                        // finished a record, let's return our pairs
                        return new ProcessingResult<List<Pair>, byte[]>(pairs);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new IOException("There was an IO Exception when parsing the JSON", e);
        }
    }

    private String printStackName(Stack<String> stack) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stack.size(); i++) {
            builder.append(stack.get(i));
            builder.append(".");
        }
        return builder.substring(0, builder.length() - 1);
    }
}
