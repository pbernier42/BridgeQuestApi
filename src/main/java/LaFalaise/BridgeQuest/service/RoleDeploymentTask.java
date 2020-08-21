package LaFalaise.BridgeQuest.service;

import LaFalaise.BridgeQuest.entity.GameEntity;
import LaFalaise.BridgeQuest.entity.PlayerEntity;
import LaFalaise.BridgeQuest.entity.Role;
import LaFalaise.BridgeQuest.repository.GameRepository;
import LaFalaise.BridgeQuest.repository.PlayerRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;


public class RoleDeploymentTask implements Runnable{

    private Integer gameId;
    private Integer deploymentTime;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Override
    @Transactional
    public void run() {
        try {
            System.out.println("Start task \'Deploiment\'");
            Thread.sleep(((this.deploymentTime * 1000 * 60) - 1000));
            //Thread.sleep(1);
            Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
            List<PlayerEntity> playerEntities = gameEntity.get().getPlayers();
            Integer spiritNumber = playerEntities.size() / (100 / gameEntity.get().getSetting().getSpiritNumber());
            while (spiritNumber != 0) {
                Random random = new Random();
                Integer index = random.nextInt(playerEntities.size());
                if (playerEntities.get(index).getRole() == Role.HUMAIN) {
                    playerEntities.get(index).setRole(Role.ESPRIT);
                    spiritNumber = spiritNumber - 1;
                    this.playerRepository.save(playerEntities.get(index));
                }
                System.out.println("Spirit " + playerEntities.get(index).getPseudo());
            }
            System.out.println(playerEntities.toString());
            //for (PlayerEntity player : playerEntities) {
            //    this.playerRepository.save(player);
            //}
            //gameEntity.get().setPlayers(playerEntities);
            //System.out.println(gameEntity.get().getPlayers().toString());
            //this.gameRepository.save(gameEntity.get());
            //System.out.println(this.gameRepository.findById(gameId).get().getPlayers());
            System.out.println("Deploiment Done");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RoleDeploymentTask(Integer gameId, Integer deploymentTime,
                              GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameId = gameId;
        this.deploymentTime = deploymentTime;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }
}
