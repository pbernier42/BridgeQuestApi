package LaFalaise.BridgeQuest.service;

import LaFalaise.BridgeQuest.entity.GameEntity;
import LaFalaise.BridgeQuest.entity.SettingEntity;
import LaFalaise.BridgeQuest.repository.GameRepository;
import LaFalaise.BridgeQuest.repository.SettingRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final SettingRepository settingRepository;

    public GameService(GameRepository gameRepository, SettingRepository settingRepository) {
        this.gameRepository = gameRepository;
        this.settingRepository = settingRepository;
    }

    public GameEntity createGame(GameEntity gameEntity){
        SettingEntity settingEntity;
        if (gameEntity.getSetting() != null) {
            settingEntity = gameEntity.getSetting();
        } else {
            settingEntity = new SettingEntity();
        }
        gameEntity.setSetting(this.settingRepository.save(settingEntity));
        return this.gameRepository.save(gameEntity);
    }
}
