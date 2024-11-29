package br.com.ucsal.controller;

import java.io.IOException;
import java.io.Serializable;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Rota;
import br.com.ucsal.annotations.logic.DependencyInjector;
import br.com.ucsal.service.ProdutoService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Rota(value = "/adicionarProdutos") // Define a rota para a URL de adicionar produtos
public class ProdutoAdicionarServlet implements Command, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject // Injeção de dependência do serviço de produto
    private ProdutoService produtoService;

    // Construtor que realiza a injeção de dependências automaticamente
    public ProdutoAdicionarServlet() {
        DependencyInjector.injectDependencies(this);
    }

    // Método principal que decide qual método (GET ou POST) chamar com base no tipo de requisição
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            // Se for GET, chama o método doGet para mostrar o formulário de adição de produto
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            // Se for POST, chama o método doPost para adicionar o produto
            doPost(request, response);
        }
    }

    // Método que lida com requisições GET (quando o usuário acessa o formulário para adicionar um produto)
    private void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redireciona o usuário para a página do formulário de adição de produto
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/produtoformulario.jsp");
        dispatcher.forward(request, response);
    }

    // Método que lida com requisições POST (quando o formulário é enviado para adicionar o produto)
    private void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera os parâmetros enviados no formulário
        String nome = request.getParameter("nome");
        double preco = Double.parseDouble(request.getParameter("preco"));

        // Chama o serviço para adicionar o novo produto
        produtoService.adicionarProduto(nome, preco);

        // Exibe uma mensagem de sucesso no console com os detalhes do produto adicionado
        System.out.println("✨ Produto adicionado com sucesso: " + nome + " - R$" + preco);

        // Após adicionar o produto, redireciona para a lista de produtos
        response.sendRedirect("listarProdutos");
    }
}
