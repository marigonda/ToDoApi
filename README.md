
Projeto Teste: TODO-LIST API (API para armazenamento/leitura de tarefas)
 
-Propõe um exemplo de RESTFUL API simples em java que armazene e atualize tarefas (TODO LIST API). 
(ex: GET, PUT, POST, DELETE /todo)
 
Informações sobre requisitos da API:
  - Toda tarefa possui um status (pending ou completed)
  - A API persiste os dados em um banco de dados
  - A API disponibiliza uma rota para listagem das tarefas e seu status (GET /todo)
  - A API fornece uma rota para validar o funcionamento de seus componentes (GET /healthcheck)
  - A API fornece uma rota com indicadores de performance da API (ex: volume de requisições atendidas,
    tempo médio de serviço em milisegundos, etc) (GET /metrics)
  - Não contempla o Frontend para input dos dados na API. (Pode-se fazer também de forma simples utilizando SWAGER)
 
Descrição geral do Ambiente
===========================
 - Desenvolvida em JAVA com o framework Spring Boot 2 (https://spring.io/projects/spring-boot)
 - Banco de dados H2 (em memória) com abstração (CRUD) utilizando JPA
 - Instrumentação utilizando Actuator (monitoramento) e métricas colhidas com o agora plugin Micrometer
 - Desenvolvido com o Lombok (automatização de logs, geração de getters/setters, etc)
 - Projeto MAVEN (http://what.is.mvn) desenvolvido na IDE IntelliJ Idea (http://www.jetbrains.com/idea)
 
Pré-requisitos
==============
 - JRE 8 ou superior (incluso no path)
 - Apache Maven 3.2 (se houver interesse em compilar) e JDK correspondente (recomendado jdk 10)
 - curl para a execução do script de testes (recomendado versão 7.54 ou superior)
 
Compilação
==============
 Basta acessar a pasta raiz do projeto e invocar: 
 
 mvn32 clean install
 
 Obs.: O mvn32 deve estar no path. (32 refere-se aqui à versão 3.2)
 
Execução
==============

 Utilizando o maven:
 
 mvn spring-boot:run
 
 Ou ainda, com o java 8 no path, acessar a pasta /bin e executar ToDoApi-0.0.1-SNAPSHOT.jar
 
 java -jar ToDoApi-0.0.1-SNAPSHOT.jar --server.ip=0.0.0.0 --server.port=8080
 
Como utilizar
=============
Para testes e exemplos de utilização verificar o script test_todoAPI.sh desenvolvido em linux shell (bash),
basta executá-lo:

./test_todoAPI.sh localhost 8080

Em linhas gerais, o modelo é o padrão REST, exemplos utilizando curl:

### Lista todos os todo's

curl -X GET -H "Content-Type: application/json" -H "Accept-Encoding: UTF-8" http://localhost:8080/todo

### Obtém o todo ou task cujo id é 1

curl -i -X GET http://localhost:8080/todo/1

### Inclui uma tarefa

curl -i -X POST -H "Content-Type: application/json" -H "Accept-Encoding: UTF-8" -d "{  \"title\": \"Teste 4\", \"status\": \"pending\" }" http://localhost:8080/todo

Formato do arquivo JSON a submeter

{  "title": "TÍTULO", "status": "pending / completed" }

### Atualiza uma tarefa (se id fornecido existir, do contrário inclui)

Atualiza a task id=4 

curl -i -X PUT -H "Content-Type: application/json" -H "Accept-Encoding: UTF-8" -d "{ \"id\": 4, \"title\": \"Novo titulo\", \"status\": \"completed\" }" http://localhost:8080/todo/4

JSON

{ "id": 4, "title": "Novo titulo", "status": "pending OU completed" }

### Apaga id 1

curl -i -X DELETE http://localhost:8080/todo/1

### Métricas diversas (Actuator/Micrometer)

curl -i -X GET http://localhost:8080/metrics

curl -i -X GET http://localhost:8080/metrics/method.timed

### Monitoração (status do banco, memória em uso, dentre outras)

curl -i -X GET http://localhost:8080/health

Problemas conhecidos
====================
Há um problema com processamento em duplicidade de uma submissão válida após uma inválida gerando exceção HttpMessageNotReadableException.
Foram realizados testes retirando o pré e o pós processamento das submissões (inclusas para efetuar log personalizado)
e também com captura da exceção no controller, porém nenhuma destas duas abordagens surtiu efeito.

Resolução de problemas
======================
Tudo deve simplesmente funcionar... porém em caso de problemas recomenda-se verificar o log (todo.log gerado
quando invocado com o maven - perfil produção ou o console quando invocado com o java -jar
Verificar ainda as dependências (versões do java e maven) e se necessário o desenvolvedor coloca-se à disposição
