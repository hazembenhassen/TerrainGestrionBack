package com.essat.springfootball.dao;

import com.essat.springfootball.model.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITerrain extends JpaRepository<Terrain, Integer> {

}
