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

import pe.edu.upc.entity.EmpleadoxLC;
import pe.edu.upc.service.IEmpleadoService;
import pe.edu.upc.service.IEmpleadoxLCService;
import pe.edu.upc.service.IListaService;

@Controller
@SessionAttributes("{empleadoxLC, usuario_rol, usuario_nombre}")
@RequestMapping("/empleadoxLCs")
public class EmpleadoxLCController {

	@Autowired
	private IEmpleadoxLCService elService;

	@Autowired
	private IListaService lService;

	@Autowired
	private IEmpleadoService eService;

	@RequestMapping("/bienvenido")
	public String irBienvenido() {
		return "bienvenido";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/nuevo")
	public String nuevoEmpleadoxLC(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("empleadoxLC", new EmpleadoxLC());
			model.addAttribute("listaEmpleados", eService.listar());
			model.addAttribute("listaLista_Compras", lService.listar());
			model.addAttribute("valorBoton", "Registrar");
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "empleadoxLC/empleadoxLC";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/guardar")
	public String guardarEmpleadoxLC(@Valid EmpleadoxLC empleadoxLC, BindingResult result, Model model,
			SessionStatus status, RedirectAttributes redirAttrs) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());
	
		if (result.hasErrors()) {
			model.addAttribute("valorBoton", "Registrar");
			return "/empleadoxLC/empleadoxLC";
		} else {
			int rpta = -1;
			Optional<EmpleadoxLC> empleadoLCEncontrado = elService.listarId(empleadoxLC.getIdEmpleadoXLC());
			if (!empleadoLCEncontrado.isPresent()) {
				rpta = elService.insertar(empleadoxLC);

				redirAttrs.addFlashAttribute("mensaje", "Se registr\u00f3 correctamente");

				if (rpta > 0) {
					model.addAttribute("valorBoton", "Registrar");
					status.setComplete();
					return "/empleadoxLC/empleadoxLC";
				}

			} else {
				elService.modificar(empleadoxLC);
				rpta = 1;
				status.setComplete();
				redirAttrs.addFlashAttribute("mensaje", "Se modific\u00f3 correctamente");
			}

		}
		model.addAttribute("listaEmpleadoxLCs", elService.listar());

		return "redirect:/empleadoxLCs/listar";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/listar")
	public String listarEmpleadoxLC(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());
	
		try {
			model.addAttribute("empleadoxLC", new EmpleadoxLC());
			model.addAttribute("listaEmpleadoxLCs", elService.listar());
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "/empleadoxLC/listaEmpleadoxLC";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/detalle/{id}") // modificar
	public String detailsEmpleadoxLC(@PathVariable(value = "id") int id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
			Optional<EmpleadoxLC> empleadoxLC = elService.listarId(id);
			if (!empleadoxLC.isPresent()) {
				model.addAttribute("info", "facturar no existe");
				return "redirect:/empleadoxLCs/listar";
			} else {
				model.addAttribute("empleadoxLC", empleadoxLC.get());
				model.addAttribute("listaEmpleados", eService.listar());
				model.addAttribute("listaLista_Compras", lService.listar());
				
			}

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		model.addAttribute("valorBoton", "Modificar");
		return "/empleadoxLC/empleadoxLC";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/eliminar")
	public String eliminar(Map<String, Object> model, @RequestParam(value = "id") Integer id,Model modelo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			modelo.addAttribute("usuario_nombre", authentication.getName().toString());
		
			if (id != null && id > 0) {
				elService.eliminar(id);
				model.put("mensaje", "Se cancel\u00f3 la relaci\u00f3n empleado por orden de compra");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.put("mensaje", "No se puede anular la relaci\u00f3n empleado por orden de compra seleccionada");
		}
		model.put("listaEmpleadoxLCs", eService.listar());

		return this.listarEmpleadoxLC((Model) model);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping("/buscar")
	public String buscar(Map<String, Object> model, @ModelAttribute EmpleadoxLC empleadoxLC,Model modelo) throws ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());
	
		List<EmpleadoxLC> listaEmpleadoxLCs;
		listaEmpleadoxLCs = elService.buscarNombreEmpleado(empleadoxLC.getEmpleadoEmpleadoLC().getNombreEmpleado());
		if (listaEmpleadoxLCs.isEmpty()) {
			model.put("mensaje", "No se encontr\u00f3 al empleado con el nombre especificado");
		}
		model.put("listaEmpleadoxLCs", listaEmpleadoxLCs);
		return "empleadoxLC/listaEmpleadoxLC";
	}

}