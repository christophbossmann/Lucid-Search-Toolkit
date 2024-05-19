package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResult;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResults;

import java.util.ArrayList;
import java.util.List;

public class SResults {

    @JsonProperty("SearchResults")
    private final List<SResult> sResults;
    public SResults(SearchResults searchResults) {
        sResults = new ArrayList<>();
        for(SearchResult searchResult : searchResults.getSearchResults()) {
            sResults.add(new SResult(searchResult));
        }
    }

    public List<SResult> getsResults() {
        return sResults;
    }
}
