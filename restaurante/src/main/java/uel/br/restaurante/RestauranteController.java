package uel.br.restaurante;

import java.util.ArrayList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestauranteController {

    @Autowired
    RestauranteRepository restauranteRepository;

    @GetMapping("/novo-restaurante")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String novorestaurante(Restaurante restaurante) {
        return "novo-restaurante";
    }

    /*Postar no banco de dados*/
    @PostMapping("/salvar-restaurante")    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos.
    public String salvarrestaurante(Restaurante restaurante, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/novo-restaurante";
        }
        restauranteRepository.save(restaurante);            //isso vai salvar o restaurante no banco de dados
        return "redirect:/listar-restaurante";              //redireciona para a página de listar restaurante
    }

    //apoos chamar a url novo-restaurante, dentro do html, o usuario vai preencher os campos e clicar em salvar, isso vai chamar o metodo atualizar-restaurante
    //ao clicar em enviar, o metodo atualizar-restaurante vai salvar os dados no banco de dados
    @PostMapping("/atualizar-restaurante/{id}")    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos.
    public String atualizarrestaurante(@PathVariable("id") int id, @Valid Restaurante restaurante, BindingResult result, Model model) {
        if (result.hasErrors()) {                                   //se tiver erro, vai retornar para a página de atualizar restaurante
            restaurante.setId(id);                                  //vai pegar o id do restaurante
            return "atualizar-restaurante";                         //vai retornar para a página de atualizar restaurante
        }
        restauranteRepository.save(restaurante);            //isso vai salvar o restaurante no banco de dados
        return "redirect:/listar-restaurante";              //redireciona para a página de listar restaurante
    }


    //equivalente ao index, vai mostrar na tela a lista de todos os restaurantes, assim o usuario pode escolher qual restaurante ele quer ver
    @GetMapping("/listar-restaurante")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String listarrestaurante(Model model) {
        return "listar-restaurante";
    }
}
