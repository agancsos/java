package com.rest2.models;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Rest2Repository extends HashMap<Long, Rest2Item> {
    private long maxID = -1;

    public Rest2Repository() {
        super();
        this.maxID = 1000;
    }

    public void save(Rest2Item a) {
        this.maxID++;
        this.put(this.maxID, a);
    }

    public List<Rest2Item> findAll() {
        List<Rest2Item> result = new ArrayList<Rest2Item>();
        for (Object e : Arrays.asList(this.values().toArray())) {
            result.add((Rest2Item)e);
        }
        return result;
    }

	public Rest2Item findById(Long id) {
		if (this.containsKey(id)) {
			return this.get(id);
		}
		return null;
	}
}
