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

    @GetMapping("/novo-cardapio")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String novocardapio(Cardapio cardapio) {
        return "novo-cardapio";    //vai retornar para a página de novo cardapio
    }

    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos.
    //salvar cardapio no banco de dados na tabela item_cardapio e vincular ao restaurante selecionado
    @PostMapping("/salvar-cardapio")    //A anotação @PostMapping é usada para mapear solicitações HTTP POST para métodos de manipulação de solicitação específicos.
    public String salvarcardapio(@Valid Cardapio cardapio, BindingResult result, Model model, HttpServletRequest request) { //@Valid é uma anotação que valida o objeto que está sendo passado como parâmetro
        if (result.hasErrors()) {    //se tiver erro, vai retornar para a página de novo cardapio
            return "/novo-cardapio";    //vai retornar para a página de novo cardapio
        }
        int idRestaurante = Integer.parseInt(request.getParameter("idRestaurante"));    //vai pegar o id do restaurante
        Restaurante restaurante = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + idRestaurante));
        cardapio.setRestaurante(restaurante);    //vai setar o restaurante no cardapio
        cardapioRepositoy.save(cardapio);    //isso vai salvar o cardapio no banco de dados
        return "redirect:/listar-cardapio";    //redireciona para a página de listar cardapio
    }

  //ao selecionar o restaurante, posso vizualizar o cardapio e alterar os itens
  @PostMapping("/atualizar-cardapio/{id}")
  public String atualizarcardapio(@PathVariable("id") int id, @Valid Cardapio cardapio, BindingResult result, Model model, HttpServletRequest request) {
      if (result.hasErrors()) {
          return "/atualizar-cardapio";
      }

      String idRestauranteParam = request.getParameter("idRestaurante");        //vai pegar o id do restaurante
      int idRestaurante = idRestauranteParam != null ? Integer.parseInt(idRestauranteParam) : 0;    //vai pegar o id do restaurante

      if (idRestaurante == 0) {
            throw new IllegalArgumentException("ID inválido: " + idRestaurante);    //se o id do restaurante for 0, vai retornar uma exceção
      }

      Restaurante restaurante = restauranteRepository.findById(idRestaurante)
              .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + idRestaurante));
      cardapio.setRestaurante(restaurante);
      cardapioRepositoy.save(cardapio);
      return "redirect:/listar-cardapio";
  }

    @GetMapping("/atualizar-cardapio/{id}")    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String obterCardapioParaAtualizar(@PathVariable("id") int id, Model model) {
        Cardapio cardapio = cardapioRepositoy.findById(id)    //vai pegar o id do cardapio
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("cardapio", cardapio);    //vai adicionar o cardapio no model
        return "atualizar-cardapio";    //vai retornar para a página de atualizar cardapio
    }

    //listar o cardapio APENAS do restaurante selecionado
    @GetMapping("/listar-cardapio/")
    public String listarcardapio(Model model) {
        List<Cardapio> cardapio = cardapioRepositoy.findAll();    //vai pegar todos os cardapios
        if (!CollectionUtils.isEmpty(cardapio)) {    //se a lista de cardapio nao estiver vazia
            model.addAttribute("cardapio", cardapio);    //vai adicionar o cardapio no model
        }
        return "listar-cardapio";    //vai retornar para a página de listar cardapio
    }
}
