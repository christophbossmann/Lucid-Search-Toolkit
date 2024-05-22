package net.bossmannchristoph.lucidsearchtoolkit.explorertools;

public interface IExplorer {
    void navigate(String path);

    void select(String objectName);

    void navigateAndSelect(String pathString) throws ExplorerIOException;

    void openFile(String path) throws ExplorerIOException;
}
