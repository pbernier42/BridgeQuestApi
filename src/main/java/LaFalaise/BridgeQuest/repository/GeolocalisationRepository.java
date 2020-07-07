package LaFalaise.BridgeQuest.repository;

import LaFalaise.BridgeQuest.entity.GeolocalisationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeolocalisationRepository extends JpaRepository<GeolocalisationEntity, Integer> {

}
