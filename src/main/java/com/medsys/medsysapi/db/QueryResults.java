package com.medsys.medsysapi.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryResults {
    private List<Map<String, Object>> results;

    QueryResults() {
        this.results = new ArrayList<>();
    }

    public void getFromQuery(List<Map<String, Object>> r) {
        this.results = r;
    }

    public List<Map<String, Object>> getResults() {
        return this.results;
    }

    public String getResultsAsString() {
        if(results.isEmpty()) {
            return "";
        }

        String result = "";
        int listSize = results.size();
        for (Map<String, Object> map : results) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                result += entry.getValue();
            }
            listSize--;
            if(listSize > 0) {
                result += ", ";
            }
        }
        return result;
    }

    public Map<String, Object> getFirstResult() {
        if(results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public Map<String, Object> getResultsAt(int index) {
        if(results.isEmpty()) {
            return null;
        }
        return results.get(index);
    }
}
