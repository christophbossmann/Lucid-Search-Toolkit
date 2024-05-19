package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bossmannchristoph.lucidsearchtoolkit.core.searcher.LuceneSearcher;

public class SearchProvider {


    @JsonIgnore
    private LuceneSearcher luceneSearcher;
    private final Integer id;
    private final String name;
    public SearchProvider(Integer id, String name, String indexPath, String outputPath) {
        this.id = id;
        this.name = name;
        initLuceneSearcher(indexPath, outputPath);
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

    public LuceneSearcher getLuceneSearcher() {
        return luceneSearcher;
    }


}
