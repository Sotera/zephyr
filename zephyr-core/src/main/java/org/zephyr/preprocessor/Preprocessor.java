package org.zephyr.preprocessor;

import java.io.InputStream;

public interface Preprocessor {

    public byte[] process(InputStream inputStream);

}
