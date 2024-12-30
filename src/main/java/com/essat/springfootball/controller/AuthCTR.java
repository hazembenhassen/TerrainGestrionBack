package com.essat.springfootball.controller;

import com.essat.springfootball.dao.IAdherant;
import com.essat.springfootball.dao.IAdministrateur;
import com.essat.springfootball.dao.IReservation;
import com.essat.springfootball.dao.ITerrain;
import com.essat.springfootball.model.Adherant;
import com.essat.springfootball.model.Administrateur;
import com.essat.springfootball.model.Reservation;
import com.essat.springfootball.model.Terrain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200/")
public class AuthCTR {

    @Autowired
    private IAdministrateur adminDAO;

    @Autowired
    private ITerrain terrainDAO;

    @Autowired
    private IReservation resDAO;

    @Autowired
    private IAdherant adhDAO;


    @PostMapping("/login")
    public ResponseEntity<Administrateur> loginAdmin(@RequestBody Administrateur adminData) {
        // Find the admin by username
        Administrateur admin = adminDAO.findByUsername((adminData.getUsername()));

        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Admin not found
        }

        // Check if the password matches
        if (admin.getPassword().equals(adminData.getPassword())) {
            return ResponseEntity.ok(admin); // Authentication successful
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Invalid password
    }



    @GetMapping("/terrain")
    public List<Terrain> f1() {
        return terrainDAO.findAll();
    }

    @GetMapping("/resevation")
    public List<Reservation> f2() {
        return resDAO.findAll();
    }


    @GetMapping("/Adherant")
    public List<Adherant> f3() { return adhDAO.findAll();}

    @DeleteMapping("/terrain/{id}")
    public ResponseEntity<String> deleteTerrain(@PathVariable int id) {
        // Check if the terrain exists
        if (!terrainDAO.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Terrain not found.");
        }

        // Find and delete all related Reservations
        List<Reservation> linkedReservations = resDAO.findByTerrainId(id);
        if (linkedReservations != null && !linkedReservations.isEmpty()) {
            resDAO.deleteAll(linkedReservations);
        }

        // Delete the Terrain
        terrainDAO.deleteById(id);
        return ResponseEntity.ok("Terrain deleted successfully, along with related reservations.");
    }


    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable int id) {
        if (resDAO.existsById(id)) {
            resDAO.deleteById(id);
            return ResponseEntity.ok("Reservation deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found.");
    }

    @DeleteMapping("/adherant/{id}")
    public ResponseEntity<String> deleteAdherant(@PathVariable int id) {
        if (adhDAO.existsById(id)) {
            adhDAO.deleteById(id);
            return ResponseEntity.ok("Adherant deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adherant not found.");
    }

    @PostMapping("/addReservation")
    public ResponseEntity<String> addReservation(@RequestBody Reservation reservation) {
        // Vérifiez si le terrain existe
        Optional<Terrain> optionalTerrain = terrainDAO.findById(reservation.getTerrain().getId());

        if (optionalTerrain.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Terrain non trouvé");
        }

        Terrain terrain = optionalTerrain.get();
        List<Reservation> existingReservations = resDAO.findByTerrainAndDate(terrain, reservation.getDate_res());
        if (!existingReservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Le terrain est déjà réservé à cette heure");
        }
        resDAO.save(reservation);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Réservation créée avec succès");
    }

}
