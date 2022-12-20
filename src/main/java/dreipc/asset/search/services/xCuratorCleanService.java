package dreipc.asset.search.services;

import dreipc.asset.search.queries.DeleteQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class xCuratorCleanService {

    private final DeleteQuery deleteQuery;
    private final List<String> indexesFullClean = List.of("colors", "image-search-service_6334534d2583b028b2cef7e0", "xcurator-service.museumobject");


    public xCuratorCleanService(DeleteQuery deleteQuery) {
        this.deleteQuery = deleteQuery;
    }

    public boolean cleanXCurator() {
        var projectId = "6334534d2583b028b2cef7e0";
        return clean(projectId);
    }

    public boolean clean(String projectId) {
        try {
            deleteQuery.assetsByProjectId(projectId);
            indexesFullClean
                    .forEach(deleteQuery::cleanIndex);
            return true;
        } catch (Exception e) {
            log.error("Unable to clean xcurator.", e);
            return false;
        }
    }
}
