package LaFalaise.BridgeQuest.controler;

import LaFalaise.BridgeQuest.entity.*;
import LaFalaise.BridgeQuest.repository.GameRepository;
import LaFalaise.BridgeQuest.repository.PlayerRepository;
import LaFalaise.BridgeQuest.repository.SignatureRepository;
import LaFalaise.BridgeQuest.service.GameService;
import LaFalaise.BridgeQuest.service.PlayerService;
import LaFalaise.BridgeQuest.service.SignatureService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class GameController {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final SignatureRepository signatureRepository;
    private final GameService gameService;
    private final SignatureService signatureService;
    private final PlayerService playerService;

    public GameController(GameRepository gameRepository, PlayerRepository playerRepository, SignatureRepository signatureRepository,
                          GameService gameService, SignatureService signatureService, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.signatureRepository = signatureRepository;
        this.gameService = gameService;
        this.signatureService = signatureService;
        this.playerService = playerService;
    }

    @GetMapping("/games")
    public List<GameEntity> getAllGames() {
        return this.gameRepository.findAll();
    }

    @GetMapping("/game/{gameId}")
    public GameEntity getGameById(@PathVariable Integer gameId) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        return gameEntity.get();
    }

    @GetMapping("/game/{gameId}/players")
    public List<PlayerEntity> getPlayers(@PathVariable Integer gameId) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        return gameEntity.get().getPlayers();
    }

    @GetMapping("/game/{gameId}/player/{playerId}")
    public PlayerEntity getPlayerById(@PathVariable Integer gameId, @PathVariable Integer playerId) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        return gameEntity.get().getPlayerById(playerId);
    }

    @GetMapping("/game/{gameId}/player/{playerId}/signature")
    public List<SignatureEntity> getSignatures(@PathVariable Integer gameId, @PathVariable Integer playerId) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        PlayerEntity playerEntity = gameEntity.get().getPlayerById(playerId);
        return playerEntity.getSignatures();

    }

    //@GetMapping("/game/{gameId}/player/{playerName}")
    //public PlayerEntity getPlayerByName(@PathVariable Integer gameId, @PathVariable String playerName){
     //   Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
     //   return gameEntity.get().getPlayerByName(playerName);
    //}

    @PostMapping("/game")
    public GameEntity createGame(@RequestBody GameEntity gameEntity) {
        return gameService.createGame(gameEntity);
    }

        //a changer
    @PutMapping("/game/{id}")
    public GameEntity updatedGame(@PathVariable Integer id, @RequestBody GameEntity gameEntity) {
        gameEntity.setId(id);
        GameEntity savedGameEntity = this.gameRepository.save(gameEntity);
        return this.gameRepository.save(savedGameEntity);
    }

    //a changer
    @PutMapping("/game/{id}/player")
    public GameEntity updatedPlayer(@PathVariable Integer id, @RequestBody PlayerEntity playerEntity) {
        PlayerEntity savedPlayerEntity = playerService.createPlayer(playerEntity);
        Optional<GameEntity> gameEntity = this.gameRepository.findById(id);
        gameEntity.get().addPlayer(savedPlayerEntity);
        return this.gameRepository.save(gameEntity.get());
    }

    //a changer
    @PutMapping("/game/{gameId}/player/{playerId}/geolocalisation")
    public PlayerEntity updateGeolocalisation(@PathVariable Integer gameId, @PathVariable Integer playerId,
                                     @RequestBody GeolocalisationEntity geolocalisation) {

        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        PlayerEntity player = gameEntity.get().getPlayerById(playerId);
        //System.out.println(player.toString());
        player.getGeolocalisation().setLatitude(geolocalisation.getLatitude());
        player.getGeolocalisation().setLongitude(geolocalisation.getLongitude());
        //player.setGeolocalisation(geolocalisation);
        System.out.println(player.toString());
        return this.playerRepository.save(player);
    }

    //a changer
    @PutMapping("/game/{gameId}/player/{playerId}/signature")
    public PlayerEntity addSignature(@PathVariable Integer gameId, @PathVariable Integer playerId,
                                        @RequestBody SignatureEntity scannedPlayerInfo){
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        PlayerEntity playerScanned = gameEntity.get().getPlayerById(scannedPlayerInfo.getId());
        //this.playerRepository.findById(scannedPlayerInfo.getId());
        System.out.println(playerScanned.toString());
        System.out.println(scannedPlayerInfo.toString());
        //a transporter des le service () !
        //Separation of concerns
        if (!Objects.equals(playerScanned.getPseudo(), scannedPlayerInfo.getPseudo())) {
            //System.out.println("Null");
            return null;
        }
        PlayerEntity playerEntity = gameEntity.get().getPlayerById(playerId);
        if ( playerScanned.getRole() == Role.HUMAIN & playerEntity.getRole() == Role.HUMAIN ) {
            SignatureEntity signatureEntity = signatureService.createSignature(playerScanned, gameEntity.get().getSetting().getHumanSignature());
            SignatureEntity savedSignature = this.signatureRepository.save(signatureEntity);
            playerEntity.addSignature(savedSignature);
            playerEntity.setPoints(playerEntity.getPoints() + savedSignature.getPoints());
            return this.playerRepository.save(playerEntity);
        } else if ( playerScanned.getRole() == Role.ESPRIT & playerEntity.getRole() == Role.HUMAIN ){
            SignatureEntity signatureEntity = signatureService.createSignature(
                    playerEntity, 2);
            // gameEntity.get().getSetting().getSpiritCapture() % de playerEntity.getPoints()
            SignatureEntity savedSignature = this.signatureRepository.save(signatureEntity);
            playerScanned.addSignature(savedSignature);
            playerScanned.setPoints(playerScanned.getPoints() + savedSignature.getPoints());
            this.playerRepository.save(playerScanned);
            playerEntity.setRole(Role.ESPRIT);
            return this.playerRepository.save(playerEntity);
        } else if ( playerScanned.getRole() == Role.HUMAIN & playerEntity.getRole() == Role.ESPRIT ) {
            SignatureEntity signatureEntity = signatureService.createSignature(
                    playerScanned, 3);
            SignatureEntity savedSignature = this.signatureRepository.save(signatureEntity);
            playerEntity.addSignature(savedSignature);
            playerEntity.setPoints(playerEntity.getPoints() + savedSignature.getPoints());
            this.playerRepository.save(playerEntity);
            playerScanned.setRole(Role.ESPRIT);
            return this.playerRepository.save(playerEntity);
        }
        return null;


       //SignatureEntity savedSignatureEntity = this.signatureRepository.save(signatureEntity);
       //Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
       //PlayerEntity playerEntity = gameEntity.get().getPlayerById(playerId);
       //playerEntity.addSignature(savedSignatureEntity);
       //return this.gameRepository.save(gameEntity.get());
    }

}
