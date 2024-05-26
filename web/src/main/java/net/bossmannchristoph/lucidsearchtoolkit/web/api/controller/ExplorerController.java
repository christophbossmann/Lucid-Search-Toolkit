package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.MRootFilePath;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.MRootFilePaths;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.service.ExplorerService;
import net.bossmannchristoph.lucidsearchtoolkit.web.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class ExplorerController {

    @Autowired
    private ExplorerService explorerService;

    public record ExplorerResult(LocalDateTime timestamp, int statuscode, String status, String message) {
        @Override
        public String toString() {
            return "ExplorerResult{" +
                    "timestamp=" + timestamp +
                    ", statuscode=" + statuscode +
                    ", status='" + status + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    @RequestMapping(value = "api/explorer/openfile", method = RequestMethod.GET, produces = "application/json")
    public ExplorerResult openfile(@RequestParam String path) throws ApplicationException {
        explorerService.openFile(path);
        return new ExplorerResult(LocalDateTime.now(), HttpStatus.OK.value(), "OPEN_FILE_SUCCESSFUL",
                "Opened file with path '" + path + "' successully!");
    }

    @RequestMapping(value = "api/explorer/openfilerelative", method = RequestMethod.GET, produces = "application/json")
    public ExplorerResult openfileRelative(@RequestParam String path, @RequestParam int searchproviderid)
            throws ApplicationException {
        explorerService.openFileRelative(path, searchproviderid);
        return new ExplorerResult(LocalDateTime.now(), HttpStatus.OK.value(), "OPEN_FILE_SUCCESSFUL",
                "Opened file with path '" + path + "' successully!");
    }

    @RequestMapping(value = "api/explorer/navigateandselect", method = RequestMethod.GET, produces = "application/json")
    public ExplorerResult navigateAndSelect(@RequestParam String path) throws ApplicationException  {
        explorerService.navigateAndSelect(path);
        return new ExplorerResult(LocalDateTime.now(), HttpStatus.OK.value(), "NAVIGATE_SELECT_SUCCESSFUL",
                "Navigated to object'" + path + "' and selected it in explorer successully!");
    }

    @RequestMapping(value = "api/explorer/navigateandselectrelative", method = RequestMethod.GET)
    public ExplorerResult navigateAndSelectRelative(@RequestParam String path, @RequestParam int searchproviderid)
            throws ApplicationException {
        explorerService.navigateAndSelectRelative(path,  searchproviderid);
        return new ExplorerResult(LocalDateTime.now(), HttpStatus.OK.value(), "NAVIGATE_SELECT_SUCCESSFUL",
                "Navigated to object'" + path + "' and selected it in explorer successully!");
    }

    @RequestMapping(value = "api/explorer/rootfilepaths", method = RequestMethod.GET)
    public MRootFilePaths getRootFilePaths() {
        return new MRootFilePaths(explorerService.getRootFilePaths(), LocalDateTime.now(),
                HttpStatus.OK.value(), "RETURN_ROOT_FILE_PATHS_SUCCESSFUL");
    }

    @RequestMapping(value = "api/explorer/rootfilepath/{searchProviderId}", method = RequestMethod.GET)
    public MRootFilePath getRootFilePath(@PathVariable int searchProviderId) throws ApplicationException {
        String rootFilePath = explorerService.getRootFilePath(searchProviderId);
        return new MRootFilePath(LocalDateTime.now(), HttpStatus.OK.value(),
                "RETURN_ROOT_FILE_PATH_SUCCESSFUL", searchProviderId, rootFilePath );
    }

}
