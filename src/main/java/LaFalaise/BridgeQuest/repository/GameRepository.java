package LaFalaise.BridgeQuest.repository;

import LaFalaise.BridgeQuest.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Integer> {

}
