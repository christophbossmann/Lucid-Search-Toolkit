package net.bossmannchristoph.lucidsearchtoolkit.web.api.model;

import java.time.LocalDateTime;

public record MRootFilePath(LocalDateTime timestamp, int statuscode, String status, int searchproviderid, String rootfilepath) {
    public String toString () {
        return "MRootFilePath{" +
                "timestamp=" + timestamp +
                ", statuscode=" + statuscode +
                ", status='" + status + '\'' +
                ", searchproviderid=" + searchproviderid +
                ", rootfilepath='" + rootfilepath + '\'' +
                '}';
    }
}
