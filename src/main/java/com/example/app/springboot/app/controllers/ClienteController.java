package com.example.app.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.springboot.app.models.entity.Cliente;
import com.example.app.springboot.app.models.service.IClienteService;
import com.example.app.springboot.app.models.service.IUploadService;
import com.example.app.springboot.app.util.paginator.PageRender;

@Controller
//Anotación para guardar en sesion el objeto Cliente hasta poner status.setComplete en el metodo guardar
@SessionAttributes("cliente")
public class ClienteController {

//	@Autowired
//	@Qualifier("clienteDaoJPA")
//	private IClienteDao clienteDao;

	@Autowired
	private IUploadService uploadService;

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {

			recurso = uploadService.load(filename);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			flash.addAttribute("error", "El cliente no existe en la bbdd");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle del cliente " + cliente.getName());

		return "ver";
	}

	@GetMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 5);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);

		return "listar";
	}

	@GetMapping(value = "/form")
	public String crear(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Formulario de clientes");

		return "form";
	}

	@PostMapping(value = "/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de clientes");
			return "form";
		}
		if (!foto.isEmpty()) {
			// Este codigo es para acceder a recursos estaticos del proyecto
//			Path directorioRecursos = Paths.get("src//main//resources//static/uploads");
			// String rootPath = directorioRecursos.toFile().getAbsolutePath();

			// Este codigo es para acceder a recurso externos del proyecto
			// String rootPath = "C://Temp//uploads";

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				uploadService.delete(cliente.getFoto());
			}
			String uniqueFileName = null;

			try {
				uniqueFileName = uploadService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}

			flash.addAttribute("info", "Has subido correctamente '" + uniqueFileName + "'");

			cliente.setFoto(uniqueFileName);

		}

		String flashMessage = (cliente.getId() != null) ? "Cliente actualizado con exito" : "Cliente creado con éxito";

		clienteService.save(cliente);
		// Finaliza la sesion con el objeto SessionStatus
		status.setComplete();
		flash.addFlashAttribute("success", flashMessage);

		return "redirect:listar";
	}

	@GetMapping(value = "/form/{id}")
	public String editar(@PathVariable Long id, RedirectAttributes flash, Model model) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser 0");
			return "redirect:/listar";
		}

		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Editar cliente");

		return "form";
	}

	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			clienteService.remove(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito");

			if (uploadService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto: " + cliente.getFoto() + "eliminada con exito");
			}
		}

		return "redirect:/listar";
	}

}
