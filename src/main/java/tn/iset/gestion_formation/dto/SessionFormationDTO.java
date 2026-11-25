
package tn.iset.gestion_formation.dto;

import java.time.LocalDate;

public class SessionFormationDTO {

    private Long id;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long formateurId;

    public SessionFormationDTO() {
    }

    public SessionFormationDTO(
            Long id,
            String description,
            LocalDate dateDebut,
            LocalDate dateFin,
            Long formateurId) {

        this.id = id;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.formateurId = formateurId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Long getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }
}
