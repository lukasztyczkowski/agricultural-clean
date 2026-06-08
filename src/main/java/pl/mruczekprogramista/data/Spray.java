package pl.mruczekprogramista.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.*;


import java.time.LocalDate;

@Entity
public class Spray  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Musisz podać datę")
    @PastOrPresent(message = "Data nie może być z przyszłości")
    private LocalDate madeDate;
    @NotBlank(message = "Musisz wybrać miejsce")
    private String place;
    @NotBlank(message = "Musisz podać nazwę")
    private String sprayName;
    @NotBlank(message = "Musisz podać nazwę roślny")
    private String plantName;

    private Integer sprayNumber;

    @Min(value = 0 , message = "Karencja nie może być ujemna")
    private Integer gracePeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }





    public LocalDate getMadeDate() {
        return madeDate;
    }

    public void setMadeDate(LocalDate madeDate) {
        this.madeDate = madeDate;
    }

    public Integer getSprayNumber() {
        return sprayNumber;
    }



    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSprayName() {
        return sprayName;
    }

    public void setSprayName(String sprayName) {
        this.sprayName = sprayName;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setSprayNumber(Integer sprayNumber) {
        this.sprayNumber = sprayNumber;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }
    @Override
    public boolean equals(Object o) {
        if (this ==o) return true;
        if (!(o instanceof Spray spray)) return false;
        return id != null && id.equals(spray.id);
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
