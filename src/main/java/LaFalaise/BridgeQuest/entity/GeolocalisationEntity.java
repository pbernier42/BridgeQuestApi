package LaFalaise.BridgeQuest.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class GeolocalisationEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private Double latitude = 90.000001;
    private Double longitude = 180.000001;

    public GeolocalisationEntity() {

    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude()  {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude()  {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
