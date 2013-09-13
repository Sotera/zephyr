package org.zephyr.schema.scheme;

import java.util.Collections;
import java.util.List;

import org.zephyr.data.Entry;
import org.zephyr.data.Pair;
import org.zephyr.schema.normalizer.NormalizationException;
import org.zephyr.schema.normalizer.Normalizer;
import org.zephyr.schema.validator.NonEmptyValidator;
import org.zephyr.schema.validator.Validator;
import org.zephyr.service.CatalogService;

/**
 * A scheme defines a mapping between the raw field name to the outgoing entry label, the visibility string to utilize, metadata, and what type(s) that should be applied to the
 * scheme for typing in later enrichers or outputters
 */
public class Scheme {

    private static final Validator NON_EMPTY_VALIDATOR = new NonEmptyValidator();

    private final String rawFieldName;
    /**
     * The label we want to use for this entry
     */
    private final String label;

    /**
     * The security label to use in the creation of each Entry, this is ultimately written to Accumulo/Cloudbase
     */
    private String visibility;
    /**
     * Metadata is a field we use to allow us to group similar terms with a string flag - primary locations, secondary locations - things of that nature, that may only make sense on a feed by feed basis
     */
    private String metadata;
    /**
     * The validator to use *before* the data is normalized
     */
    private Validator preNormalizationValidator;
    /**
     * The validator to use *after* the data is normalized - ideally you wouldn't need this step, but we have seen time and time again that Normalizers can have bugs that produce faulty data and we don't catch it
     */
    private Validator postNormalizationValidator;
    /**
     * The ordered list of normalizers to operate over the provided value
     */
    private List<Normalizer> normalizers;
    /**
     * The ordered list of types that we declare this field to be
     */
    private List<String> types;

    /**
     * Constructor that requires the raw field name and the label, sets both validators as NON_EMPTY_VALIDATORs, and sets the Normalizer and Types lists to empty (and immutable) lists.
     *
     * @param label
     * @param visibility
     * @param metadata
     * @param preNormalizationValidator
     * @param postNormalizationValidator
     * @param normalizers
     */
    public Scheme(final String rawFieldName, final String label) {
        this.rawFieldName = rawFieldName;
        this.label = label;
        this.metadata = "";
        this.visibility = "";
        this.preNormalizationValidator = NON_EMPTY_VALIDATOR;
        this.postNormalizationValidator = NON_EMPTY_VALIDATOR;
        this.normalizers = Collections.emptyList();
        this.types = Collections.emptyList();
    }

    public void setVisibility(final String visibility) {
        this.visibility = visibility;
    }

    public void setMetadata(final String metadata) {
        this.metadata = metadata;
    }

    public void setPreNormalizationValidator(Validator preNormalizationValidator) {
        this.preNormalizationValidator = preNormalizationValidator;
    }

    public void setPostNormalizationValidator(Validator postNormalizationValidator) {
        this.postNormalizationValidator = postNormalizationValidator;
    }

    public void setNormalizers(List<Normalizer> normalizers) {
        this.normalizers = normalizers;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * The map method is called by the Schema's normalizeAndValidate() method - it takes in a Pair object, validates it against the
     * preNormalizationValidator, executes all normalizers on the value (in order, as applicable), and then validates it against the postNormalizationValidator.
     * <p/>
     * If either of the Validators fails, a new SchemeException is thrown, and any exception thrown by any normalizer in the chain results in a new SchemeException being thrown.
     *
     * @param pair
     * @return
     * @throws SchemeException
     */
    public Entry map(Pair pair, CatalogService catalogService) throws SchemeException {
        if (!preNormalizationValidator.isValid(pair.getValue())) {
            throw new SchemeException("The pair " + pair + " provided failed pre-normalization validation for validator: " + preNormalizationValidator.getClass().getCanonicalName());
        }

        String value = pair.getValue();
        if (normalizers != null && normalizers.size() > 0) {
            for (Normalizer normalizer : normalizers) {
                try {
                    value = normalizer.normalize(value);
                } catch (NormalizationException e) {
                    e.printStackTrace();
                    throw new SchemeException("The pair " + pair + " provided failed normalization on normalizer: " + normalizer.getClass().getCanonicalName(), e);
                }
            }
        }

        if (!postNormalizationValidator.isValid(value)) {
            if (normalizers != null && normalizers.size() > 0) {
                StringBuilder builder = new StringBuilder();
                builder.append("[");
                for (Normalizer normalizer : normalizers) {
                    builder.append(normalizer.getClass().getCanonicalName());
                    builder.append(",");
                }
                builder.append("]");
                throw new SchemeException("The value normalized [" + value + "] failed post-normalization validation for validator: " + postNormalizationValidator.getClass().getCanonicalName() + ".  Normalizer information was, in sequence: " + builder.substring(0, builder.length() - 1));
            } else {
                throw new SchemeException("The value normalized [" + value + "] failed post-normalization validation for validator: " + postNormalizationValidator.getClass().getCanonicalName());
            }
        }

        Entry entry = catalogService.getEntry(this.label, value, this.types, this.visibility, this.metadata);
        if (entry == null) {
            throw new SchemeException("The label provided, " + this.label + ", was not found by the catalog service.");
        }
        return entry;

    }

    public String getRawFieldName() {
        return this.rawFieldName;
    }

}
