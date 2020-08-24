package LaFalaise.BridgeQuest.service;

import LaFalaise.BridgeQuest.entity.GameEntity;
import LaFalaise.BridgeQuest.entity.PlayerEntity;
import LaFalaise.BridgeQuest.entity.Role;
import LaFalaise.BridgeQuest.entity.SignatureEntity;
import LaFalaise.BridgeQuest.repository.GameRepository;
import LaFalaise.BridgeQuest.repository.PlayerRepository;
import LaFalaise.BridgeQuest.repository.SignatureRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EndGameTaskExecutor implements Runnable{

    private Integer gameId;
    private Integer durationGame;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final SignatureRepository signatureRepository;

    @Override
    public void run() {
        try {
            System.out.println("Start task \'Game end\'");
            //System.out.println(gameEntity.get().getSetting().getDurationGame().toString());
            Thread.sleep((this.durationGame * 1000 * 60));
            //Thread.sleep(2);
            Optional<GameEntity> gameEntity = this.gameRepository.findById(gameId);
            System.out.println("Game end");
            gameEntity.get().setOngoing(false);
            this.gameRepository.save(gameEntity.get());

            List<PlayerEntity> playerEntities = gameEntity.get().getPlayers();

            List<PlayerEntity> winHuman = new ArrayList<PlayerEntity>();
            Boolean humanWon = false;
            for (PlayerEntity player : playerEntities) {
                if (player.getRole() == Role.HUMAIN) {
                    player.setPoints(player.getPoints() + 100);
                    this.playerRepository.save(player);
                    humanWon = true;
                }
            }
            if (!humanWon) {
                List<SignatureEntity> allSignatures = new ArrayList<SignatureEntity>();
                for (PlayerEntity player : playerEntities) {
                    for (SignatureEntity signature : player.getSignatures()) {
                        allSignatures.add(signature);
                    }
                }
                allSignatures.sort((left, right) -> left.getId() - right.getId());
                Integer humanLeftBonus = gameEntity.get().getSetting().getHumanLeftBonus();
                Integer index = 0;
                while (index < humanLeftBonus) {
                    for (PlayerEntity player : playerEntities) {
                        if (player.getPseudo() == allSignatures.get(allSignatures.size() - 1 - humanLeftBonus + index).getPseudo()) {
                            player.setPoints(player.getPoints() + (100 / (humanLeftBonus - index)));
                        }
                    }
                    ++index;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public EndGameTaskExecutor(Integer gameId, Integer durationGame,
                               GameRepository gameRepository, PlayerRepository playerRepository, SignatureRepository signatureRepository) {
        this.gameId = gameId;
        this.durationGame = durationGame;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.signatureRepository = signatureRepository;
    }
}
