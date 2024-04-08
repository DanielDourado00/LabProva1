package uel.br.restaurante;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

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

    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos, ele vai salvar o restaurante no banco de dados
    //é usado no html novo-restaurante
    @PostMapping("/atualizar-restaurante/{id}") //posta no banco de dados
    public String atualizarrestaurante(@PathVariable("id") Integer id, @Valid Restaurante restaurante, BindingResult result) {
        //printar informacoes do restaurante para debug

        if (id == null) {
            // Lidar com o cenário em que o id é nulo
            // Por exemplo, você pode redirecionar para uma página de erro ou retornar uma mensagem de erro para o usuário
            return "redirect:/listar-restaurante"; // ou outra página adequada
        }

        if (result.hasErrors()) {
            restaurante.setId(id);
            return "atualizar-restaurante";
        }
        restaurante.setId(id); // Definir o ID do restaurante com base no parâmetro do caminho
        restauranteRepository.save(restaurante); // Salvar o restaurante no banco de dados
        return "redirect:/listar-restaurante";
    }

    //esta funcao a seguir é para atualizar o restaurante, ou seja, vai pegar o id do restaurante e vai mostrar na tela os campos para o usuario alterar
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
    //redireciona para a página listar-cardapio.html com o id do restaurante
    @GetMapping("/cardapio/{id}")
    public String cardapio(@PathVariable("id") int id, Model model) {
        System.out.println("\n\nID recebido no RestauranteController cardapio: " + id + "\n\n");
        System.out.println("\n\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n\n");
        Restaurante restaurante = restauranteRepository.findById(id)                   //pega id do restaurante
             .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
     List<Cardapio> cardapios = new ArrayList<>(restaurante.getCardapioSet());                        //pega cardapio do restaurante
        model.addAttribute("restaurante", restaurante);                 //adiciona o restaurante no model
        model.addAttribute("cardapios", cardapios);                     //adiciona o cardapio no model
        return "listar-cardapio";
    }


}
