package br.com.ucsal.controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.reflections.Reflections;

import br.com.ucsal.annotations.Rota;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebListener
public class InicializadorListener implements ServletContextListener {

	private Map<String, Command> commands = new HashMap<>(); // Mapa para armazenar as rotas e seus comandos

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("⚙️ Preparando a máquina: inicializando rotas...");

		try {
			// Usando Reflections para buscar classes no pacote br.com.ucsal.controller
			// LEMBRAR: O Reflections vai "varrer" o pacote e encontrar todas as classes que possuem
			// a anotação @Rota.
			// Basicamente, ele faz uma busca dinâmica pelas classes no pacote especificado,
			// sem precisar declarar uma lista manualmente.
			Reflections reflections = new Reflections("br.com.ucsal.controller");
			Set<Class<?>> classesAnotadas = reflections.getTypesAnnotatedWith(Rota.class);

			// Se não encontrar nenhuma classe anotada com @Rota, avisa
			if (classesAnotadas.isEmpty()) {
				System.out.println("❌ Ops! Não encontramos classes com @Rota. Verifique o pacote.");
			}

			// Loop para processar todas as classes encontradas com a anotação @Rota
			for (Class<?> classe : classesAnotadas) {
				System.out.println("🔍 Classe anotada localizada: " + classe.getName());
				Rota rota = classe.getAnnotation(Rota.class); // Pega a anotação @Rota da classe

				// Se a classe não implementar Command, dá erro
				if (!Command.class.isAssignableFrom(classe)) {
					throw new IllegalArgumentException(
							"❌ A classe " + classe.getName() + " precisa implementar Command!");
				}

				// Cria uma instância do Command a partir da classe e registra a rota
				Command commandInstance = (Command) classe.getDeclaredConstructor().newInstance();
				commands.put(rota.value(), commandInstance); // Adiciona a rota e o comando ao mapa
				System.out.println("✅ Rota registrada: " + rota.value() + " -> " + classe.getName());
			}

			// Armazena o mapa de comandos no contexto da aplicação para poder acessar de
			// outros lugares
			sce.getServletContext().setAttribute("commands", commands);
			System.out.println("🚀 Rotas prontas para ação!");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("🔥 Erro fatal ao inicializar rotas: " + e.getMessage(), e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		commands.clear(); // Limpa o mapa de comandos quando o contexto é destruído
		System.out.println("🧹 Limpeza completa: rotas foram encerradas.");
	}
}
