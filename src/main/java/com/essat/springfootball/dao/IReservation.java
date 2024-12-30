package com.essat.springfootball.dao;

import com.essat.springfootball.model.Reservation;
import com.essat.springfootball.model.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IReservation extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r WHERE r.terrain = :terrain AND r.date_res = :date")
    List<Reservation> findByTerrainAndDate(@Param("terrain") Terrain terrain, @Param("date") String date);
    List<Reservation> findByTerrainId(int terrainId);

}
