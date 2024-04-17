package br.uel.Consumidor;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;

//Trata com Restaurantes
@Controller
public class ConsumidorRestauranteController implements Serializable {

    private static final String SESSION_CONSUMIDOR = "pedido";  //Atributo que representa a sessão do consumidor
    private static final String SESSION_RESTAURANTE = "restaurantes";  //Atributo que representa a sessão do restaurante

    @Autowired
    ConsumidorRestauranteRepository consumidorRepository; //Anotação que indica que a classe é um repositório

    //mostrar menu inicial index
    @GetMapping("/index")  //Anotação que indica que o método é chamado quando a URL é /index
    public String index() {
        return "index";
    }

    @GetMapping("/listar-restaurante")
    public String listarrestaurante(Model model) {
        // Obter a lista de restaurantes
        List<ConsumidorRestaurante> restaurantes = consumidorRepository.findAll();
        //id


        // Para cada restaurante, obter os dados do cardápio e adicionar ao modelo
        for (ConsumidorRestaurante restaurante : restaurantes) {
            List<CardapioConsumidor> cardapios = new ArrayList<>(restaurante.getPedidos());
            model.addAttribute("cardapios_" + restaurante.getId(), cardapios);
        }

        // Adicionar a lista de restaurantes ao modelo
        model.addAttribute("restaurantes", restaurantes);


        return "listar-restaurantes";
    }


    @GetMapping("/cardapio/{id}") // cardapio/id
    public String cardapio(@PathVariable("id") int id, Model model) {
        System.out.println("\n\nID recebido no RestauranteController cardapio: " + id + "\n\n");
        System.out.println("\n\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n\n");
        ConsumidorRestaurante restaurante = consumidorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<CardapioConsumidor> cardapios = new ArrayList<>(restaurante.getPedidos());     // Lista de cardápios do restaurante

        model.addAttribute("restaurante", restaurante); // Adicionar o consumidor ao modelo
        model.addAttribute("cardapios", cardapios);
        return "listar-cardapio";
    }


    @GetMapping("/carrinho/{id}/{cardapioId}") // pedido/id(restaurante)/id(cardapio)
    public String pedido(@PathVariable("id") int id, @PathVariable("cardapioId") int cardapioId, HttpServletRequest request) {

        // Obter o restaurante com base no ID
        ConsumidorRestaurante restaurante = consumidorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        // Obter o cardápio do restaurante com base no ID
        CardapioConsumidor cardapio = restaurante.getPedidos().stream()
                .filter(c -> c.getId() == cardapioId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + cardapioId));

        // Verificar se o item ainda está disponível no cardápio ---funcao falha
        if (!restaurante.getPedidos().contains(cardapio)) {
            // Item não está mais disponível, então redirecione para algum lugar apropriado
            // Remover o item do carrinho
            List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);
            if (pedido != null) {
                pedido.removeIf(item -> item.getId() == cardapioId);
                request.getSession().setAttribute(SESSION_CONSUMIDOR, pedido);
            }
            return "redirect:/listar-restaurantes";
        }

        // Obter o pedido do consumidor da sessão
        List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);
        if (pedido == null) {
            pedido = new ArrayList<>();
        }

        // Encontrar o item no pedido
        Pedido existingItem = pedido.stream()
                .filter(p -> p.getId() == cardapio.getId())
                .findFirst()
                .orElse(null);

        if (existingItem == null) { // Se o item não estiver no pedido, adicione-o com quantidade 1
            Pedido novoPedido = new Pedido(cardapio, 1);
            pedido.add(novoPedido);
        } else { // Se o item já estiver no pedido, aumente sua quantidade
            existingItem.setQuantidade(existingItem.getQuantidade() + 1);
            existingItem.setValor(existingItem.getPreco() * existingItem.getQuantidade());
        }

        // Definir a sessão do consumidor com o pedido atualizado
        request.getSession().setAttribute(SESSION_CONSUMIDOR, pedido);


        return "redirect:/pedido";
    }



    @GetMapping("/pedido")
    public String pedido(HttpServletRequest request, Model model) {
        List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);
        if (pedido == null) {
            pedido = new ArrayList<>();
        }

        // Obter todos os itens do cardápio do banco de dados
        List<CardapioConsumidor> cardapiosDisponiveis = new ArrayList<>();
        List<ConsumidorRestaurante> restaurantes = consumidorRepository.findAll();
        for (ConsumidorRestaurante restaurante : restaurantes) {
            cardapiosDisponiveis.addAll(restaurante.getPedidos());
        }

        // Verificar cada item do pedido em relação aos itens disponíveis no banco de dados
        List<Pedido> pedidosAtualizados = new ArrayList<>();
        for (Pedido itemPedido : pedido) {
            boolean itemEncontrado = false;
            for (CardapioConsumidor itemCardapio : cardapiosDisponiveis) {
                if (itemPedido.getId() == itemCardapio.getId()) {
                    // Se o item do pedido ainda estiver disponível, adicioná-lo à lista de pedidos atualizados
                    pedidosAtualizados.add(itemPedido);
                    itemEncontrado = true;
                    break;
                }
            }
            // Se o item do pedido não foi encontrado nos itens disponíveis, não adicioná-lo aos pedidos atualizados
            if (!itemEncontrado) {
                System.out.println("Item com ID " + itemPedido.getId() + " não está mais disponível e será removido do pedido.");
            }
        }

        // Atualizar o pedido na sessão com os pedidos atualizados
        request.getSession().setAttribute(SESSION_CONSUMIDOR, pedidosAtualizados);

        double total = 0.0;
        for (Pedido item : pedidosAtualizados) {
            total += item.getValor();
        }

        model.addAttribute("pedido", pedidosAtualizados);
        model.addAttribute("total", total);
        model.addAttribute("restaurantes", restaurantes);
        return "pedido";
    }




    @GetMapping("/acrescentar-item/{id}")
    public String acrescentarItem(@PathVariable("id") int id, HttpServletRequest request) {
        List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);
        if (pedido == null) {   // Se o pedido estiver vazio, crie uma nova lista
            pedido = new ArrayList<>();
        }

        Pedido item = pedido.stream()   // Encontre o item no pedido
                .filter(p -> p.getId() == id)   // Filtrar o item com base no ID
                .findFirst()    // Encontrar o primeiro item
                .orElse(null);  // Se não houver item, retorne nulo

        if (item != null) {     // Se o item estiver no pedido, aumente sua quantidade
            item.setQuantidade(item.getQuantidade() + 1);
            item.setValor(item.getPreco() * item.getQuantidade());
        } else {
            System.err.println("Item com ID " + id + " não encontrado no pedido.");
        }

        request.getSession().setAttribute(SESSION_CONSUMIDOR, pedido);  // Definir a sessão do consumidor com o pedido atualizado

        return "redirect:/pedido";
    }
    @GetMapping("/remover-item/{id}")
    public String removerItem(@PathVariable("id") int id, HttpServletRequest request) {
        List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);
        if (pedido == null) {
            pedido = new ArrayList<>();
        }

        Pedido item = pedido.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (item != null) {
            item.setQuantidade(item.getQuantidade() - 1);
            if (item.getQuantidade() <= 0) {    // Se a quantidade for menor ou igual a zero, remova o item do pedido
                pedido.remove(item);            // Remover o item do pedido
            } else {
                item.setValor(item.getPreco() * item.getQuantidade());
            }
        } else {
            System.err.println("Item com ID " + id + " não encontrado no pedido.");
        }

        request.getSession().setAttribute(SESSION_CONSUMIDOR, pedido);

        return "redirect:/pedido";
    }

}

