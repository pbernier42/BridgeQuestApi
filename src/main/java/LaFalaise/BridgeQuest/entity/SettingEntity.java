package LaFalaise.BridgeQuest.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SettingEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer durationGame = 120;
    private Integer deploymentTime = 10;

    private Integer humanPointMinute = 1;
    private Integer humanSignature = 30;
    private Integer humanLeftBonus = 3;

    //pourcentage
    private Integer spiritNumber = 33;
    private Integer spiritCapture = 30;

    public SettingEntity() {
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDurationGame() {
        return durationGame;
    }
    public void setDurationGame(Integer durationGame) {
        this.durationGame = durationGame;
    }

    public Integer getDeploymentTime() {
        return deploymentTime;
    }
    public void setDeploymentTime(Integer deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    public Integer getHumanPointMinute() {
        return humanPointMinute;
    }
    public void setHumanPointMinute(Integer humanPointMinute) {
        this.humanPointMinute = humanPointMinute;
    }

    public Integer getHumanSignature() {
        return humanSignature;
    }
    public void setHumanSignature(Integer humanSignature) {
        this.humanSignature = humanSignature;
    }

    public Integer getHumanLeftBonus() {
        return humanLeftBonus;
    }
    public void setHumanLeftBonus(Integer humanLeftBonus) {
        this.humanLeftBonus = humanLeftBonus;
    }

    public Integer getSpiritNumber() {
        return spiritNumber;
    }
    public void setSpiritNumber(Integer spiritNumber) {
        this.spiritNumber = spiritNumber;
    }

    public Integer getSpiritCapture() {
        return spiritCapture;
    }
    public void setSpiritCapture(Integer spiritCapture) {
        this.spiritCapture = spiritCapture;
    }

    @Override
    public String toString() {
        return "SettingEntity{" +
                "durationGame=" + durationGame +
                ", deploymentTime=" + deploymentTime +
                ", humanPointMinute=" + humanPointMinute +
                ", humanSignature=" + humanSignature +
                ", humanLeftBonus=" + humanLeftBonus +
                ", spiritNumber=" + spiritNumber +
                ", spiritCapture=" + spiritCapture +
                '}';
    }

}
