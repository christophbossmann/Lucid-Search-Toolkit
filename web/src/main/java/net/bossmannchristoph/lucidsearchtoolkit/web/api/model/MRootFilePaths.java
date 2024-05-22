package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MRootFilePaths {

    public MRootFilePaths(Map<Integer, String> searchProviderRootFilePathMap, LocalDateTime timestamp, int statuscode, String status) {
        this.timestamp = timestamp;
        this.statuscode = statuscode;
        this.status = status;
        rootfilepaths = new ArrayList<>();
        searchProviderRootFilePathMap.forEach((key, value) -> rootfilepaths.add(
                new Entry(key, value)));
        rootfilepaths.sort(Comparator.comparingInt(x -> x.searchproviderid));
    }
    public record Entry(int searchproviderid, String rootfilepath) {
        @Override
        public String toString() {
            return "Entry{" +
                    "searchproviderid=" + searchproviderid +
                    ", rootfilepath='" + rootfilepath + '\'' +
                    '}';
        }
    };

    public LocalDateTime timestamp;
    public int statuscode;
    public String status;
    public List<Entry> rootfilepaths;

    @Override
    public String toString() {
        return "MRootFilePaths{" +
                "rootfilepaths=" + rootfilepaths +
                ", timestamp=" + timestamp +
                ", statuscode=" + statuscode +
                ", status='" + status + '\'' +
                '}';
    }
}
