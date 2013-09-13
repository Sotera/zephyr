package org.zephyr.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;

/**
 * This DepthFirstXMLParser uses a STAX streaming implementation to parse the provided InputStream.
 * If there is only one record in the provided InputStream, the repeatingQName will be the root node of the
 * XML document.  If an XML document has elements (and attributes) that exist higher up in the tree, they will be returned
 * with each record parsed.
 */
public class DepthFirstXMLParser extends Parser {

    private String repeatingQName;
    private XMLStreamReader reader;
    private Exception exceptionCreatingStreamReader;
    private Stack<String> nameStack;
    private List<Pair> pairsArchetype;

    public DepthFirstXMLParser(String repeatingQName, InputStream inputStream) {
        this.repeatingQName = repeatingQName;
        nameStack = new Stack<String>();
        this.pairsArchetype = new ArrayList<Pair>();
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        try {
            this.reader = factory.createXMLStreamReader(inputStream);
        } catch (Exception e) {
            this.exceptionCreatingStreamReader = e;
            this.reader = null;
        }
    }

    /**
     * The parse method will parse the XML document, flattening it, and turning the path down to the QName
     */
    @Override
    public ProcessingResult<List<Pair>, byte[]> parse() throws IOException {
        // Kind of a hack putting this in here like this, but our API doesn't allow for exceptions to be thrown when creating our Parsers - possibly a TODO?
        if (this.reader == null)
            throw new IOException("There was an exception thrown in reading the XML", this.exceptionCreatingStreamReader);

        boolean atRepeat = false;
        List<Pair> pairs = new ArrayList<Pair>();

        try {
            while (this.reader.hasNext()) {
                this.reader.next();
                if (this.reader.isStartElement()) {
                    if (this.reader.getLocalName().equals(repeatingQName)) {
                        atRepeat = true;
                        if (pairsArchetype.size() > 0) {
                            pairs.addAll(pairsArchetype);
                        }
                    }
                    nameStack.push(this.reader.getLocalName());
                    if (this.reader.getAttributeCount() > 0) {
                        List<Pair> currentPairsToWriteTo = null;
                        if (atRepeat) {
                            currentPairsToWriteTo = pairs;
                        } else {
                            currentPairsToWriteTo = pairsArchetype;
                        }
                        for (int i = 0; i < this.reader.getAttributeCount(); i++) {
                            currentPairsToWriteTo.add(new Pair(printStackName(nameStack) + "." + this.reader.getAttributeLocalName(i), this.reader.getAttributeValue(i)));
                        }
                    }
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
                    if (this.reader.getLocalName().equals(repeatingQName)) {
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
            throw new IOException("There was an IO Exception when parsing the XML", e);
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
