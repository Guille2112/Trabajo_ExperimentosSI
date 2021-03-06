package pe.edu.upc.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.edu.upc.entity.Proveedor;
import pe.edu.upc.service.IProveedorService;

@Controller
@SessionAttributes("{proveedor, usuario_rol, usuario_nombre}")
@RequestMapping("/proveedores")
public class ProveedorController {

	@Autowired
	private IProveedorService pService;

	@RequestMapping("/bienvenido")
	public String irBienvenido() {
		return "bienvenido";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/nuevo")
	public String nuevoProveedor(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("proveedor", new Proveedor());
			model.addAttribute("valorBoton", "Registrar");
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "proveedor/proveedor";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/guardar")
	public String guardarProveedor(@Valid Proveedor proveedor, BindingResult result, Model model, SessionStatus status,
			RedirectAttributes redirAttrs) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());
	
		if (result.hasErrors()) {
			model.addAttribute("valorBoton", "Registrar");
			return "/proveedor/proveedor";
		} else {
			int rpta = -1;
			Optional<Proveedor> proveedorEncontrado = pService.listarId(proveedor.getIdProveedor());
			if (!proveedorEncontrado.isPresent()) {
				rpta = pService.insertar(proveedor);
				redirAttrs.addFlashAttribute("mensaje", "Se registr\u00f3 correctamente");
				if (rpta > 0) {
					model.addAttribute("mensaje", "Ya existe un proveedor con el mismo RUC");
					model.addAttribute("valorBoton", "Registrar");
					status.setComplete();
					return "/proveedor/proveedor";
				}
			} else {
				rpta = pService.modificar(proveedor);
				if (rpta > 0) {
					model.addAttribute("mensaje", "Ya existe un proveedor con el mismo RUC");
					model.addAttribute("valorBoton", "Modificar");
					status.setComplete();
					return "/proveedor/proveedor";
				}
				redirAttrs.addFlashAttribute("mensaje", "Se modific\u00f3 correctamente");
			}

		}
		model.addAttribute("listaProveedores", pService.listar());

		return "redirect:/proveedores/listar";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/listar")
	public String listarProveedores(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());
	
		try {
			model.addAttribute("proveedor", new Proveedor());
			model.addAttribute("listaProveedores", pService.listar());
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "/proveedor/listaProveedor";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/eliminar")
	public String eliminar(Map<String, Object> model, @RequestParam(value = "id") Integer id,
			RedirectAttributes redirAttrs, Model modelo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		
		try {
			modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			modelo.addAttribute("usuario_nombre", authentication.getName().toString());
		
			if (id != null && id > 0) {
				pService.eliminar(id);
				redirAttrs.addFlashAttribute("mensaje", "se cancel\u00f3 el contrato con el proveedor seleccionado");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			redirAttrs.addFlashAttribute("mensaje", "No se puede anular el contrato con el proveedor seleccionado");
		}
		model.put("listaProveedores", pService.listar());
		return "redirect:/proveedores/listar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/detalle/{id}")
	public String detailsProveedor(@PathVariable(value = "id") int id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
			Optional<Proveedor> proveedor = pService.listarId(id);
			if (!proveedor.isPresent()) {
				model.addAttribute("info", "proveedor no existe");
				return "redirect:/proveedores/listar";
			} else {
				model.addAttribute("proveedor", proveedor.get());
			}
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		model.addAttribute("valorBoton", "Modificar");
		return "/proveedor/proveedor";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping("/buscar")
	public String buscar(Map<String, Object> model, @ModelAttribute Proveedor proveedor, Model modelo) throws ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());
	
		List<Proveedor> listaProveedores;
		proveedor.setNombreProveedor(proveedor.getNombreProveedor());
		listaProveedores = pService.buscarNombre(proveedor.getNombreProveedor());
		if (listaProveedores.isEmpty()) {
			model.put("mensaje", "No se encontr\u00f3 al proveedor con el nombre especificado");
		}
		model.put("listaProveedores", listaProveedores);
		return "proveedor/listaProveedor";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping("/buscarruc")
	public String buscarRuc(Map<String, Object> model, @ModelAttribute Proveedor proveedor, Model modelo) throws ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());
	
		List<Proveedor> listaprovedor;
		proveedor.setDireccionProveedor(proveedor.getDireccionProveedor());
		listaprovedor = pService.buscarDireccion(proveedor.getDireccionProveedor());
		if (listaprovedor.isEmpty()) {
			listaprovedor = pService.buscarNombre(proveedor.getDireccionProveedor());
		}
		if (listaprovedor.isEmpty()) {
			listaprovedor = pService.buscarRuc(proveedor.getDireccionProveedor());
		}
		if (listaprovedor.isEmpty()) {
			model.put("mensaje", "No se encontr\u00f3 ning\u00fan resultado");
		}

		model.put("listaProveedores", listaprovedor);
		return "proveedor/listaProveedor";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash, Model modelo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());
	
		Optional<Proveedor> proveedor = pService.listarId(id);
		if (proveedor == null) {
			flash.addFlashAttribute("error", "El proveedor no existe en la base de datos");
			return "redirect:/proveedores/listar";
		}
		model.put("proveedor", proveedor.get());

		return "proveedor/verp";
	}

}
