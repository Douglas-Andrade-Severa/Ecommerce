package buildrun.ecommerce.rest.controller.ecommerce;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ecommerce")
public class EcommerceHomeController {
    private static final String PATH = "ecommerce/";

    @GetMapping("/")
    public String root() {
        return "redirect:/ecommerce/home"; // Redireciona a raiz para a home do e-commerce
    }

    @GetMapping("/home")
    public String ecommerceHome() {
        return PATH + "home";  // Renderiza o template "home.html"
    }

    @GetMapping("/login") // Mantido simples
    public String ecommerceLogin() {
        return PATH + "ecommerceLogin";  // Renderiza o template "ecommerceLogin.html"
    }
}
