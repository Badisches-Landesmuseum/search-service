package dreipc.asset.search.repositories;

import dreipc.asset.search.models.ColorIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorIndexRepository extends ElasticsearchRepository<ColorIndex, String> {

}
