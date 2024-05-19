package net.bossmannchristoph.lucidsearchtoolkit.web.api.service;

import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.LuceneSearcher;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.SearchResults;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.SResults;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.SearchProvider;
import net.bossmannchristoph.lucidsearchtoolkit.web.exception.ApplicationException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SearchProviderService {

    private final List<SearchProvider> indexProviderList = new ArrayList<>();

    public SearchProviderService() {
        SearchProvider indexProvider1 = new SearchProvider(1, "InitalProvider", "C:\\Users\\chris\\dev\\Lucid-Search-Toolkit\\core\\documents\\index", null);
        indexProviderList.add(indexProvider1);
    }

    public List<SearchProvider> getIndexProviderList() {
        return indexProviderList;
    }

    public Optional<SearchProvider> getSearchProvider(Integer id) {
        Optional<SearchProvider> optional = Optional.empty();
        for(SearchProvider indexProvider : indexProviderList) {
            if(Objects.equals(indexProvider.getId(), id)) {
                optional = Optional.of(indexProvider);
                break;
            }
        }
        return optional;
    }

    public SResults search(Integer searchProviderId, String searchString, String searchPath, Boolean multifield, Integer numberOfResults) throws ApplicationException {
        Optional<SearchProvider> searchProviderOptional = getSearchProvider(searchProviderId);
        if(multifield == null) {
            multifield = false;
        }
        if(numberOfResults == null) {
            throw new ApplicationException("NumberOfResults not provided!");
        }
        SearchProvider searchProvider;
        if(searchProviderOptional.isEmpty()) {
            throw new ApplicationException("SearchProvider not found with id: " + searchProviderId + "!");
        }
        try {
            searchProvider = searchProviderOptional.get();
            LuceneSearcher searcher = searchProvider.getLuceneSearcher();
            SearchResults searchResults = searcher.search(searchString, searchPath, multifield, numberOfResults);
            searcher.prettyPrint(searchResults);
            return new SResults(searchResults);
        }
        catch(ParseException e) {
            throw new ApplicationException("Could not parse query: " + e.getMessage());
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }

    }
}
