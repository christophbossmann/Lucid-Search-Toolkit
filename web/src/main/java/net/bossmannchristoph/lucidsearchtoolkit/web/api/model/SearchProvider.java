package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.LuceneSearcher;

import java.util.Optional;

public class SearchProvider {

    @JsonIgnore
    private LuceneSearcher luceneSearcher;
    private final Integer id;
    private final String name;
    private final String identifier;
    private final String indexpath;
    private final String outputpath;
    private final String filesrootpath;

    public SearchProvider(@JsonProperty("id") Integer id, @JsonProperty("name") String name, @JsonProperty("identifier") String identifier,
                          @JsonProperty("indexpath") String indexpath, @JsonProperty("outputpath") String outputpath,
                          @JsonProperty("filesrootpath") String filesrootpath) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.indexpath = indexpath;
        this.outputpath = outputpath;
        this.filesrootpath = filesrootpath;
        initLuceneSearcher(indexpath, outputpath);
    }

    private void initLuceneSearcher(String indexPath, String outputPath) {
        try {
            luceneSearcher = new LuceneSearcher();
            luceneSearcher.prepareOutput(outputPath);
            luceneSearcher.init(indexPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {return identifier;}

    public String getIndexpath() {
        return indexpath;
    }

    public String getOutputpath() {
        return outputpath;
    }

    public String getFilesrootpath() {
        return filesrootpath;
    }

    public LuceneSearcher getLuceneSearcher() {
        return luceneSearcher;
    }


}
