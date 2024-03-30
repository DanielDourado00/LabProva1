package uel.br.restaurante;

import java.util.ArrayList;
import java.util.List;

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


    /*funcoes para cadastrar e listar restauratnes, elas trabalham diretamente com o banco de dados, o html lista esta responseavel por listar os
    * restaurantes e tambem para editar e excluir os mesmo, alem de ter um html para cadastrar na qual vai enviar para o banco de dados*/
    /*========================================================================*/
    @GetMapping("/novo-restaurante")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String novorestaurante(Restaurante restaurante) {
        return "novo-restaurante";
    }

    /*Postar no banco de dados*/
    //HTML co clicar em novo restaurante, vai chamar o metodo salvar-restaurante
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
    //ou seja, este metodo e para postar no banco de dados

    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos, ele vai salvar o restaurante no banco de dados
    //é usado no html novo-restaurante
    @PostMapping("/atualizar-restaurante/{id}")    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos.
    public String atualizarrestaurante(@PathVariable("id") int id, @Valid Restaurante restaurante, BindingResult result) {
        if (result.hasErrors()) {                                   //se tiver erro, vai retornar para a página de atualizar restaurante
            restaurante.setId(id);                                  //vai pegar o id do restaurante
            return "atualizar-restaurante";                         //vai retornar para a página de atualizar restaurante
        }
        restauranteRepository.save(restaurante);            //isso vai salvar o restaurante no banco de dados
        return "redirect:/listar-restaurante";              //redireciona para a página de listar restaurante
    }

    @GetMapping("/atualizar-restaurante/{id}")
    public String obterRestauranteParaAtualizar(@PathVariable("id") int id, Model model) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("restaurante", restaurante);
        return "atualizar-restaurante";
    }


    //equivalente ao index, vai mostrar na tela a lista de todos os restaurantes, assim o usuario pode escolher qual restaurante ele quer ver
    @GetMapping("/listar-restaurante")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String listarrestaurante(Model model) {
        model.addAttribute("restaurantes", restauranteRepository.findAll());    //vai adicionar o atributo restaurantes, que vai ser uma lista de todos os restaurantes
        return "listar-restaurante";
    }
    /*========================================================================*/
    //edicao exclusao dos resturantes no html listar restaurante

    @GetMapping ("/excluir-restaurante/{id}")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String excluirrestaurante (@PathVariable("id") int id, Model model) {
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        restauranteRepository.delete(restaurante);    //vai deletar o restaurante
        model.addAttribute("restaurantes", restauranteRepository.findAll());    //vai adicionar o atributo restaurantes, que vai ser uma lista de todos os restaurantes
        return "listar-restaurante";                    //vai retornar para a página de listar restaurante
    }

}
