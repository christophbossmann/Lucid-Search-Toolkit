package net.bossmannchristoph.lucidsearchtoolkit.web.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.LuceneSearcher;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResults;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.MSearchResults;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.SearchProvider;
import net.bossmannchristoph.lucidsearchtoolkit.web.exception.ApplicationException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SearchProviderService {
    @Autowired
    private ConfigService configService;

    ObjectMapper objectMapper;
    public SearchProviderService() {
        objectMapper = new ObjectMapper();
    }

    public List<SearchProvider> getSearchProviderList() {
        try {
            JsonNode configJson = configService.getConfig();
            List<SearchProvider> searchProviderList = new ArrayList<>();
            JsonNode searchprovidersNode = configJson.get("searchproviders");
            for(JsonNode node : searchprovidersNode) {
                SearchProvider sP = objectMapper.treeToValue(node, SearchProvider.class);
                searchProviderList.add(sP);
            }
            return searchProviderList;
        }
        catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<SearchProvider> getSearchProvider(Integer id) {
        Optional<SearchProvider> optional = Optional.empty();
        for(SearchProvider searchProvider : getSearchProviderList()) {
            if(Objects.equals(searchProvider.getId(), id)) {
                optional = Optional.of(searchProvider);
                break;
            }
        }
        return optional;
    }

    public MSearchResults search(int[] searchproviderids, String searchString, String searchPath, Boolean multifield, Integer numberOfResults) throws ApplicationException {
        try {
            MSearchResults mSearchResults = new MSearchResults();
            for (int searchproviderid : searchproviderids) {
                addSearchResults(mSearchResults, searchproviderid, searchString, searchPath, multifield, numberOfResults);
            }
            mSearchResults.getsResults().sort((x, y) -> Float.compare(y.getScore(), x.getScore()));
            mSearchResults.limit(numberOfResults);
            return mSearchResults;
        }
        catch(ParseException e) {
            throw new ApplicationException("Could not parse query: " + e.getMessage(), "QUERY_PARSE_NOT_SUCCESSFUL");
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MSearchResults search(Integer searchProviderId, String searchString, String searchPath, Boolean multifield, Integer numberOfResults) throws ApplicationException {
        try {
            MSearchResults mSearchResults = new MSearchResults();
            addSearchResults(mSearchResults, searchProviderId, searchString, searchPath, multifield, numberOfResults);
            mSearchResults.getsResults().sort((x, y) -> Float.compare(y.getScore(), x.getScore()));
            return mSearchResults;
        }
        catch(ParseException e) {
            throw new ApplicationException("Could not parse query: " + e.getMessage(), "QUERY_PARSE_NOT_SUCCESSFUL");
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSearchResults(MSearchResults mSearchResults, int searchproviderid, String searchString, String searchPath, Boolean multifield, Integer numberOfResults) throws ApplicationException, IOException, ParseException {
        Optional<SearchProvider> searchProviderOpt = getSearchProvider(searchproviderid);
        if(searchProviderOpt.isEmpty()) {
            throw new ApplicationException("Searchprovider with id: " + searchproviderid + " not available!", "SEARCHPROVIDER_NOT_AVAILABLE");
        }
        SearchProvider searchProvider = searchProviderOpt.get();
        SearchResults searchResults = innersearch(searchproviderid, searchString, searchPath, multifield, numberOfResults);
        mSearchResults.addSearchResults(searchResults.getSearchResults(), searchProvider.getId(), searchProvider.getIdentifier());
        mSearchResults.setUsedquery(searchResults.getUsedQuery());
    }

    private SearchResults innersearch(Integer searchProviderId, String searchString, String searchPath,
                                     Boolean multifield, Integer numberOfResults)
            throws ApplicationException, IOException, ParseException {
        Optional<SearchProvider> searchProviderOptional = getSearchProvider(searchProviderId);
        if(multifield == null) {
            multifield = false;
        }
        if(numberOfResults == null) {
            throw new ApplicationException("NumberOfResults not provided!", "NUMBER_OF_RESULTS_MISSING");
        }
        SearchProvider searchProvider;
        if(searchProviderOptional.isEmpty()) {
            throw new ApplicationException("SearchProvider not found with id: " + searchProviderId + "!",
                    "QUERY_PARSE_NOT_SUCCESSFUL");
        }
        searchProvider = searchProviderOptional.get();
        LuceneSearcher searcher = searchProvider.getLuceneSearcher();
        SearchResults searchResults = searcher.search(searchString, searchPath, multifield, numberOfResults);
        searcher.prettyPrint(searchResults);
        return searchResults;
    }
}
