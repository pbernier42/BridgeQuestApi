package LaFalaise.BridgeQuest.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Boolean ongoing = false;
    private Integer timeLeft;
    private String dateStart;
    @OneToOne
    private SettingEntity setting;

    @OneToMany
    private List<PlayerEntity> players = new ArrayList<>();

    public GameEntity() {

        //this.timeLeft = timeLeft;
        //this.setting = new SettingEntity();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOngoing()  {
        return ongoing;
    }
    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public Integer getTimeLeft()  {
        return timeLeft;
    }
    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getDateStart()  {
        return dateStart;
    }
    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public SettingEntity getSetting()  {
        return setting;
    }
    public void setSetting(SettingEntity settingEntity) {
        this.setting = settingEntity;
    }

    public List<PlayerEntity> getPlayers() {
        return players;
    }
    public void setPlayers(List<PlayerEntity> players) {
        this.players = players;
    }
    public void addPlayer(PlayerEntity player) {
        this.players.add(player);
    }

    public PlayerEntity getPlayerById(Integer playerId){

        for (PlayerEntity player : this.players) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }
    public PlayerEntity getPlayerByName(String playerName){

        for (PlayerEntity player : this.players) {
            if (player.getPseudo().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ongoing=" + ongoing +
                ", players=" + players +
                '}';
    }
}
