package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResult;
import org.apache.lucene.index.IndexableField;

import java.util.ArrayList;
import java.util.List;

public class MSearchResult {

    @JsonIgnore
    SearchResult searchResult;

    private final int searchproviderid;
    private final float score;
    private final List<Field> fields;

    public MSearchResult(SearchResult searchResult, int searchproviderid) {
        this.searchproviderid = searchproviderid;
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
}
