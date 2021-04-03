package pe.edu.upc.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upc.entity.Incidente;
import pe.edu.upc.repository.IncidenteRepository;
import pe.edu.upc.service.IIncidenteService;

@Service
public class IncidenteServiceImpl implements IIncidenteService {

	@Autowired
	private IncidenteRepository fR;
	

	@Override
	@Transactional
	public Integer insertar(Incidente Incidente) {

		int rpta = 0;
		if (rpta == 0) {
			fR.save(Incidente);
		}
		return rpta;

	}

	@Override
	@Transactional
	public void modificar(Incidente Incidente) {

		fR.save(Incidente);
	}

	@Override
	@Transactional
	public void eliminar(int idIncidente) {

		fR.deleteById(idIncidente);

	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Incidente> listarId(int idIncidente) {
		Optional<Incidente> lista = fR.findById(idIncidente);
		if (lista.isPresent()) {
		}
		return lista;
	}

	@Override
	public List<Incidente> listar() {
		return fR.findAll();
	}


}

