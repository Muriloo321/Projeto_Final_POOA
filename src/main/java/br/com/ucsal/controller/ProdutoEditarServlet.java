package br.com.ucsal.controller;

import java.io.IOException;
import java.io.Serializable;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Rota;
import br.com.ucsal.annotations.logic.DependencyInjector;
import br.com.ucsal.model.Produto;
import br.com.ucsal.service.ProdutoService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Rota(value = "/editarServlets") // Define a URL que este servlet irá tratar
public class ProdutoEditarServlet implements Command, Serializable {
    private static final long serialVersionUID = 1L;

    @Inject // Injeção de dependência para o serviço de produto
    private ProdutoService produtoService;

    // Construtor que realiza a injeção de dependências automaticamente
    public ProdutoEditarServlet() {
        DependencyInjector.injectDependencies(this);
    }

    // Método que é executado quando uma requisição é feita ao servlet
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verifica qual método HTTP foi usado (GET ou POST)
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            // Se for GET, chama o método doGet para mostrar o formulário de edição
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            // Se for POST, chama o método doPost para atualizar o produto
            doPost(request, response);
        }
    }

    // Método que lida com requisições GET (quando o usuário acessa o formulário de edição)
    private void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera o ID do produto que será editado
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        // Recupera o produto com o ID específico usando o serviço de produtos
        Produto produto = produtoService.obterProdutoPorId(id);
        
        // Define o produto como um atributo na requisição para ser usado na página JSP
        request.setAttribute("produto", produto);
        
        // Redireciona para a página do formulário de edição de produto
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/produtoformulario.jsp");
        dispatcher.forward(request, response);
    }

    // Método que lida com requisições POST (quando o usuário envia o formulário para atualizar o produto)
    private void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera os parâmetros enviados pelo formulário
        Integer id = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        double preco = Double.parseDouble(request.getParameter("preco"));
        
        // Cria um novo objeto Produto com os dados atualizados
        Produto produtoAtualizado = new Produto(id, nome, preco);
        
        // Chama o serviço para atualizar o produto no banco de dados
        produtoService.atualizarProduto(produtoAtualizado);
        
        // Após a atualização, redireciona o usuário para a lista de produtos
        response.sendRedirect("listarProdutos");
    }
}
