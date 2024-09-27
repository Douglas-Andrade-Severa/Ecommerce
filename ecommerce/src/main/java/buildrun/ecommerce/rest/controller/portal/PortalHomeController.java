package buildrun.ecommerce.rest.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portal")
public class PortalHomeController {
    private static final String PATH = "portal/";

    @GetMapping("/") // Rota raiz do portal
    public String portalHome() {
        return PATH + "portalHome";  // Renderiza o template "portalHome.html"
    }

    @GetMapping("/login") // Rota de login do portal
    public String portalLogin() {
        return PATH + "portalLogin";  // Renderiza o template "portalLogin.html"
    }
}
