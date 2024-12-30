package com.essat.springfootball.dao;

import com.essat.springfootball.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdministrateur extends JpaRepository<Administrateur, String> {

    Administrateur findById(int id);

    Administrateur findByUsername(String username);
}
