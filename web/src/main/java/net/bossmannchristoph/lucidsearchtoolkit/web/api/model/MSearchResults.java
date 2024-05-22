package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class MSearchResults {

    @JsonProperty("SearchResults")
    private final List<MSearchResult> sResults;
    public MSearchResults() {
        sResults = new ArrayList<>();
    }

    public void addSearchResults(List<SearchResult> searchResults, int searchproviderid) {
        for(SearchResult searchResult : searchResults) {
            sResults.add(new MSearchResult(searchResult, searchproviderid));
        }
    }

    public List<MSearchResult> getsResults() {
        return sResults;
    }
}
