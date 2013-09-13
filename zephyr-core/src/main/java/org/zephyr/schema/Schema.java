package org.zephyr.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zephyr.data.Entry;
import org.zephyr.data.Record;
import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;
import org.zephyr.schema.scheme.Scheme;
import org.zephyr.schema.scheme.SchemeException;
import org.zephyr.service.CatalogService;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Sets;

/**
 * The Schema abstract class provides implementing classes with a contract to follow and some standard methods defined for
 * the Scheme, Normalization, and Validation of a key:value pair for each implementing class (and subsequent feed).
 * <p/>
 * The idea behind this class (and subclasses) is for the developer to generate a Schema implementation for a feed.  Our current
 * example feed is AIS, and thus we have an AISSchema.  The AISSchema has a few required fields (fields that must be there or we assume the record is invalid).
 * These required fields are retrieved by the getRequiredFields() method, which (for best performance) should be set in the constructor of the implementing Schema .
 * <p/>
 * If a field does not validate or normalize correctly from the Scheme's map() method, a debug message is logged; if the field was a required field, the entire record will ultimately fail processing
 * by throwing a SchemeException.
 */
public class Schema {

    private static final Logger logger = LoggerFactory.getLogger(Schema.class);

    private CatalogService catalogService;

    private ArrayListMultimap<String, Scheme> schemata;
    private String feedName;
    private Set<String> requiredFields;

    public Schema() {
        this.schemata = ArrayListMultimap.create();
        this.feedName = "UnnamedFeed";
        this.requiredFields = Sets.newTreeSet();
    }

	/*
	 * First Spring necessary getters/setters 
	 */

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public void setSchemata(List<Scheme> schemes) {
        for (Scheme scheme : schemes) {
            this.schemata.put(scheme.getRawFieldName(), scheme);
        }
    }

    public void setRequiredFields(Set<String> requiredFields) {
        this.requiredFields = requiredFields;
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * This method takes in a List<Pair> of orderedPairings (key:value pairs).  It creates a new Record with a new UUID, then iterates over the list of Pair items.
     * <p/>
     * If the Pair item currently operated upon can be normalized and validated as per the Scheme for that pair.getKey() successfully, it is added to the Record
     * and the pair.getKey() is added to the normalizedAndValidatedFields Set - this set keeps track of all the fields that have been successfully normalized, validated, mapped, and security labeled.
     * <p/>
     * After all Pairs have been mapped according to their scheme, we then check the normalizedAndValidatedFields Set against the requiredFields set - unless all requiredFields have been successfully operated over,
     * we throw a SchemeException.  If all have been operated over successfully, we return the Record.
     *
     * @param orderedPairings
     * @return
     * @throws SchemeException
     */
    public ProcessingResult<Record, List<Pair>> map(List<Pair> orderedPairings) {
        Set<String> normalizedAndValidatedFields = new TreeSet<String>();
        Record record = new Record();
        record.setFeedName(getFeedName());
        for (Pair pair : orderedPairings) {
            try {
                List<ProcessingResult<Entry, Pair>> results = normalizeAndValidate(pair);
                for (ProcessingResult<Entry, Pair> result : results) {
                    if (result.wasProcessedSuccessfully()) {
                        normalizedAndValidatedFields.add(pair.getKey());
                        record.add(result.getProcessedData());
                    } else {
                        logger.debug("A SchemeException error occurred when processing: {}", pair, result.getError());
                    }
                }
            } catch (SchemeException e) {
                logger.debug("A SchemeException error occurred when processing: {}", pair, e);
            }
        }

        Set<String> requiredFields = getRequiredFields();

        if (requiredFields != null) {
            if (normalizedAndValidatedFields.containsAll(requiredFields)) {
                return new ProcessingResult<Record, List<Pair>>(record);
            } else {
                Set<String> missingRequiredFields = new TreeSet<String>(requiredFields);
                missingRequiredFields.removeAll(normalizedAndValidatedFields);
                StringBuilder builder = new StringBuilder();
                for (String missing : missingRequiredFields) {
                    builder.append(missing);
                    builder.append(",");
                }
                String error = builder.substring(0, builder.length() - 1);
                return new ProcessingResult<Record, List<Pair>>(orderedPairings, new SchemeException("Of the required fields for this Schema, the following were unsuccessfully normalized or validated: " + error));
            }
        } else {
            return new ProcessingResult<Record, List<Pair>>(record);
        }

    }

    /**
     * Attempts to find a Schema for the provided Pair (by using the Pair.getKey() as the key for the member variable "mappings").
     * If one is not found, throws a SchemeException.
     * <p/>
     * If one is found, it returns the Canonicalized, Normalized, Validated, and Security Tagged Entry representation
     *
     * @param pair
     * @return
     * @throws SchemeException
     */
    public List<ProcessingResult<Entry, Pair>> normalizeAndValidate(Pair pair) throws SchemeException {
        ArrayListMultimap<String, Scheme> schemata = getSchemata();
        List<Scheme> schemesToProcess = schemata.get(pair.getKey());
        List<ProcessingResult<Entry, Pair>> results = new ArrayList<ProcessingResult<Entry, Pair>>(schemesToProcess.size());
        if (schemesToProcess != null && schemesToProcess.size() > 0) {
            for (Scheme scheme : schemesToProcess) {
                try {
                    results.add(new ProcessingResult<Entry, Pair>(scheme.map(pair, catalogService)));
                } catch (SchemeException e) {
                    results.add(new ProcessingResult<Entry, Pair>(pair, e));
                }
            }
            return results;
        } else {
            throw new SchemeException("The pair we were attempting to map, " + pair + ", was not found in the list of Schemes provided by this Schema!");
        }
    }

    /**
     * @return The feed name is a string representation of this data.  Ultimately, it can be used in many different writers in different ways; Hive table names, prepended or appended
     *         values to MapRed output folders, Accumulo cell names, etc.  This will allow us to work with only specific data types later.  This populates a field in each Record coming from the
     *         instantiating Schema called fieldName and passes it along with the data.
     */
    public String getFeedName() {
        return this.feedName;
    }

    /**
     * Returns a Set of Strings that identify the key of fields that are required to be processed for the Record to be valid
     *
     * @return
     */
    private Set<String> getRequiredFields() {
        return this.requiredFields;
    }

    /**
     * Returns an Map of Schemata, identified by the key that fields are identified by in the provided feed.
     *
     * @return
     */
    private ArrayListMultimap<String, Scheme> getSchemata() {
        return this.schemata;
    }

}
