package pe.edu.upc.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upc.entity.Proveedor;
import pe.edu.upc.repository.ProveedorRepository;
import pe.edu.upc.service.IProveedorService;

@Service
public class ProveedorServiceImpl implements IProveedorService {

	@Autowired
	private ProveedorRepository pR;

	@Override
	@Transactional
	public Integer insertar(Proveedor proveedor) 
	{
		int rpta = pR.findByRucProveedor(proveedor.getRucProveedor());
		if (rpta == 0) {
			pR.save(proveedor);
		}
		return rpta;

	}

	@Override
	@Transactional
	public Integer modificar(Proveedor proveedor) {
		int rpta = pR.findByRucProveedor(proveedor.getRucProveedor());
		if (rpta == 0) {
			pR.save(proveedor);
		}
		return rpta;
	}

	@Override
	@Transactional
	public void eliminar(int idProveedor) {
		pR.deleteById(idProveedor);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Proveedor> listarId(int idProveedor) {
		return pR.findById(idProveedor);
	}

	@Override
	public List<Proveedor> listar() {
		return pR.findAll(Sort.by(Sort.Direction.DESC, "nombreProveedor"));
	}
	@Override
	@Transactional(readOnly = true)
	public List<Proveedor> buscarDireccion(String direccionProveedor) {
		return pR.findByDireccion(direccionProveedor);
	}

	@Override
	public List<Proveedor> buscarNombre(String nombreProveedor) {
		return pR.findByNombreProveedor(nombreProveedor);
	}
	@Override
	public List<Proveedor> buscarRuc(String RucProveedor) {
		return pR.findByRUC(RucProveedor);
	}

}
