package br.com.samuca.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    	
    	//Definindo um container em runtime usando a engine do mysql
        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");
        
        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }
        
        //Configurações da conexao do container Mysql Gerado
        private static Map<String, String> createConnectionConfiguration(){
            return Map.of(
                "spring.datasource.url", mysql.getJdbcUrl(),
                "spring.datasource.username", mysql.getUsername(),
                "spring.datasource.password", mysql.getPassword());
        }
        
        //Inicializa o container, chamando o metodo startContainers() 
        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            
            //Obtém o contexto do Spring que está inicializando.
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            //seta as propriedades de conexão
            MapPropertySource testcontaines =
                new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration());
            
            //Vai adicionar as novas propriedades antes de todas as outras
            environment.getPropertySources().addFirst(testcontaines);
        }
        
    }
}
