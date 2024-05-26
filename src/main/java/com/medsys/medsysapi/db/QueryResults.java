package com.medsys.medsysapi.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResults {
    private List<Map<String, Object>> results;

    QueryResults() {
        this.results = new ArrayList<>();
    }

    public void getFromList(List<Map<String, Object>> r) {
        this.results = r;
    }

    public void getFromResultSet(ResultSet rs) throws SQLException {

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            results.add(row);
        }
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
