package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.MSearchResults;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.SearchProvider;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.service.SearchProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class SearchController {

    @Autowired
    private SearchProviderService searchProviderService;

    public SearchController() {
    }

    @RequestMapping(value = "api/searchprovider/{searchProviderId}", method = RequestMethod.GET)
    public SearchProvider getSearchProvider(@PathVariable int searchProviderId) {
        Optional<SearchProvider> optional = searchProviderService.getSearchProvider(searchProviderId);
        return (SearchProvider) optional.orElse(null);
    }

    @RequestMapping(value = "api/searchprovider/{searchProviderIds}/search", method = RequestMethod.GET)
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


}
