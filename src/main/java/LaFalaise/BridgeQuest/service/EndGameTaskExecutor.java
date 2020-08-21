package LaFalaise.BridgeQuest.service;

import LaFalaise.BridgeQuest.entity.GameEntity;
import LaFalaise.BridgeQuest.repository.GameRepository;

import java.util.Optional;

public class EndGameTaskExecutor implements Runnable{

    private Integer gameId;
    private Integer durationGame;
    private final GameRepository gameRepository;

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public EndGameTaskExecutor(Integer gameId, Integer durationGame, GameRepository gameRepository) {
        this.gameId = gameId;
        this.durationGame = durationGame;
        this.gameRepository = gameRepository;
    }
}
