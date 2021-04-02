package pe.edu.upc.service;

import java.util.List;
import java.util.Optional;

import pe.edu.upc.entity.Incidente;

public interface IIncidenteService {

	public Integer insertar(Incidente incidente);

	public void modificar(Incidente incidente);

	public void eliminar(int idincidente);

	Optional<Incidente> listarId(int idincidente);

	List<Incidente> listar();
}
