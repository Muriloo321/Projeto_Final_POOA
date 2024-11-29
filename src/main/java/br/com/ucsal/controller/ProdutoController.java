package br.com.ucsal.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/view/*", "/"}) // Mapeamento para capturar qualquer caminho dentro de "/view"
public class ProdutoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private Map<String, Command> commands; // Mapa para armazenar os comandos associados aos caminhos solicitados

    // Método de inicialização, chamado uma vez quando o servlet é carregado
    @SuppressWarnings("unchecked")
    @Override
    public void init() throws ServletException {
        // Recupera o mapa de comandos armazenado no contexto da aplicação
        this.commands = (Map<String, Command>) getServletContext().getAttribute("commands");
        
        if (this.commands == null) {
            // Caso o mapa de comandos não esteja disponível, exibe um aviso
            System.out.println("😓 Erro: Mapa de comandos não encontrado no contexto da aplicação.");
        } else {
            // Caso o mapa de comandos seja encontrado, exibe uma mensagem de sucesso
            System.out.println("✅ Mapa de comandos carregado com sucesso.");
        }
    }

    // Método responsável por tratar as requisições HTTP (GET, POST, etc.)
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera o caminho completo da requisição, por exemplo: /seu-app/view/adicionarProdutos
        String requestURI = request.getRequestURI();
        
        // Recupera o contexto da aplicação, por exemplo: /seu-app
        String contextPath = request.getContextPath();

        // Remove o contexto da URL para obter apenas o caminho da requisição após o contexto
        String path = requestURI.substring(contextPath.length());

        // Caso o caminho esteja vazio ou seja apenas "/", define como o caminho para listar produtos
        if (path == null || path.equals("/")) {
            path = "/listarProdutos"; // Redireciona para a página de listagem de produtos
        }

        System.out.println("🔍 Caminho solicitado: " + path);

        // Verifica se o mapa de comandos foi carregado corretamente
        if (this.commands == null) {
            // Caso o mapa de comandos seja nulo, gera um erro e envia uma resposta de falha
            System.out.println("❌ Erro: Mapa de comandos não encontrado no contexto durante a requisição.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no servidor ao processar a requisição.");
            return;
        }

        // Verifica se existe um comando associado ao caminho solicitado
        Command command = commands.get(path);

        if (command != null) {
            // Se o comando for encontrado, executa-o
            System.out.println("🚀 Executando comando para o caminho: " + path);
            command.execute(request, response);
        } else {
            // Caso o comando não seja encontrado, retorna um erro 404
            System.out.println("⚠️ Comando não encontrado para o caminho: " + path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página não encontrada para o caminho: " + path);
        }
    }
}
