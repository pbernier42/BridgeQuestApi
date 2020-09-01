package LaFalaise.BridgeQuest.service;

import LaFalaise.BridgeQuest.entity.GameEntity;
import LaFalaise.BridgeQuest.entity.PlayerEntity;
import LaFalaise.BridgeQuest.entity.Role;
import LaFalaise.BridgeQuest.repository.GameRepository;
import LaFalaise.BridgeQuest.repository.PlayerRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RegularHumanPointsTask implements Runnable {

    private Integer gameId;
    private Integer humanPointMinute;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;


    @Override
    @Transactional
    public void run() {
        try {
            System.out.println("Start task \'Distrubution\'");

            Boolean onGoing = true;
            while (onGoing) {
                Thread.sleep(1000 * 60);
                Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
                onGoing = gameEntity.get().getOngoing();
                if (!onGoing) {
                    System.out.println("distribution Done");
                    return;
                }
                List<PlayerEntity> playerEntities = gameEntity.get().getPlayers();
                for (PlayerEntity player : playerEntities) {
                    if (player.getRole() == Role.HUMAIN) {
                        player.setPoints(player.getPoints() + this.humanPointMinute);
                        this.playerRepository.save(player);
                        System.out.println(player.getPseudo());
                    }
                }
                System.out.println("Distribution !");
            }
            System.out.println("Distrubution Done");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RegularHumanPointsTask(Integer gameId, Integer humanPointMinute,
                              GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameId = gameId;
        this.humanPointMinute = humanPointMinute;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

}
