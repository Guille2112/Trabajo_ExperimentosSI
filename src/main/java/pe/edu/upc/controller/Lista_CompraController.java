package pe.edu.upc.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import pe.edu.upc.entity.Lista_Compra;
import pe.edu.upc.service.IDetalle_List_CompraService;
import pe.edu.upc.service.IListaService;
import pe.edu.upc.service.IProveedorService;

@Controller
@SessionAttributes("{listaCompra, usuario_rol, usuario_nombre}")
@RequestMapping("/listaCompras")
public class Lista_CompraController {

	@Autowired
	private IListaService lService;
	@Autowired
	private IProveedorService pService;

	@Autowired
	private IDetalle_List_CompraService serviceDetalle;

	@GetMapping("/bienvenido")
	public String bienvenido(Model model) {
		return "bienvenido";
	}

	@Secured({ "ROLE_ADMIN" })
	@GetMapping("/nuevo")
	public String nuevaLista_Compra(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("lista_Compra", new Lista_Compra());
			model.addAttribute("listaProveedores", pService.listar());
			model.addAttribute("valorBoton", "Registrar");
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "/listaCompra/listaCompra";
	}

	@PostMapping("/guardar")
	public String guardarLista_Compra(@Valid Lista_Compra lista_Compra, BindingResult result, Model model,
			SessionStatus status, RedirectAttributes redirAttrs) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		model.addAttribute("usuario_nombre", authentication.getName().toString());
	
		if (result.hasErrors()) {
			model.addAttribute("listaProveedores", pService.listar());
			model.addAttribute("valorBoton", "Registrar");
			return "/listaCompra/listaCompra";
		} else {
			int rpta = -1;
			Optional<Lista_Compra> listaEncontrado = lService.listarId(lista_Compra.getIdLista());
			if (!listaEncontrado.isPresent()) {
				rpta = lService.insertar(lista_Compra);
				redirAttrs.addFlashAttribute("mensaje", "Se registr\u00f3 correctamente");
				if (rpta > 0) {
					model.addAttribute("valorBoton", "Registrar");
					status.setComplete();
					return "/listaCompra/listaCompra";
				}

			} else {
				lService.modificar(lista_Compra);
				rpta = 1;
				status.setComplete();
				redirAttrs.addFlashAttribute("mensaje", "Se modific\u00f3 correctamente");
			}
		}
		model.addAttribute("listaLista_Compras", lService.listar());

		return "redirect:/listaCompras/listar";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/listar")
	public String listarLista_Compras(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
			model.addAttribute("lista_Compra", new Lista_Compra());

			List<Lista_Compra> list = lService.listar();
			List<Detalle_List_Compra> detalleLista = serviceDetalle.listar();

			for (Lista_Compra l : list) {
				float precioLista = 0;

				for (Detalle_List_Compra e : detalleLista.stream()
						.filter(c -> c.getListaDetalle().getIdLista() == l.getIdLista()).collect(Collectors.toList()))
					precioLista += e.getPrecioDetalle() * e.getUnidadesDetalle();

				l.setPrecioLista(precioLista);

			}
			model.addAttribute("listaLista_Compras", list);

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "/listaCompra/listaListaCompra";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/eliminar")
	public String eliminar(Model model, @RequestParam(value = "id") Integer id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
			if (id != null && id > 0) {
				lService.eliminar(id);
				model.addAttribute("mensaje", "Se elimin\u00f3 correctamente la lista de compra");
			}
		} catch (Exception e) {

			model.addAttribute("mensaje",
					"No se puede eliminar la lista de compra por que cuenta con detalles de lista de compra y/o al empleado por lista de compra.");
		}

		try {
			model.addAttribute("lista_Compra", new Lista_Compra());

			List<Lista_Compra> list = lService.listar();
			List<Detalle_List_Compra> detalleLista = serviceDetalle.listar();

			for (Lista_Compra l : list) {
				float precioLista = 0;

				for (Detalle_List_Compra e : detalleLista.stream()
						.filter(c -> c.getListaDetalle().getIdLista() == l.getIdLista()).collect(Collectors.toList()))
					precioLista += e.getPrecioDetalle() * e.getUnidadesDetalle();

				l.setPrecioLista(precioLista);

			}

			model.addAttribute("listaLista_Compras", list);

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}

		return "/listaCompra/listaListaCompra";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping("/buscarp")
	public String buscarProveedor(Map<String, Object> model, @ModelAttribute Lista_Compra lista,Model modelo) throws ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());
	
		List<Lista_Compra> listaListas;
		lista.setNotaLista(lista.getNotaLista());
		listaListas = lService.buscar(lista.getNotaLista());
		if (listaListas.isEmpty()) {
			listaListas = lService.buscarProveedor(lista.getNotaLista());
		}
		if (listaListas.isEmpty()) {
			listaListas = lService.buscarEstado(lista.getNotaLista());
		}
		if (listaListas.isEmpty()) {
			model.put("mensaje", "No se encontr\u00f3 ning\u00fan resultado");
		}
		List<Lista_Compra> list = lService.listar();
		List<Detalle_List_Compra> detalleLista = serviceDetalle.listar();
		
		for (Lista_Compra l : list) {
			float precioLista = 0;

			for (Detalle_List_Compra e : detalleLista.stream()
					.filter(c -> c.getListaDetalle().getIdLista() == l.getIdLista()).collect(Collectors.toList()))
				precioLista += e.getPrecioDetalle() * e.getUnidadesDetalle();

			l.setPrecioLista(precioLista);

		}
		modelo.addAttribute("listaLista_Compras", list);
		model.put("listaLista_Compras", listaListas);
		return "listaCompra/listaListaCompra";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@RequestMapping("/buscarm")
	public String buscarMayor(Map<String, Object> model, @ModelAttribute Lista_Compra lista, Model modelo) throws ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());
		List<Lista_Compra> listaListas = new ArrayList<>();
		lista.setPrecioLista(lista.getPrecioLista());

		List<Lista_Compra> list = lService.listar();
		List<Detalle_List_Compra> detalleLista = serviceDetalle.listar();

		for (Lista_Compra l : list) {
			float precioLista = 0;

			for (Detalle_List_Compra e : detalleLista.stream()
					.filter(c -> c.getListaDetalle().getIdLista() == l.getIdLista()).collect(Collectors.toList()))
				precioLista += e.getPrecioDetalle() * e.getUnidadesDetalle();

			l.setPrecioLista(precioLista);

			if (l.getPrecioLista() > lista.getPrecioLista())
				listaListas.add(l);

		}
		if (listaListas.isEmpty()) {
			model.put("mensaje", "No se encontr\u00f3 ning\u00fan resultado");
		}
		model.put("listaLista_Compras", listaListas);
		return "listaCompra/listaListaCompra";

	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/detalle/{id}")
	public String detailsLista(@PathVariable(value = "id") int id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			model.addAttribute("usuario_rol", authentication.getAuthorities().toString());
			model.addAttribute("usuario_nombre", authentication.getName().toString());
		
			Optional<Lista_Compra> detalle = lService.listarId(id);
			if (!detalle.isPresent()) {
				model.addAttribute("info", "Lista de Compra no existe");
				return "redirect:/detalles/listar";
			} else {
				model.addAttribute("listaProveedores", pService.listar());
				model.addAttribute("lista_Compra", detalle.get());
			}
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("valorBoton", "Modificar");

		return "/listaCompra/listaCompra";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash,
			Model model2,Model modelo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		modelo.addAttribute("usuario_rol", authentication.getAuthorities().toString());
		modelo.addAttribute("usuario_nombre", authentication.getName().toString());
	
		Optional<Lista_Compra> lista_Compra = lService.listarId(id);
		if (lista_Compra == null) {
			flash.addFlashAttribute("error", "La lista de compra no existe en la base de datos.");
			return "redirect:/listaCompras/listar";
		}
		model.put("lista_Compra", lista_Compra.get());
		model2.addAttribute("listaRecursosDentro", serviceDetalle.buscarlistaspropietaria(id));
		return "listaCompra/verlc";
	}

}
