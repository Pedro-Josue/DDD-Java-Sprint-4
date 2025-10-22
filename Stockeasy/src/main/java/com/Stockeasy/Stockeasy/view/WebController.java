package com.Stockeasy.Stockeasy.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/view/materiais")
    public String materiais() {
        return "dashboard";
    }

    @GetMapping("/view/categorias")
    public String categorias() {
        return "dashboard";
    }

    @GetMapping("/view/movimentacoes")
    public String movimentacoes() {
        return "dashboard";
    }

    @GetMapping("/user/novo")
    public String register() {
        return "register";
    }
}
