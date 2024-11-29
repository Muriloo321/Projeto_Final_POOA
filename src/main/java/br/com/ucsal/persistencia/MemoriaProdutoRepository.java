package br.com.ucsal.persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.ucsal.annotations.Singleton;
import br.com.ucsal.model.Produto;

@Singleton
public class MemoriaProdutoRepository implements ProdutoRepository<Produto, Integer>{

    // Mapa para armazenar os produtos, usando o ID como chave
    private Map<Integer, Produto> produtos = new HashMap<>();
    // Variável para gerar IDs únicos de forma thread-safe
    private AtomicInteger currentId = new AtomicInteger(1);

    // Instância única do repositório (singleton)
    private static MemoriaProdutoRepository instancia;
    // Flag que controla se a instância foi criada ou não
    private static boolean instanciaCriada = false;

    // Construtor privado para evitar criação de instâncias fora da classe (Singleton)
    private MemoriaProdutoRepository() {
    }

    // Método para obter a instância única do repositório
    public static synchronized MemoriaProdutoRepository getInstancia() {
        if (instancia == null) {
            // Caso a instância ainda não tenha sido criada, cria uma nova
            instancia = new MemoriaProdutoRepository();
            System.out.println("🚀 Opa, parece que não tinha uma instância! Criando uma nova instância de MemoriaProdutoRepository...");
            instanciaCriada = true; // Marca que a instância foi criada
        } else if (!instanciaCriada) {
            // Caso a instância já tenha sido criada, apenas exibe uma mensagem amigável
            System.out.println("😉 Já temos uma instância por aqui! Retornando a instância existente de MemoriaProdutoRepository.");
            instanciaCriada = true; // Marca que a mensagem já foi exibida
        }
        return instancia;
    }

    // Método para adicionar um produto ao repositório
    @Override
    public void adicionar(Produto entidade) {
        int id = currentId.getAndIncrement(); // Gera um novo ID único
        entidade.setId(id); // Atribui o ID ao produto
        produtos.put(entidade.getId(), entidade); // Adiciona o produto ao mapa
        System.out.println("🎉 Produto " + entidade.getNome() + " adicionado com sucesso! ID atribuído: " + id);
    }

    // Método para atualizar um produto existente no repositório
    @Override
    public void atualizar(Produto entidade) {
        produtos.put(entidade.getId(), entidade); // Atualiza o produto no mapa
        System.out.println("🔄 Produto " + entidade.getNome() + " atualizado com sucesso!");
    }

    // Método para remover um produto pelo ID
    @Override
    public void remover(Integer id) {
        Produto produtoRemovido = produtos.remove(id); // Remove o produto do mapa
        if (produtoRemovido != null) {
            System.out.println("❌ Produto com ID " + id + " removido com sucesso.");
        } else {
            System.out.println("⚠️ Não encontramos um produto com o ID " + id + " para remover.");
        }
    }

    // Método para listar todos os produtos no repositório
    @Override
    public List<Produto> listar() {
        List<Produto> listaProdutos = new ArrayList<>(produtos.values()); // Cria uma lista com todos os produtos
        System.out.println("📜 Listando todos os produtos...");
        return listaProdutos;
    }

    // Método para obter um produto pelo ID
    @Override
    public Produto obterPorID(Integer id) {
        Produto produto = produtos.get(id); // Busca o produto pelo ID
        if (produto != null) {
            System.out.println("🔍 Encontramos o produto com ID " + id + ": " + produto.getNome());
        } else {
            System.out.println("⚠️ Não encontramos produto com ID " + id + "...");
        }
        return produto;
    }
}
