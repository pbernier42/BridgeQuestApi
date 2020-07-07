package LaFalaise.BridgeQuest.service;

import LaFalaise.BridgeQuest.entity.GeolocalisationEntity;
import LaFalaise.BridgeQuest.entity.PlayerEntity;
import LaFalaise.BridgeQuest.repository.GeolocalisationRepository;
import LaFalaise.BridgeQuest.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GeolocalisationRepository geolocalisationRepository;

    public PlayerService(PlayerRepository playerRepository, GeolocalisationRepository geolocalisationRepository) {
        this.playerRepository = playerRepository;
        this.geolocalisationRepository = geolocalisationRepository;
    }


    public PlayerEntity createPlayer(PlayerEntity playerEntity){
        GeolocalisationEntity geolocalisationEntity;
        if (playerEntity.getGeolocalisation() != null) {
            geolocalisationEntity = playerEntity.getGeolocalisation();
        } else {
            geolocalisationEntity = new GeolocalisationEntity();
        }
        playerEntity.setGeolocalisation(this.geolocalisationRepository.save(geolocalisationEntity));
        return this.playerRepository.save(playerEntity);
    }
}

//    PlayerEntity savedPlayerEntity = this.playerRepository.save(playerEntity);
 //   Optional<GameEntity> gameEntity = this.gameRepository.findById(id);
  //      gameEntity.get().addPlayer(savedPlayerEntity);
   //             return this.gameRepository.save(gameEntity.get());
