package br.com.ucsal.controller;

import java.io.IOException;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Rota;
import br.com.ucsal.annotations.logic.DependencyInjector;
import br.com.ucsal.service.ProdutoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Rota(value = "/excluirProdutos")
public class ProdutoExcluirServlet implements Command {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private ProdutoService produtoService;

    public ProdutoExcluirServlet() {
        DependencyInjector.injectDependencies(this); // Injeta as dependências automaticamente
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lógica de exclusão
        Integer id = Integer.parseInt(request.getParameter("id"));
        produtoService.removerProduto(id); // Chama o serviço para remover o produto
        System.out.println("Produto de ID " + id + " excluído com sucesso!"); // Mensagem personalizada no console
        response.sendRedirect("listarProdutos"); // Redireciona para a lista de produtos
    }
}
