package com.springboot.usuarios.app.springbootusuariosapp.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.springboot.usuarios.app.springbootusuariosapp.models.entity.Usuario;
import com.springboot.usuarios.app.springbootusuariosapp.models.service.IUsuarioService;
import com.springboot.usuarios.app.springbootusuariosapp.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageable = PageRequest.of(page, 5);

        Page<Usuario> usuarios = usuarioService.findAll(pageable);

        PageRender<Usuario> pageRender = new PageRender<>("/listar", usuarios);
        model.addAttribute("titulo", "Listar usuarios");
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @GetMapping("/form")
    public String crear(Map<String, Object> model) {
        model.put("usuario", new Usuario());
        model.put("titulo", "Crear Usuario");
        return "form";
    }

    @PostMapping("/form")
    public String guardar(@Valid Usuario usuario, BindingResult result, Model model,
            @RequestParam("file") MultipartFile foto,
            RedirectAttributes redirectAttributes, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de usuario");
            return "form";
        }

        if (!foto.isEmpty()) {
            Path directorioRecursos = Paths.get("src/main/resources/static/uploads");
            String rootPath = directorioRecursos.toFile().getAbsolutePath();
            try {
                byte[] bytes = foto.getBytes();
                Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
                Files.write(rutaCompleta, bytes);

                usuario.setFoto(foto.getOriginalFilename());

            } catch (Exception e) {

            }

        }
        usuarioService.save(usuario);
        status.setComplete();
        redirectAttributes.addFlashAttribute("success", "Usuario creado con exito");
        return "redirect:listar";
    }

    @GetMapping("/form/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        if (id != null) {
            Usuario usuario = usuarioService.findOne(id);
            if (usuario != null) {
                model.addAttribute("usuario", usuario);
                model.addAttribute("titulo", "Editar Usuario");
                return "form";
            }
        }
        return "redirect:/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (id != null) {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado con exito");
        }
        return "redirect:/listar";
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        Usuario usuario = usuarioService.findOne(id);
        if (usuario == null) {
            return "redirect:/listar";
        }
        model.put("usuario", usuario);
        model.put("titulo", "Detalle usuario: " + usuario.getNombre());

        return "ver";
    }
}
