package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.MSearchResults;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.Response;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.SearchProvider;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.service.SearchProviderService;
import net.bossmannchristoph.lucidsearchtoolkit.web.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class SearchController {

    @Autowired
    private SearchProviderService searchProviderService;

    public SearchController() {
    }

    @RequestMapping(value = "api/searchprovider", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<SearchProviderListResponse> getSearchProviders() throws ApplicationException {
        List<SearchProvider> searchproviders = searchProviderService.getSearchProviderList();
        return new ResponseEntity<>(new SearchProviderListResponse(searchproviders, "SEARCH_PROVIDERS_OK",HttpStatus.OK.value()), HttpStatus.OK);
    }

    @RequestMapping(value = "api/searchprovider/{searchProviderId}", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<SearchProviderResponse> getSearchProvider(@PathVariable int searchProviderId) throws ApplicationException {
        Optional<SearchProvider> optional = searchProviderService.getSearchProvider(searchProviderId);
        if(optional.isPresent()) {
            return new ResponseEntity<>(new SearchProviderResponse(optional.get(), "SEARCH_PROVIDER_OK",HttpStatus.OK.value()), HttpStatus.OK);
        }
        else {
            throw new ApplicationException("Search provider with id: " + searchProviderId + ", not available", "SEARCH_PROVIDER_NOT_AVAILABLE");
        }
    }

    @RequestMapping(value = "api/searchprovider/{searchProviderIds}/search", method = RequestMethod.GET,
            produces = "application/json")
    public MSearchResults search(@PathVariable String searchProviderIds, @RequestParam String searchString,
                                 @RequestParam String searchPath, @RequestParam Boolean multifield,
                                 @RequestParam Integer numberOfResults) {
        try {
            String[] searchProviderIdsStrArr = searchProviderIds.split(",");
            if(searchProviderIdsStrArr.length == 1) {
                return searchProviderService.search(Integer.parseInt(searchProviderIdsStrArr[0]),
                        searchString, searchPath, multifield, numberOfResults);
            }
            int[] searchProviderIdsArr = Arrays.stream(searchProviderIdsStrArr).
                    mapToInt(Integer::parseInt).toArray();
            return searchProviderService.search(searchProviderIdsArr,
                    searchString, searchPath, multifield, numberOfResults);


        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static class SearchProviderResponse extends Response {
        private final SearchProvider searchprovider;

        public SearchProviderResponse(SearchProvider searchprovider, String status, int statuscode) {
            super(status, statuscode);
            this.searchprovider = searchprovider;
        }
        public SearchProvider getSearchprovider() {
            return searchprovider;
        }
    }

    public static class SearchProviderListResponse extends Response {
        private final List<SearchProvider> searchproviders;

        public SearchProviderListResponse(List<SearchProvider> searchproviders, String status, int statuscode) {
            super(status, statuscode);
            this.searchproviders = searchproviders;
        }
        public List<SearchProvider> getSearchproviders() {
            return searchproviders;
        }
    }




}
