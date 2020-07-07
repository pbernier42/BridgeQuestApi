package LaFalaise.BridgeQuest.controler;

import LaFalaise.BridgeQuest.repository.PlayerRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    //@PostMapping("/players")
    //public PlayerEntity createPlayer(@RequestBody PlayerEntity playerEntity) {
    //    return this.playerRepository.save(playerEntity);
    //}
}
