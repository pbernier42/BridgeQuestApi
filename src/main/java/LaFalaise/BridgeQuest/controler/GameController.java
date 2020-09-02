package LaFalaise.BridgeQuest.controler;

import LaFalaise.BridgeQuest.entity.*;
import LaFalaise.BridgeQuest.repository.GameRepository;
import LaFalaise.BridgeQuest.repository.PlayerRepository;
import LaFalaise.BridgeQuest.repository.SignatureRepository;
import LaFalaise.BridgeQuest.service.*;
import org.springframework.core.task.TaskExecutor;
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
    private final TaskExecutor taskExecutor;

    public GameController(GameRepository gameRepository, PlayerRepository playerRepository, SignatureRepository signatureRepository,
                          GameService gameService, SignatureService signatureService, PlayerService playerService, TaskExecutor taskExecutor) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.signatureRepository = signatureRepository;
        this.gameService = gameService;
        this.signatureService = signatureService;
        this.playerService = playerService;
        this.taskExecutor = taskExecutor;
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

    @CrossOrigin
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

    @PutMapping("/game/{gameId}/player/{playerId}/visible")
    public PlayerEntity switchPlayerVisible(@PathVariable Integer gameId, @PathVariable Integer playerId) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        PlayerEntity player = gameEntity.get().getPlayerById(playerId);
        player.setVisible(player.getVisible() ? false : true);
        return this.playerRepository.save(player);
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
        System.out.println(player.getPseudo() + " = " + player.getGeolocalisation().getLatitude() + " - " + player.getGeolocalisation().getLongitude());
        return this.playerRepository.save(player);
    }

    //a changer
    @PutMapping("/game/{gameId}/player/{playerId}/signature")
    public PlayerEntity addSignature(@PathVariable Integer gameId, @PathVariable Integer playerId,
                                     @RequestBody SignatureEntity scannedPlayerInfo) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        PlayerEntity playerScanned = gameEntity.get().getPlayerById(scannedPlayerInfo.getId());
        //this.playerRepository.findById(scannedPlayerInfo.getId());
        //System.out.println(playerScanned.toString());
        //System.out.println(scannedPlayerInfo.toString());
        //a transporter des le service () !
        //Separation of concerns

        Boolean SpiritUp = false;
        List<PlayerEntity> playerEntities = gameEntity.get().getPlayers();
        for (PlayerEntity player : playerEntities) {
            if (player.getRole() == Role.ESPRIT) {
                SpiritUp = true;
                break;
            }
        }

        if (!SpiritUp || !Objects.equals(playerScanned.getPseudo(), scannedPlayerInfo.getPseudo())) {
            //System.out.println("Null");
            return null;
        }


        PlayerEntity playerEntity = gameEntity.get().getPlayerById(playerId);
        if (playerScanned.getRole() == Role.HUMAIN & playerEntity.getRole() == Role.HUMAIN) {

            SignatureEntity signatureEntity = signatureService.createSignature(playerScanned, gameEntity.get().getSetting().getHumanSignature());
            for (SignatureEntity playerSignature : playerEntity.getSignatures()) {
                if (playerSignature.getPseudo() == signatureEntity.getPseudo()) {
                    return null;
                }
            }

            SignatureEntity savedSignature = this.signatureRepository.save(signatureEntity);
            playerEntity.addSignature(savedSignature);
            playerEntity.setPoints(playerEntity.getPoints() + savedSignature.getPoints());
            return this.playerRepository.save(playerEntity);

        } else if (playerScanned.getRole() == Role.ESPRIT & playerEntity.getRole() == Role.HUMAIN) {
            SignatureEntity signatureEntity = signatureService.createSignature(
                    playerEntity, (playerEntity.getPoints() / (100 / gameEntity.get().getSetting().getSpiritCapture())));
            SignatureEntity savedSignature = this.signatureRepository.save(signatureEntity);
            playerScanned.addSignature(savedSignature);
            playerScanned.setPoints(playerScanned.getPoints() + savedSignature.getPoints());
            this.playerRepository.save(playerScanned);
            playerEntity.setRole(Role.ESPRIT);
            return this.playerRepository.save(playerEntity);
        } else if (playerScanned.getRole() == Role.HUMAIN & playerEntity.getRole() == Role.ESPRIT) {
            SignatureEntity signatureEntity = signatureService.createSignature(
                    playerScanned, (playerScanned.getPoints() / (100 / gameEntity.get().getSetting().getSpiritCapture())));
            SignatureEntity savedSignature = this.signatureRepository.save(signatureEntity);
            playerEntity.addSignature(savedSignature);
            playerEntity.setPoints(playerEntity.getPoints() + savedSignature.getPoints());
            this.playerRepository.save(playerEntity);
            playerScanned.setRole(Role.ESPRIT);
            playerRepository.save(playerScanned);
            return playerEntity;
        }
        return null;


        //SignatureEntity savedSignatureEntity = this.signatureRepository.save(signatureEntity);
        //Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        //PlayerEntity playerEntity = gameEntity.get().getPlayerById(playerId);
        //playerEntity.addSignature(savedSignatureEntity);
        //return this.gameRepository.save(gameEntity.get());
    }

    @PutMapping("/game/{gameId}/player/{playerId}/convert")
    public Double convertPlayer(@PathVariable Integer gameId, @PathVariable Integer playerId,
                                @RequestBody SignatureEntity scannedPlayerInfo) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        PlayerEntity playerScanned = gameEntity.get().getPlayerById(scannedPlayerInfo.getId());
        PlayerEntity playerEntity = gameEntity.get().getPlayerById(playerId);

        // System.out.println(playerScanned.getGeolocalisation().getLatitude());
        //System.out.println(playerScanned.getGeolocalisation().getLongitude());
        //System.out.println(playerEntity.getGeolocalisation().getLatitude());
        //System.out.println(playerEntity.getGeolocalisation().getLongitude());

        double lat1 = playerScanned.getGeolocalisation().getLatitude();
        double lon1 = playerScanned.getGeolocalisation().getLongitude();
        double lat2 = playerEntity.getGeolocalisation().getLatitude();
        double lon2 = playerEntity.getGeolocalisation().getLongitude();

        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        if (distance <= 15) {
            SignatureEntity signatureEntity = signatureService.createSignature(
                    playerScanned, (playerScanned.getPoints() / (100 / gameEntity.get().getSetting().getSpiritCapture())));

            SignatureEntity savedSignature = this.signatureRepository.save(signatureEntity);
            playerEntity.addSignature(savedSignature);
            System.out.println(playerEntity.getPoints() + " + " + savedSignature.getPoints());
            playerEntity.setPoints(playerEntity.getPoints() + savedSignature.getPoints());
            this.playerRepository.save(playerEntity);
            playerScanned.setRole(Role.ESPRIT);
            this.playerRepository.save(playerScanned);
        }

        return distance;
    }

    @PutMapping("/game/{gameId}/start")
    public GameEntity startGame(@PathVariable Integer gameId) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        gameEntity.get().setOngoing(true);

        System.out.println(gameEntity.get().getPlayers());

        this.taskExecutor.execute(new EndGameTaskExecutor(
                gameId, gameEntity.get().getSetting().getDurationGame(), this.gameRepository, this.playerRepository, this.signatureRepository));
        this.taskExecutor.execute(new RoleDeploymentTask(
                gameId, gameEntity.get().getSetting().getDeploymentTime(), this.gameRepository, this.playerRepository));
        this.taskExecutor.execute(new RegularHumanPointsTask(
                gameId, gameEntity.get().getSetting().getHumanPointMinute(), this.gameRepository, this.playerRepository));

        GameEntity savedGameEntity = this.gameRepository.save(gameEntity.get());
        return savedGameEntity;
    }

    //@DeleteMapping("/game/{gameId}/player/{playerId}")
    //public void deletePlayer(@PathVariable Integer gameId, @PathVariable Integer playerId) {
     //   this.playerRepository.deleteById(playerId);
    //}

    //TEMPORAIRE
    @PutMapping("/game/{gameId}/player/{playerId}/puppet/{puppetId}")
    public PlayerEntity transfomPlayerPuppet(@PathVariable Integer gameId, @PathVariable Integer playerId, @PathVariable Integer puppetId) {
        Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
        PlayerEntity player = gameEntity.get().getPlayerById(playerId);
        player.setId(puppetId);
        return this.playerRepository.save(player);
    }
}