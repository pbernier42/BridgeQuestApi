package LaFalaise.BridgeQuest.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class SignatureEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String pseudo;
    private Integer points = 0;
    private Role role = Role.HUMAIN;

    public SignatureEntity() {
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

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignatureEntity that = (SignatureEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SignatureEntity{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", points=" + points +
                ", role=" + role +
                '}';
    }
}
