package dreipc.asset.search.repositories;

import dreipc.asset.search.models.AssetIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetIndexRepository extends ElasticsearchRepository<AssetIndex, String>, AssetIndexRepositoryCustom {

}
