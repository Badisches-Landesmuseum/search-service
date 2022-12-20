package dreipc.asset.search.repositories;

import dreipc.asset.search.models.PageIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;
public interface PageIndexRepository extends ElasticsearchRepository<PageIndex, String>, PageIndexRepositoryCustom {


    List<PageIndex> findAllByIdIn(Collection<String> ids);

    void deleteAllByAssetId(String id);

    void deleteById(String id);

}
