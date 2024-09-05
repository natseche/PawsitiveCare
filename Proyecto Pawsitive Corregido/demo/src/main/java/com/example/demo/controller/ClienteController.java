package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Cliente;
import com.example.demo.service.ClienteService;
import com.example.demo.service.MascotaService;
import com.example.error.NotFoundException;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @Autowired
    private MascotaService mascotaService;

    // Mostrar todos los clientes
    @GetMapping("/ver")
    public String mostrarClientes(Model model, @RequestParam("veterinarioId") Long veterinarioId) {
        model.addAttribute("clientes", service.findAll());
        model.addAttribute("veterinarioId", veterinarioId);
        return "mostrar_clientes";
    }

    // Mostrar un cliente específico y sus mascotas
    @GetMapping("/ver/{id}")
    public String verCliente(@PathVariable("id") Long id, Model model) {
        Cliente cliente = service.findById(id);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("mascotas", mascotaService.findByDuenoId(id));
            return "cliente";
        } else {
            throw new NotFoundException(id);
        }
    }

    // Agregar un nuevo cliente (Formulario)
    @GetMapping("/agregar")
    public String agregarCliente(Model model, @RequestParam("veterinarioId") Long veterinarioId) {
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        model.addAttribute("veterinarioId", veterinarioId);
        return "agregar_cliente";
    }

    // Procesar el formulario de agregar cliente
    @PostMapping("/agregar")
    public String agregarCliente(@ModelAttribute("cliente") Cliente cliente,
                                 BindingResult result,
                                 @RequestParam("veterinarioId") Long veterinarioId) {
        if (result.hasErrors()) {
            return "agregar_cliente";
        }
        service.add(cliente);
        return "redirect:/clientes/ver?veterinarioId=" + veterinarioId;
    }

    // Eliminar un cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id,
                                  @RequestParam("veterinarioId") Long veterinarioId) {
        service.deleteById(id);
        return "redirect:/clientes/ver?veterinarioId=" + veterinarioId;
    }

    // Modificar un cliente (Formulario)
    @GetMapping("/modificar/{id}")
    public String modificarCliente(@PathVariable("id") Long id,
                                   @RequestParam("veterinarioId") Long veterinarioId,
                                   Model model) {
        Cliente cliente = service.findById(id);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("veterinarioId", veterinarioId);
            return "modificar_cliente";
        } else {
            throw new NotFoundException(id);
        }
    }

    // Procesar el formulario de modificación de cliente
    @PostMapping("/modificar/{id}")
    public String modificarCliente(@ModelAttribute("cliente") Cliente cliente,
                                   BindingResult result,
                                   @RequestParam("veterinarioId") Long veterinarioId) {
        if (result.hasErrors()) {
            return "modificar_cliente";
        }
        service.update(cliente);
        return "redirect:/clientes/ver?veterinarioId=" + veterinarioId;
    }

    // Método para agregar veterinarioId al modelo
    @ModelAttribute
    public void addVeterinarioIdToModel(@RequestParam("veterinarioId") Long veterinarioId, Model model) {
        model.addAttribute("veterinarioId", veterinarioId);
    }
}
