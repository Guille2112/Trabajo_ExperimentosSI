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

import pe.edu.upc.entity.Detalle_List_Compra;
import pe.edu.upc.service.IDetalle_List_CompraService;
import pe.edu.upc.service.IListaService;
import pe.edu.upc.service.IRecursoService;

@Controller
@SessionAttributes("detalle")
@RequestMapping("/detalles")
public class Detalle_List_CompraController {

	@Autowired
	private IDetalle_List_CompraService dService;
	@Autowired
	private IListaService lService;
	@Autowired
	private IRecursoService rService;

	@RequestMapping("/bienvenido")
	public String irBienvenido() {
		return "bienvenido";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/nuevo")
	public String nuevoDetalle(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		model.addAttribute("detalle", new Detalle_List_Compra());
		model.addAttribute("listaCompras", lService.listar());
		model.addAttribute("listaRecursos", rService.listar());
		model.addAttribute("valorBoton", "Registrar");
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());
	
		return "detalle/detalle";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/nuevodentro/{id}")
	public String nuevoDetalleespecifico(@PathVariable(value = "id") Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		
		
		model.addAttribute("detalle", new Detalle_List_Compra());
		model.addAttribute("listaCompras", lService.buscarespefico(id));
		model.addAttribute("listaRecursos", rService.listar());
		model.addAttribute("valorBoton", "Registrar");
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());
	
		return "detalle/detalle";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/guardar")
	public String guardarDetalle(@Valid @ModelAttribute(value = "detalle") Detalle_List_Compra detalle_List_Compra,
			BindingResult result, Model model, SessionStatus status, RedirectAttributes redirAttrs) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());

		/*if (result.hasErrors()) {
			model.addAttribute("listaCompras", lService.listar());
			model.addAttribute("listaRecursos", rService.listar());
			model.addAttribute("detalle", detalle_List_Compra);
			model.addAttribute("valorBoton", "Registrar");
			return "/detalle/detalle";
		} else {*/
			int rpta = -1;
			Optional<Detalle_List_Compra> detalleEncontrado = dService.listarId(detalle_List_Compra.getIdDetalle());
			//rService.buscarNombreRecursoCompleto(detalle_List_Compra.getRecursoDetalle().getNombreRecurso()).get(0).getNombreRecurso()
			detalle_List_Compra.setPrecioDetalle(rService.listarId(detalle_List_Compra.getRecursoDetalle().getIdRecurso()).getPrecioRecurso());
			if (!detalleEncontrado.isPresent()) {
				rpta = dService.insertar(detalle_List_Compra);
				redirAttrs.addFlashAttribute("mensaje", "Se registr\u00f3 correctamente");
				if (rpta > 0) {
					model.addAttribute("valorBoton", "Registrar");
					status.setComplete();
					return "/detalle/detalle";
				}

			} else {
				dService.modificar(detalle_List_Compra);
				rpta = 1;
				status.setComplete();
				redirAttrs.addFlashAttribute("mensaje", "Se modific\u00f3 correctamente");
			}

		/*}*/
		model.addAttribute("listaDetalles", dService.listar());

		return "redirect:/detalles/listar";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/listar")
	public String listarDetalles(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());

			model.addAttribute("detalle", new Detalle_List_Compra());
			model.addAttribute("listaDetalles", dService.listar());
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "/detalle/listaDetalle";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/eliminar")
	public String eliminar(Map<String, Object> model, @RequestParam(value = "id") Integer id, Model modelo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		

		try {
			modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			modelo.addAttribute("usuario_nombre", authentication.getName().toString());

			if (id != null && id > 0) {
				dService.eliminar(id);
				model.put("mensaje", "Se ha eliminado el detalle de compra correctamente.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.put("mensaje", "No se puede eliminar el detalle de compra seleccionado.");
		}
		model.put("listaDetalles", dService.listar());

		return "redirect:/detalles/listar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/detalle/{id}")
	public String detailsDetalle(@PathVariable(value = "id") int id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());

			Optional<Detalle_List_Compra> detalle = dService.listarId(id);
			if (!detalle.isPresent()) {
				model.addAttribute("info", "Detalle no existe");
				return "redirect:/detalles/listar";
			} else {
				model.addAttribute("listaCompras", lService.listar());
				model.addAttribute("listaRecursos", rService.listar());
				model.addAttribute("detalle", detalle.get());
			}
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("valorBoton", "Modificar");

		return "/detalle/detalle";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping("/buscar")
	public String buscar(Map<String, Object> model, @ModelAttribute Detalle_List_Compra detalle, Model modelo) throws ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());

		List<Detalle_List_Compra> listaDetalles;

		detalle.setRecursoDetalle(detalle.getRecursoDetalle());
		listaDetalles = dService.FindRecursosByListaCompra(detalle.getListaDetalle().getIdLista());

		if (listaDetalles.isEmpty()) {
			model.put("mensaje", "No se encontraron recursos con la cantidad de unidades especificado.");

		}
		model.put("listaDetalles", listaDetalles);
		return "detalle/listaDetalle";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash, Model modelo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());


		Optional<Detalle_List_Compra> detalle = dService.listarId(id);
		if (detalle == null) {
			flash.addFlashAttribute("error", "El detalle de lista de compra no existe en la base de datos.");
			return "redirect:/detalles/listar";
		}
		model.put("detalle", detalle.get());

		return "detalle/verd";
	}

}
