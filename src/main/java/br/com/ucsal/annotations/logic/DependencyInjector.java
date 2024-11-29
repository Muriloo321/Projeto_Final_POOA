package br.com.ucsal.annotations.logic;

import java.lang.reflect.Field;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.model.Produto;
import br.com.ucsal.persistencia.PersistenciaFactory;
import br.com.ucsal.persistencia.ProdutoRepository;
import br.com.ucsal.service.ProdutoService;

public class DependencyInjector {

    @SuppressWarnings("unchecked")
    public static void injectDependencies(Object target) {
        Class<?> clazz = target.getClass();

        // Itera sobre todos os campos da classe
        for (Field field : clazz.getDeclaredFields()) {
            // Confere se o campo está anotado com @Inject.
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true); // Torna o campo acessível

                try {
                    Object dependency = null;

                    // Realiza a injeção de dependência a partir do tipo do campo.
                    if (field.getType().equals(ProdutoService.class)) {
                        // Define o tipo de repositório de forma dinâmica.
                        int repositoryType = PersistenciaFactory.TIPO_MEMORIA; 
                        ProdutoRepository<Produto, Integer> repository =
                                (ProdutoRepository<Produto, Integer>) PersistenciaFactory.getProductRepository(repositoryType);

                        // Gera uma instância do ProdutoService utilizando o repositório correspondente.
                        dependency = new ProdutoService(repository);
                    } else {
                        throw new IllegalArgumentException(
                                "Esse tipo de dependência não é suportado para a injeção: " + field.getType().getName()
                        );
                    }

                    // Aqui abaixo é onde é realizada a injeção da dependência no campo.
                    field.set(target, dependency);
                    System.out.println("Injeção bem-sucedida! Dependência injetada no campo: " + field.getName()); 
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Falha ao injetar a dependência no campo: " + field.getName(), e);
                } catch (Exception e) {
                    throw new RuntimeException("Ocorreu um erro inesperado durante a injeção: " + e.getMessage(), e);
                }
            }
        }
    }
}
