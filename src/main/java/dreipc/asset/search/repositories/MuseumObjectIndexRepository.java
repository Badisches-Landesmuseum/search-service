package dreipc.asset.search.repositories;

import dreipc.asset.search.models.MuseumObjectIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuseumObjectIndexRepository extends ElasticsearchRepository<MuseumObjectIndex, String>, MuseumObjectIndexRepositoryCustom {

}
