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

    @Autowired
    ConsumidorRestauranteRepository consumidorRepository; //Anotação que indica que a classe é um repositório

    //mostrar menu inicial index
    @GetMapping("/index")  //Anotação que indica que o método é chamado quando a URL é /index
    public String index() {
        return "index";
    }

    @GetMapping("/listar-restaurante")
    //A anotação @GetMapping é usada para mapear solicitações HTTP GET para métodos de manipulação de solicitação específicos.
    public String listarrestaurante(Model model) {
        model.addAttribute("restaurantes", consumidorRepository.findAll()); //vai adicionar os restaurantes no model
        return "listar-restaurantes";
    }

    @GetMapping("/cardapio/{id}") // cardapio/id
    public String cardapio(@PathVariable("id") int id, Model model) {
        System.out.println("\n\nID recebido no RestauranteController cardapio: " + id + "\n\n");
        System.out.println("\n\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n\n");
        ConsumidorRestaurante restaurante = consumidorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<CardapioConsumidor> cardapios = new ArrayList<>(restaurante.getPedidos());

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

        // Se o item existir no pedido, decrementar a quantidade
        if (existingItem != null) {
            existingItem.setQuantidade(existingItem.getQuantidade() - 1);

            // Se a quantidade do item for menor ou igual a zero, remova-o do pedido
            if (existingItem.getQuantidade() <= 0) {
                pedido.remove(existingItem);
            }
        }

        // Definir a sessão do consumidor com o pedido atualizado
        request.getSession().setAttribute(SESSION_CONSUMIDOR, pedido);

        // Redirecionar para a página de listar cardápio do restaurante
        return "redirect:/pedido/" + id;
    }

    @GetMapping("/pedido/{id}")
    public String pedido(@PathVariable("id") int id, HttpServletRequest request, Model model) {

        ConsumidorRestaurante restaurante = consumidorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);
        if (pedido == null) {
            pedido = new ArrayList<>();
        }

        double total = 0.0; // Inicializa o total como zero

        // Calcula o valor total somando o valor de cada item no pedido
        for (Pedido item : pedido) {
            total += item.getValor();
        }


        model.addAttribute("restaurante", restaurante);
        model.addAttribute("pedido", pedido);
        model.addAttribute("total", total);
        return "pedido";
    }


    @GetMapping("/adicionar-pedido/{id}/{cardapioId}")
    public String adicionarPedido(@PathVariable("id") int id, @PathVariable("cardapioId") int cardapioId, HttpServletRequest request) {

        System.out.println("\n\nID do restaurante recebido no /adicionar-pedido/{id}: " + id + "\n\n");
        System.out.println("\n\n ANKARA MESSI ANKARA MESSI \n\n");

        List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);

        // Encontrar o pedido correspondente ao ID do cardápio
        Pedido itemParaAcrescentar = pedido.stream()
                .filter(p -> p.getId() == cardapioId)
                .findFirst()
                .orElse(null);

        if (itemParaAcrescentar != null) { // Se o item existir no pedido, aumentar a quantidade e atualizar o valor
            itemParaAcrescentar.setQuantidade(itemParaAcrescentar.getQuantidade() + 1);
            itemParaAcrescentar.setValor(itemParaAcrescentar.getPreco() * itemParaAcrescentar.getQuantidade());
        } else {
            // Se o item não existir no pedido, talvez seja necessário tratar esse cenário adequadamente
            // Por exemplo, você pode lançar uma exceção ou redirecionar para uma página de erro
            // Aqui, estou apenas imprimindo uma mensagem de erro
            System.err.println("Item com ID " + cardapioId + " não encontrado no pedido.");
        }

        // Atualizar a sessão do consumidor com o pedido modificado
        request.getSession().setAttribute(SESSION_CONSUMIDOR, pedido);

        // Redirecionar para a página de listar cardápio do restaurante
        return "redirect:/pedido/" + id;
    }

    @GetMapping("/remover-pedido/{id}/{cardapioId}")
    public String removerPedido(@PathVariable("id") int id, @PathVariable("cardapioId") int cardapioId, HttpServletRequest request) {

        List<Pedido> pedido = (List<Pedido>) request.getSession().getAttribute(SESSION_CONSUMIDOR);

        // Encontrar o pedido correspondente ao ID do cardápio
        Pedido itemParaRemover = pedido.stream()
                .filter(p -> p.getId() == cardapioId)
                .findFirst()
                .orElse(null);

        if (itemParaRemover != null) { // Se o item existir no pedido
            itemParaRemover.setQuantidade(itemParaRemover.getQuantidade() - 1); // Diminuir a quantidade

            if (itemParaRemover.getQuantidade() <= 0) { // Se a quantidade for menor ou igual a zero, remover o item do pedido
                pedido.remove(itemParaRemover);
            } else {
                // Atualizar o valor se a quantidade for maior que zero
                itemParaRemover.setValor(itemParaRemover.getPreco() * itemParaRemover.getQuantidade());
            }
        } else {
            System.err.println("Item com ID " + cardapioId + " não encontrado no pedido.");
        }

        // Atualizar a sessão do consumidor com o pedido modificado
        request.getSession().setAttribute(SESSION_CONSUMIDOR, pedido);

        // Redirecionar para a página de listar cardápio do restaurante
        return "redirect:/pedido/" + id;
    }



}

/*{

        System.out.println("\n\nID do restaurante recebido no pedido: " + id + "\n\n");
        System.out.println("\n\nID do cardapio recebido no pedido: " + cardapioId + "\n\n");
        System.out.println("\n\nDISJBNFODSNFDNSOFNDSONFODSNFODSNF \n\n");

        ConsumidorRestaurante restaurantes = consumidorRepository.findById(id)   // Obter o restaurante com base no ID
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        List<CardapioConsumidor> pedido = new ArrayList<>(restaurantes.getPedidos()); // Obter o pedido do restaurante

        CardapioConsumidor cardapios = restaurantes.getPedidos().stream() // Obter o cardápio do restaurante com base no ID
                .filter(c -> c.getId() == cardapioId)             // do cardápio
                .findFirst()                                 // Obter o primeiro cardápio que corresponde ao ID
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + cardapioId)); // Lidar com o cenário em que o ID do cardápio é inválido

        boolean itemJaNoPedido = pedido.contains(cardapios); // Verificar se o item já está no pedido
        //se nao tiver no pedido, adicionar 1 unidade do item no pedido
        if (!itemJaNoPedido) {
            Pedido novoPedido = new Pedido();
            novoPedido.setId(cardapios.getId());
            novoPedido.setNome(cardapios.getNome());
            novoPedido.setDescricao(cardapios.getDescricao());
            novoPedido.setPreco(cardapios.getPreco());
            novoPedido.setQuantidade(1);
            pedido.add(novoPedido);
        }else {
            //se tiver no pedido, adicionar mais 1 unidade do item no pedido
            return "redirect:/cardapio/" + id;

        }
        request.getSession().setAttribute(SESSION_CONSUMIDOR, restaurantes); // Definir a sessão do consumidor
        return "redirect:/cardapio/" + id; // Redirecionar para a página de listar cardápio
    }*/