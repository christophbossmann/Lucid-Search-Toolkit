package net.bossmannchristoph.lucidsearchtoolkit.web.api.service;

import net.bossmannchristoph.lucidsearchtoolkit.explorertools.ExplorerIOException;
import net.bossmannchristoph.lucidsearchtoolkit.explorertools.ExplorerWindows;
import net.bossmannchristoph.lucidsearchtoolkit.explorertools.IExplorer;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.MRootFilePaths;
import net.bossmannchristoph.lucidsearchtoolkit.web.exception.ApplicationException;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExplorerService {

    private final IExplorer explorer;

    private final Map<Integer, String> searchProviderRootFilePathMap;

    public ExplorerService() {
        explorer = ExplorerWindows.getInstance();
        searchProviderRootFilePathMap = new HashMap<>();
        searchProviderRootFilePathMap.put(1, "C:\\Users\\chris\\dev\\Lucid-Search-Toolkit\\core\\documents\\input");
        searchProviderRootFilePathMap.put(2, "C:\\Users\\chris\\dev\\Lucid-Search-Toolkit\\core\\documents\\input2");
    }

    public void openFile(String path) throws ApplicationException {
        try {
            explorer.openFile(path);
        } catch (ExplorerIOException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public void openFileRelative(String path, int searchProviderId) throws ApplicationException{
        openFile(getCombinedPath(path, searchProviderId));
    }

    public void navigateAndSelect(String path) throws ApplicationException {
        try {
            explorer.navigateAndSelect(path);
        } catch (ExplorerIOException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public void navigateAndSelectRelative(String path, int searchProviderId) throws ApplicationException {

        navigateAndSelect(getCombinedPath(path, searchProviderId));

    }

    public Map<Integer, String> getRootFilePaths() {
        return searchProviderRootFilePathMap;
    }

    public String getRootFilePath(int searchProviderId) throws ApplicationException {
        if(getRootFilePaths().get(searchProviderId) == null) {
            throw new ApplicationException("Root path for SearchProvider with id: " + searchProviderId + " not registered!");
        }
        return getRootFilePaths().get(searchProviderId);
    }

    private String getCombinedPath(String path, int searchProviderId) throws ApplicationException {
        String rootPath = searchProviderRootFilePathMap.get(searchProviderId);
        if(rootPath == null) {
            throw new ApplicationException("Root path for SearchProvider with id: " + searchProviderId + " not registered!");
        }
        Path finalPath = Paths.get(rootPath, path);
        return finalPath.toAbsolutePath().toString();
    }
}
