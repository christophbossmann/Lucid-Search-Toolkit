package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResult;
import org.apache.lucene.index.IndexableField;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MSearchResult {

    @JsonIgnore
    SearchResult searchResult;

    private final int searchproviderid;
    private final String searchprovideridentifier;
    private final float score;
    private final List<Field> fields;

    public MSearchResult(SearchResult searchResult, int searchproviderid, String searchprovideridentifier) {
        this.searchproviderid = searchproviderid;
        this.searchprovideridentifier = searchprovideridentifier;
        this.searchResult = searchResult;
        this.score = searchResult.getScoreDoc().score;
        fields = new ArrayList<>();
        for(IndexableField indexableField : searchResult.getDocument().getFields()) {
            fields.add(new Field(indexableField));
        }
    }
    public int getSearchproviderid() {
        return searchproviderid;
    }

    public String getSearchprovideridentifier() {
        return searchprovideridentifier;
    }
    public String getModified() {
        String modified = null;
        for(Field f : getFields()) {
            if(f.name.equals("modified")) {
                modified = f.value;
                break;
            }
        }
        if(modified == null) {
            throw new RuntimeException("modified value empty!");
        }
        Date d = new Date(Long.parseLong(modified));
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(d);
    }
    public float getScore() {
        return score;
    }
    public List<Field> getFields() {
        return fields;
    }

    public static class Field {
        private final String name;
        private final String value;
        public Field(IndexableField indexableField) {
            name = indexableField.name();
            if(name.equals("contents") && indexableField.stringValue().length() > 150) {
                value = indexableField.stringValue().substring(0, 150) + " ...";
            }
            else {
                value = indexableField.stringValue();
            }

        }
        public String getName() {
            return name;
        }
        public String getValue() {
            return value;
        }
    }

    @Override
    public String toString() {
        return "MSearchResult{" +
                "searchproviderid=" + searchproviderid +
                ", searchprovideridentifier='" + searchprovideridentifier + '\'' +
                ", score=" + score +
                ", fields=" + fields +
                '}';
    }
}
