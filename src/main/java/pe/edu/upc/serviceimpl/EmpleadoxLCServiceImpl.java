package pe.edu.upc.serviceimpl;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.entity.EmpleadoxLC;
import pe.edu.upc.repository.EmpleadoxLCRepository;
import pe.edu.upc.service.IEmpleadoxLCService;

@Service
public class EmpleadoxLCServiceImpl implements IEmpleadoxLCService {

	@Autowired
	private EmpleadoxLCRepository eR;

	@Override
	@Transactional
	public Integer insertar(EmpleadoxLC empleadoxLC) {
		int rpta = 0;
		if (rpta == 0) {
			eR.save(empleadoxLC);
		}
		return rpta;
	}

	@Override
	@Transactional
	public void modificar(EmpleadoxLC empleadoxLC) {
		eR.save(empleadoxLC);

	}

	@Override
	@Transactional
	public void eliminar(int idEmpleadoXLC) {
		eR.deleteById(idEmpleadoXLC);

	}

	@Override
	@Transactional(readOnly = true)
	public Optional<EmpleadoxLC> listarId(int idEmpleadoXLC) {
		return eR.findById(idEmpleadoXLC);
	}

	@Override
	public List<EmpleadoxLC> listar() {

		return eR.findAll();
	}

	@Override
	public List<EmpleadoxLC> buscarNombreEmpleado(String nombreEmpleado) {
		// TODO Auto-generated method stub
		return eR.findByNombreEmpleado(nombreEmpleado);
	}

}