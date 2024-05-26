package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MSearchResults {

    @JsonProperty("SearchResults")
    private List<MSearchResult> sResults;
    private String usedquery;
    public MSearchResults() {
        sResults = new ArrayList<>();
    }

    public void addSearchResults(List<SearchResult> searchResults, int searchproviderid, String searchprovideridentifier) {
        for(SearchResult searchResult : searchResults) {
            sResults.add(new MSearchResult(searchResult, searchproviderid, searchprovideridentifier));
        }
    }

    public void limit(int maxSize) {
        sResults = sResults.stream().limit(maxSize).collect(Collectors.toList());
    }

    public List<MSearchResult> getsResults() {
        return sResults;
    }

    public String getUsedquery() {
        return usedquery;
    }

    public void setUsedquery(String usedquery) {
        this.usedquery = usedquery;
    }

    @Override
    public String toString() {
        return "MSearchResults{" +
                "sResults=" + sResults +
                '}';
    }
}
