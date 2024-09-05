package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.model.Veterinario;
import com.example.demo.service.ClienteService;
import com.example.demo.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping()
    public String login(Model model){
        return "login_vet";
    }

    @PostMapping()
    public String login(@RequestParam Long cedula, @RequestParam String password, 
                        @RequestParam String type, Model model) {

        switch (type) {
            case "1": // Veterinario
                Veterinario veterinario = veterinarioService.findByCedulaAndClave(cedula, password);
                if (veterinario != null) {
                    model.addAttribute("veterinario", veterinario);
                    return "redirect:/veterinario/" + veterinario.getId();
                }
                break;

            case "3": // Cliente
                Cliente cliente = clienteService.findByCedula(cedula);
                if (cliente != null) {
                    model.addAttribute("cliente", cliente);
                    return "redirect:/client_home";
                }
                break;

            default:
                model.addAttribute("error", "Tipo de usuario no válido");
                return "login_vet"; // Or a generic login page
        }

        model.addAttribute("error", "Usuario no encontrado o credenciales inválidas");
        return "login_vet"; // Or a generic login page
    }
}

