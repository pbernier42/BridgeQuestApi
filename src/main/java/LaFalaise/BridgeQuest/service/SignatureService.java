package LaFalaise.BridgeQuest.service;

import LaFalaise.BridgeQuest.entity.PlayerEntity;
import LaFalaise.BridgeQuest.entity.SignatureEntity;
import org.springframework.stereotype.Service;

@Service
public class SignatureService {

    public SignatureService(){

    }

    //A revoir (constructeur)
    public SignatureEntity createSignature(PlayerEntity playerEntity, Integer points){
        SignatureEntity signatureEntity = new SignatureEntity();
        signatureEntity.setPseudo(playerEntity.getPseudo());
        signatureEntity.setRole(playerEntity.getRole());
        signatureEntity.setPoints(points);
        return signatureEntity;
    }
}
