#!/bin/bash
HEADER_CONTENT_TYPE="Content-Type: application/json"
HEADER_ACCEPT="Accept-Encoding: ASCII"

function list_all_todos {
    # Lista todas as tarefas
    # Exemplo:
    # curl -X GET -H "Content-Type: application/json" -H "Accept-Encoding: UTF-8" http://localhost:8080/todo
    echo "URI (GET): " $TODOURI
    echo "LISTA 'todos'"
    curl -X GET -H "${HEADER_ACCEPT}" -H "${HEADER_CONTENT_TYPE}" ${URL}${TODOURI} 
    echo
}

function get_todo(){
    # Apaga uma tarefa
    # Exemplo:
    # curl -i -X GET http://192.168.1.3:8080/todo/1
    id=$1
    echo "URI (GET): " $TODOURI/$id
    echo "GET 'todo' id=$id"
    curl -X GET ${URL}${TODOURI}/$id
    echo 
}

function create_todo {
    # cria uma tarefa
    # Exemplo:
    # curl -i -X POST -H "Content-Type: application/json" -H "Accept-Encoding: UTF-8" -d "{  \"title\": \"Teste 4\", \"status\": \"pending\" }" http://localhost:8080/todo
    title=$1
    status=$2
    echo "URI (POST): " $TODOURI
    echo "CRIA 'todo' titulo $title"
    XML="{ \"title\": \"$title\", \"status\": \"$status\" }"
    echo $XML
    curl -X POST -H "${HEADER_ACCEPT}" -H "${HEADER_CONTENT_TYPE}" ${URL}${TODOURI} -d "$XML" ${URL}${TODOURI}
    echo
}

function upsert_todo {
    # atualiza uma tarefa se o id (sorteado de 1 a 10) existir
    # Exemplo:
    # curl -i -X PUT -H "Content-Type: application/json" -H "Accept-Encoding: UTF-8" -d "{ \"id\": 4, \"title\": \"Novo titulo\", \"status\": \"completed\" }" http://192.168.1.3:8080/todo/4
    id=$1
    title=$2
    status=$3
    echo "URI (PUT): " $TODOURI/$id
    echo "UPSERT 'todo' titulo Teste $id"
    # Sorteia um ID de 1 a 10
    XML="{ \"id\": \"$id\", \"title\": \"$title\", \"status\": \"$status\" }"
    echo $XML
    curl -X PUT -H "${HEADER_ACCEPT}" -H "${HEADER_CONTENT_TYPE}" ${URL}${TODOURI} -d "$XML" ${URL}${TODOURI}/$id
    echo
}

function delete_todo(){
    # Apaga uma tarefa
    # Exemplo:
    # curl -i -X DELETE http://192.168.1.3:8080/todo/1
    id=$1
    echo "URI (DELETE): " $TODOURI/$id
    echo "APAGA 'todo' id=$id"
    curl -X DELETE ${URL}${TODOURI}/$id
    echo
}

function get_method_metrics {
    # Verifica metricas
    # Exemplo:
    # curl -i -X GET http://192.168.1.3:8080/metrics
    # curl -i -X GET http://192.168.1.3:8080/metrics/method.timed
    id=$1
    echo "URI (GET): " /metrics/method.timed
    echo "GET 'method metric"
    curl -X GET ${URL}/metrics/method.timed
    echo
}

function get_health {
    # Verifica status
    # Exemplo:
    # curl -i -X GET http://192.168.1.3:8080/health
    # curl -i -X GET http://192.168.1.3:8080/
    id=$1
    echo "URI (GET): " /health 
    echo "GET 'method health"
    curl -X GET ${URL}/health
    echo
}

###################################################################

PROGNAME=`basename $0`

if [ ! $# -eq 2 ] ; then
  echo "Uso: $PROGNAME IP PORTA";
  exit 0;
fi

IP=$1
PORT=$2
URL=http://$IP:$PORT
TODOURI=/todo

# LISTA
echo "=========================================================="
echo "Lista todas as tarefas"
list_all_todos
echo

# INSERE (falha e ok)
echo "=========================================================="
echo "Cria uma tarefa com dados OK"
create_todo "Status_OK" "pending"
echo
echo "Cria uma tarefa com status inexistente (permitido: pending ou completed)"
create_todo "Status_ERR" "erro"

# APAGA
echo "=========================================================="
# apaga id=1
echo "Apaga todo id=1"
delete_todo 1
echo "Verifica se apagou (consulta)"
get_todo 1
echo "=========================================================="

# ALTERA (id sorteado de 1 a 10)
id=$(( $RANDOM % 10 + 1 ))
title="Alterado"
stat="completed"
echo "Atualiza todo id='$id', título='$title', status='$stat'"
echo "CONSULTA ESTADO ANTERIOR"
get_todo $id
echo "ATUALIZANDO"
upsert_todo $id $title $stat
echo "VERIFICANDO ATUALIZAÇÃO/INSERÇÃO"
get_todo $id
echo "=========================================================="

# MÉTRICAS e SANIDADE
# Metricas dos métodos monitorados
echo "Métrica"
get_method_metrics
echo "=========================================================="
echo "Sanidade do sistema"
get_health
echo "=========================================================="
echo "FIM"
echo "Verifique o arquivo todo.log (criado quando perfil=produção)"

