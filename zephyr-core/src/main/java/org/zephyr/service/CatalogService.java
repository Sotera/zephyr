package org.zephyr.service;

import java.util.List;

import org.zephyr.data.Entry;

public interface CatalogService {

    Entry getEntry(String label, String value, List<String> types, String visibility, String metadata);

}
