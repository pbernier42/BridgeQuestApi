package LaFalaise.BridgeQuest.repository;

import LaFalaise.BridgeQuest.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {

}
