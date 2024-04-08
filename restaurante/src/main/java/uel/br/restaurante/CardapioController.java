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
public class CardapioController {

    @Autowired                          //A anotação @Autowired é usada para permitir que o Spring resolva e injete colaboradores em nosso bean.
    CardapioRepository cardapioRepositoy;    //vai injetar o cardapioRepositoy
    //vincular o cardapio a ser salvo com seu respectivo restaurante
    @Autowired
    RestauranteRepository restauranteRepository;    //vai injetar o restauranteRepository

    //o caradpio precisa ser vinculado ao restaurante selecionado, isso acontecera pelo id, ao selecionar o restaurante que quero aplicar o cardapio, o id do restaurante sera passado para o cardapio

    //novo cardapio que recebera o id do restaurante selecionado

 /*   @GetMapping("/novo-cardapio")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String novoCardapio(Model model, HttpServletRequest request) {
        String idRestauranteParam = request.getParameter("idRestaurante");    //vai pegar o id do restaurante
        int idRestaurante = idRestauranteParam != null ? Integer.parseInt(idRestauranteParam) : 0;    //vai pegar o id do restaurante
        if (idRestaurante == 0) {
            throw new IllegalArgumentException("ID inválido: " + idRestaurante);    //se o id do restaurante for 0, vai retornar uma exceção
        }
        Restaurante restaurante = restauranteRepository.findById(idRestaurante)    //vai pegar o id do restaurante
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + idRestaurante));
        model.addAttribute("restaurante", restaurante);    //vai adicionar o restaurante no model
        model.addAttribute("cardapio", new Cardapio());    //vai adicionar o cardapio no model
        return "novo-cardapio";    //vai retornar para a página de novo cardapio
    }*/

    //funcao novo-cardapio que recebe o id do restaurante e retorna a pagina novo-cardapio
    @GetMapping("/novo-cardapio/{id}")
    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String novoCardapio(@PathVariable("id") int id, Model model) {    //vai pegar o id do cardapio
      System.out.println("\n\nID recebido: no novo-cardapio " + id + "\n\n");
        Restaurante restaurante = restauranteRepository.findById(id)    //vai pegar o id do restaurante
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("restaurante", restaurante);    //vai adicionar o restaurante no model
        model.addAttribute("cardapio", new Cardapio());    //vai adicionar o cardapio no model
        return "novo-cardapio";    //vai retornar para a página de novo cardapio
    }


    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos.
    //salvar cardapio no banco de dados na tabela item_cardapio e vincular ao restaurante selecionado


    @PostMapping("/atualizar-cardapio/{id}")     //atualizar cardapio no banco de dados na tabela item_cardapio e vincular ao restaurante selecionado
    public String atualizarCardapio(@PathVariable("id") Integer id, @Valid Cardapio cardapio, BindingResult result) {

        System.out.println("\n\nID recebido no atualizar-cardapio: " + id + "\n\n");
    //salvo no banco de dados o id do restaurante selecionado, a descricao do item, o preço e nome do item junto com o id dele
        if (id == null) {
            // Lidar com o cenário em que o id é nulo
            // Por exemplo, você pode redirecionar para uma página de erro ou retornar uma mensagem de erro para o usuário
            return "redirect:/listar-cardapio"; // ou outra página adequada
        }

        if (result.hasErrors()) {
            cardapio.setId(id);
            return "atualizar-cardapio";
        }
        System.out.println("\n\nID recebido no atualizar-cardapio: " + id + "\n\n");
        //setar o id do restaurante selecionado
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        cardapio.setRestaurante(restaurante);

        cardapio.setId(id); // Definir o ID do cardapio com base no parâmetro do caminho
        cardapioRepositoy.save(cardapio); // Salvar o cardapio no banco de dados
        return "redirect:/cardapio/" + id; // Redirecionar para a página de listar cardapio
    }





    @GetMapping("/atualizar-cardapio/{id}")
    public String obterCardapioParaAtualizar(@PathVariable("id") Integer id, Cardapio cardapio, Model model) {
           if (id == null) {
                return "redirect:/listar-cardapio";
            }
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));
        model.addAttribute("restaurante", restaurante);
        return "atualizar-cardapio";
    }


    //listar o cardapio APENAS do restaurante selecionado (que corresponde ao id do restaurante)


}

