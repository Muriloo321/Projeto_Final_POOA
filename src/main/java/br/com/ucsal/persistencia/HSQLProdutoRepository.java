package br.com.ucsal.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.ucsal.model.Produto;
import br.com.ucsal.util.DatabaseUtil;

public class HSQLProdutoRepository implements ProdutoRepository<Produto, Integer>{

    // Método para adicionar um novo produto no banco de dados
    @Override
    public void adicionar(Produto entidade) {
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entidade.getNome()); // Define o nome do produto no comando SQL
            stmt.setDouble(2, entidade.getPreco()); // Define o preço do produto no comando SQL
            stmt.executeUpdate(); // Executa a inserção no banco
            ResultSet rs = stmt.getGeneratedKeys(); // Recupera o ID gerado
            if (rs.next()) {
                entidade.setId(rs.getInt(1)); // Atribui o ID gerado ao produto
                System.out.println("✨ Produto " + entidade.getNome() + " inserido com sucesso! ID atribuído: " + entidade.getId());
            }
        } catch (SQLException e) {
            System.out.println("😓 Oops! Ocorreu um erro ao tentar adicionar o produto. Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para remover um produto pelo ID no banco de dados
    @Override
    public void remover(Integer id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Define o ID do produto a ser removido
            stmt.executeUpdate(); // Executa a remoção
            System.out.println("🗑️ Produto com ID " + id + " removido com sucesso!");
        } catch (SQLException e) {
            System.out.println("😓 Oops! Algo deu errado ao tentar remover o produto. Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para listar todos os produtos no banco de dados
    @Override
    public List<Produto> listar() {
        String sql = "SELECT * FROM produtos";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto produto = new Produto(
                    rs.getInt("id"), // Recupera o ID
                    rs.getString("nome"), // Recupera o nome
                    rs.getDouble("preco") // Recupera o preço
                );
                produtos.add(produto); // Adiciona o produto à lista
            }
            System.out.println("📜 Todos os produtos foram listados com sucesso!");
        } catch (SQLException e) {
            System.out.println("😓 Oops! Ocorreu um erro ao tentar listar os produtos. Erro: " + e.getMessage());
            e.printStackTrace();
        }
        return produtos;
    }

    // Método para atualizar as informações de um produto no banco de dados
    @Override
    public void atualizar(Produto entidade) {
        String sql = "UPDATE produtos SET nome = ?, preco = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidade.getNome()); // Define o nome a ser atualizado
            stmt.setDouble(2, entidade.getPreco()); // Define o preço a ser atualizado
            stmt.setInt(3, entidade.getId()); // Define o ID do produto a ser atualizado
            stmt.executeUpdate(); // Executa a atualização
            System.out.println("🔄 Produto " + entidade.getNome() + " atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("😓 Oops! Algo deu errado ao tentar atualizar o produto. Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para obter um produto pelo ID no banco de dados
    @Override
    public Produto obterPorID(Integer id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Define o ID do produto a ser consultado
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto(
                        rs.getInt("id"), // Recupera o ID
                        rs.getString("nome"), // Recupera o nome
                        rs.getDouble("preco") // Recupera o preço
                    );
                    System.out.println("🔍 Produto com ID " + id + " encontrado: " + produto.getNome());
                    return produto; // Retorna o produto encontrado
                }
            }
        } catch (SQLException e) {
            System.out.println("😓 Oops! Ocorreu um erro ao tentar obter o produto. Erro: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("⚠️ Não encontramos produto com ID " + id + "...");
        return null; // Retorna null caso o produto não seja encontrado
    }
}
