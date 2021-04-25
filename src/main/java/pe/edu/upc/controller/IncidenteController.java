package pe.edu.upc.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.upc.entity.Incidente;
import pe.edu.upc.service.IIncidenteService;
import pe.edu.upc.service.IListaService;

@Controller
@SessionAttributes("incidente")
@RequestMapping("/incidentes")
public class IncidenteController {

	@Autowired
	private IIncidenteService fService;

	
	@Autowired
	private IListaService icService;

	@RequestMapping("/bienvenido")
	public String irBienvenido() {
		return "bienvenido";
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/nuevo")
	public String nuevoincidente(Model model) {
		model.addAttribute("incidente", new Incidente());
		model.addAttribute("listaLista_Compras", icService.listar());
		model.addAttribute("valorBoton", "Registrar");
		return "/incidente/incidente";
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/guardar")
	public String guardarincidente(@Valid Incidente incidente, BindingResult result, Model model, SessionStatus status,
			RedirectAttributes redirAttrs) throws Exception {
		if (result.hasErrors()) {
			model.addAttribute("listaLista_Compras", icService.listar());
			model.addAttribute("valorBoton", "Registrar");
			return "/incidente/incidente";
		} else {
			int rpta = -1;
			Optional<Incidente> incidenteEncontrado = fService.listarId(incidente.getIdIncidente());
			if (!incidenteEncontrado.isPresent()) {
				rpta = fService.insertar(incidente);
				redirAttrs.addFlashAttribute("mensaje", "Se registr\u00f3 correctamente");
				if (rpta > 0) {
					model.addAttribute("valorBoton", "Registrar");
					status.setComplete();
					return "/incidente/incidente";
				}
			} else {
				fService.modificar(incidente);
				rpta = 1;
				redirAttrs.addFlashAttribute("mensaje", "Se modific\u00f3 correctamente");
			}

		}
		model.addAttribute("listaIncidentes", fService.listar());

		return "redirect:/incidentes/listar";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/listar")
	public String listarincidentes(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("incidente", new Incidente());

			List<Incidente> list = fService.listar();
			model.addAttribute("listaIncidentes", list);
			System.out.println("EL ROLE : "+authentication.getAuthorities());

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "/incidente/listaIncidente";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping("/eliminar")
	public String eliminar(Map<String, Object> model, @RequestParam(value = "id") Integer id,
			RedirectAttributes redirAttrs) {
		try {
			if (id != null && id > 0) {
				fService.eliminar(id);
				redirAttrs.addFlashAttribute("mensaje", "Se cancel\u00f3 la incidente");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.put("mensaje", "No se puede anular la incidente");
		}
		redirAttrs.addFlashAttribute("listaIncidentes", fService.listar());

		return "redirect:/incidentes/listar";
	}

	@Secured({"ROLE_ADMIN"})
	@GetMapping("/detalle/{id}") // modificar
	public String detailsincidente(@PathVariable(value = "id") int id, Model model) {
		try {
			Optional<Incidente> incidente = fService.listarId(id);
			if (!incidente.isPresent()) {
				model.addAttribute("info", "incidente no existe");
				return "redirect:/incidentes/listar";
			} else {
				model.addAttribute("incidente", incidente.get());
				model.addAttribute("listaLista_Compras", icService.listar());

			}

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		model.addAttribute("valorBoton", "Modificar");	
		return "/incidente/incidente";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {

		Optional<Incidente> incidente = fService.listarId(id);
		if (incidente == null) {
			flash.addFlashAttribute("error", "La incidente no existe en la base de datos");
			return "redirect:/incidentes/listar";
		}
		model.put("incidente", incidente.get());
		return "incidente/verf";
	}
}
