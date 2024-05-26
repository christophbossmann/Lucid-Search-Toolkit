package net.bossmannchristoph.lucidsearchtoolkit.web.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import net.bossmannchristoph.lucidsearchtoolkit.explorertools.ExplorerIOException;
import net.bossmannchristoph.lucidsearchtoolkit.explorertools.ExplorerWindows;
import net.bossmannchristoph.lucidsearchtoolkit.explorertools.IExplorer;
import net.bossmannchristoph.lucidsearchtoolkit.web.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExplorerService {

    @Autowired
    ConfigService configService;
    private final IExplorer explorer;

//searchProviderRootFilePathMap.put(1, "C:\\Users\\chris\\dev\\Lucid-Search-Toolkit\\core\\documents\\input");
    public ExplorerService() {
        explorer = ExplorerWindows.getInstance();
    }

    public void openFile(String path) throws ApplicationException {
        try {
            explorer.openFile(path);
        } catch (ExplorerIOException e) {
            throw new ApplicationException(e.getMessage(), "OPEN_FILE_NOT_SUCCESSFUL", e);
        }
    }

    public void openFileRelative(String path, int searchProviderId) throws ApplicationException{
        openFile(getCombinedPath(path, searchProviderId));
    }

    public void navigateAndSelect(String path) throws ApplicationException {
        try {
            explorer.navigateAndSelect(path);
        } catch (ExplorerIOException e) {
            throw new ApplicationException(e.getMessage(), "NAVIGATE_AND_SELECT_NOT_SUCCESSFUL", e);
        }
    }

    public void navigateAndSelectRelative(String path, int searchProviderId) throws ApplicationException {

        navigateAndSelect(getCombinedPath(path, searchProviderId));

    }

    public Map<Integer, String> getRootFilePaths() {
        Map<Integer, String> rootFilePaths = new HashMap<>();
        JsonNode jsonSearchProviders = configService.getConfig().get("searchproviders");
        for(JsonNode obj : jsonSearchProviders) {
            rootFilePaths.put(obj.get("id").intValue(), obj.get("filesrootpath").textValue());
        }
        return rootFilePaths;
    }

    public String getRootFilePath(int searchProviderId) throws ApplicationException {
        if(getRootFilePaths().get(searchProviderId) == null) {
            throw new ApplicationException("Root path for SearchProvider with id: " + searchProviderId + " not registered!", "ROOT_PATH_NOT_REGISTERED");
        }
        return getRootFilePaths().get(searchProviderId);
    }

    private String getCombinedPath(String path, int searchProviderId) throws ApplicationException {
        String rootPath = getRootFilePaths().get(searchProviderId);
        if(rootPath == null) {
            throw new ApplicationException("Root path for SearchProvider with id: " + searchProviderId + " not registered!", "ROOT_PATH_NOT_REGISTERED");
        }
        Path finalPath = Paths.get(rootPath, path);
        return finalPath.toAbsolutePath().toString();
    }
}
