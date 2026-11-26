
package tn.iset.gestion_formation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import tn.iset.gestion_formation.dto.SessionFormationDTO;
import tn.iset.gestion_formation.entity.SessionFormation;
import tn.iset.gestion_formation.service.SessionFormationService;

@RestController
@RequestMapping("/api/sessions")
public class SessionFormationRestController {

    private final SessionFormationService sessionFormationService;

    public SessionFormationRestController(
            SessionFormationService sessionFormationService) {
        this.sessionFormationService = sessionFormationService;
    }

    // GET : Afficher toutes les sessions
    @GetMapping
    public List<SessionFormationDTO> afficherToutesLesSessions() {
        return sessionFormationService.listerToutesLesSessions()
                .stream()
                .map(this::convertirEnDTO)
                .toList();
    }

    // GET : Rechercher une session par ID
    @GetMapping("/{id}")
    public SessionFormationDTO afficherSession(@PathVariable Long id) {
        try {
            SessionFormation session =
                    sessionFormationService.rechercherParId(id);

            return convertirEnDTO(session);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // POST : Ajouter une nouvelle session
    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(
            HttpStatus.CREATED)
    public SessionFormationDTO ajouterSession(
            @RequestBody SessionFormationDTO dto) {
        try {
            SessionFormation session =
                    sessionFormationService.enregistrer(
                            dto.getDescription(),
                            dto.getDateDebut(),
                            dto.getDateFin(),
                            dto.getFormateurId()
                    );

            return convertirEnDTO(session);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // PUT : Modifier une session
    @PutMapping("/{id}")
    public SessionFormationDTO modifierSession(
            @PathVariable Long id,
            @RequestBody SessionFormationDTO dto) {
        try {
            SessionFormation session =
                    sessionFormationService.modifier(
                            id,
                            dto.getDescription(),
                            dto.getDateDebut(),
                            dto.getDateFin(),
                            dto.getFormateurId()
                    );

            return convertirEnDTO(session);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // DELETE : Supprimer une session
    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(
            HttpStatus.NO_CONTENT)
    public void supprimerSession(@PathVariable Long id) {
        try {
            sessionFormationService.supprimer(id);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Convertir une entité SessionFormation en DTO
    private SessionFormationDTO convertirEnDTO(SessionFormation session) {
        Long formateurId = session.getFormateur() != null
                ? session.getFormateur().getId()
                : null;

        return new SessionFormationDTO(
                session.getId(),
                session.getDescription(),
                session.getDateDebut(),
                session.getDateFin(),
                formateurId
        );
    }
}
