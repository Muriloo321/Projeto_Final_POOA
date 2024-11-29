package br.com.ucsal.persistencia;

import br.com.ucsal.annotations.logic.SingletonLoader;

public class PersistenciaFactory {

    public static final int TIPO_MEMORIA = 0;
    public static final int TIPO_HSQL = 1;

    public static ProdutoRepository<?, ?> getProductRepository(int repoType) {
        switch (repoType) {
            case TIPO_MEMORIA:
                System.out.println("Fábrica: Criando repositório em memória.");
                return SingletonLoader.carregarSingleton(MemoriaProdutoRepository.class);
            case TIPO_HSQL:
                System.out.println("Fábrica: Criando repositório HSQLDB.");
                return new HSQLProdutoRepository();
            default:
                throw new IllegalArgumentException("Valor inesperado: " + repoType);
        }
    }
}
