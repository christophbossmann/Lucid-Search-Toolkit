package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.SResults;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.SearchProvider;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.service.SearchProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SearchController {

    @Autowired
    private SearchProviderService searchProviderService;

    public SearchController() {
    }

    @RequestMapping(value = "api/searchprovider/{searchProviderId}", method = RequestMethod.GET)
    public SearchProvider getIndexProvider(@PathVariable int searchProviderId) {
        Optional<SearchProvider> optional = searchProviderService.getSearchProvider(searchProviderId);
        return (SearchProvider) optional.orElse(null);
    }

    @RequestMapping(value = "api/searchprovider/{searchProviderId}/search", method = RequestMethod.GET)
    public SResults search(@PathVariable int searchProviderId, @RequestParam String searchString,
                           @RequestParam String searchPath, @RequestParam Boolean multifield, @RequestParam Integer numberOfResults) {
        try {
            return searchProviderService.search(searchProviderId, searchString, searchPath, multifield, numberOfResults);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

    }


}
