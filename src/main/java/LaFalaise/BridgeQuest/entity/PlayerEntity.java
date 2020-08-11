package LaFalaise.BridgeQuest.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class PlayerEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String pseudo;
    private Integer points = 0;
    private Role role = Role.HUMAIN;
    private Boolean visible = true;
    @OneToOne
    private GeolocalisationEntity geolocalisation;
    @OneToMany
    private List<SignatureEntity> signatures = new ArrayList<>();

    public PlayerEntity() {
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Integer getPoints() {
        return points;
    }
    public void setPoints(Integer points) {
        this.points = points;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public GeolocalisationEntity getGeolocalisation() { return geolocalisation; }
    public void setGeolocalisation(GeolocalisationEntity geolocalisation) {
        //this.geolocalisation.setLatitude(geolocalisation.getLatitude());
        //this.geolocalisation.setLongitude(geolocalisation.getLongitude());
        this.geolocalisation = geolocalisation;
    }

    public List<SignatureEntity> getSignatures() {
        return signatures;
    }
    public void setSignatures(List<SignatureEntity> signatures) {
        this.signatures = signatures;
    }
    public void addSignature(SignatureEntity signature) {
        this.signatures.add(signature);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerEntity that = (PlayerEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", points=" + points +
                ", role=" + role +
                ", visible=" + visible +
                ", geolocalisation=" + geolocalisation +
                ", signatures=" + signatures +
                '}';
    }
}
