package com.example.app.controllers;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.model.entity.Cliente;
import com.example.app.model.service.IClienteService;
import com.example.app.model.service.IFileUploadService;
import com.example.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService iClienteService;

	@Autowired
	private IFileUploadService iFileUploadService;

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = iFileUploadService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = iClienteService.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());

		return "ver";
	}

	@GetMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = iClienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@GetMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("titulo", "formulario de cliente");
		model.put("cliente", cliente);
		return "form";
	}

	@PostMapping(value = "/form")
	private String guardar(@Valid Cliente cliente, BindingResult result, @RequestParam("file") MultipartFile foto,
			RedirectAttributes flash, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "formulario de cliente");
			return "form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				iFileUploadService.delete(cliente.getFoto());

			}

			String uniqueFileName = "";

			try {
				uniqueFileName = iFileUploadService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}

			cliente.setFoto(uniqueFileName);
			flash.addFlashAttribute("info", "Has subido corectamente " + uniqueFileName);
		}

		String mensajeFlash = (cliente.getId() == null) ? "Cliente creado con exito" : "Cliente editado con exito";

		iClienteService.save(cliente);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@GetMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = iClienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El Id del cliente no existe");
				return "redirect:listar";
			}
		} else {
			flash.addFlashAttribute("error", "El Id del cliente no puede ser cero(0)");
			return "redirect:listar";
		}
		model.put("titulo", "Editar cliente");
		model.put("cliente", cliente);
		return "form";
	}

	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {

			Cliente cliente = iClienteService.findOne(id);

			iClienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito");

			if (iFileUploadService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito");
			}

		}
		return "redirect:/listar";

	}

}
