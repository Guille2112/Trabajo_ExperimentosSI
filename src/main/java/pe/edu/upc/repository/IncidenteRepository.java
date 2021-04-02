package pe.edu.upc.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import pe.edu.upc.entity.Incidente;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Integer> {



}
