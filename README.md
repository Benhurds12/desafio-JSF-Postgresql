# Gerenciador de Tarefas JSF

## 1. Visão Geral do Projeto

Este é um projeto de Gerenciador de Tarefas desenvolvido para demonstrar a criação de uma aplicação web CRUD (Create, Read, Update, Delete) utilizando JavaServer Faces (JSF) como framework MVC, Java Persistence API (JPA) com Hibernate como provedor de persistência, e PostgreSQL como sistema de gerenciamento de banco de dados.

O projeto permite aos usuários listar, adicionar novas tarefas, editar tarefas existentes (populando um formulário com os dados da tarefa), atualizar tarefas (com uma lógica específica baseada na correspondência do título), marcar tarefas como concluídas, excluir tarefas e filtrar a visualização de tarefas por sua situação.

## 2. Funcionalidades Implementadas

O projeto implementa as seguintes funcionalidades-chave que foram desenvolvidas/discutidas:

* **Listagem de Tarefas:** Exibição de tarefas em uma tabela, com possibilidade de uso de componentes PrimeFaces para melhorias como paginação e ordenação.
* **Adição de Nova Tarefa:** Formulário para inserir os detalhes de uma nova tarefa (título, descrição, responsável, prioridade, deadline, situação).
* **Edição de Tarefa (Carregar no Formulário):**
    * Um botão "Editar" em cada linha da tabela de tarefas.
    * Ao ser clicado, os dados da tarefa selecionada são carregados nos campos do formulário de "Adicionar / Editar Tarefa" para modificação.
* **Atualização de Tarefa:**
    * Um botão "Atualizar" dedicado.
    * A lógica de atualização implementada (conforme solicitado) tenta encontrar uma tarefa existente no banco de dados com base no **título** fornecido no formulário. Se encontrada, essa tarefa é atualizada com os demais dados do formulário.
        * _**Nota Importante:** Esta abordagem de atualização por título tem limitações significativas, futuramente farei alterações para que seja feita apenas por id ._
* **Exclusão de Tarefa:** Permite remover tarefas.
* **Conclusão de Tarefa:** Permite alterar a situação de uma tarefa para "Concluída".
* **Filtragem de Tarefas por Situação:**
    * Um menu dropdown permite ao usuário filtrar a lista de tarefas para exibir "Todas", apenas "Em Andamento" ou apenas "Concluídas".
    * A lógica de filtro é aplicada no `TarefaBean` sobre a lista de tarefas carregada.
* **Interface do Usuário Melhorada com PrimeFaces:**      (PENDENTE!!!!)
    Em breve será atualizada
* **Validações Básicas:** Como campos obrigatórios (ex: título da tarefa).
* **Interações AJAX:** Uso extensivo de AJAX para atualizações dinâmicas da página (atualização da tabela, mensagens, formulário) sem a necessidade de recarregamento completo, proporcionando uma experiência mais fluida.

## 3. Tecnologias Utilizadas

* **Java:** Versão 8 ou superior.
* **JavaServer Faces (JSF):** Framework web (provavelmente JSF 2.x, ex: Mojarra).
* **PrimeFaces:** Biblioteca de componentes UI para JSF (ex: versão 10.0+).
* **Java Persistence API (JPA):** Especificação para persistência (ex: JPA 2.1/2.2).
* **Hibernate:** Implementação JPA (ex: Hibernate 5.x).
* **PostgreSQL:** Banco de dados relacional (ex: versão 12+).
* **Maven ou Gradle:** Para gerenciamento de dependências e build.
* **Servidor de Aplicação Java EE / Servlet Container:** Ex: Apache Tomcat 9+, GlassFish.

## 4. Instruções para Execução em Ambiente Local

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

### 4.1. Pré-requisitos

* **JDK (Java Development Kit):** Versão 8 ou superior.
* **Maven (ou Gradle):** Instalado e configurado.
* **PostgreSQL Server:** Instalado, em execução e acessível.
* **IDE Java:** IntelliJ IDEA, Eclipse, etc. (opcional, mas recomendado).
* **Servidor de Aplicação:** Configurado na sua IDE ou standalone.

### 4.2. Configuração do Banco de Dados PostgreSQL

1.  **Crie o Banco de Dados:**
    Se ainda não existir, crie um banco de dados para a aplicação (ex: `gerenciador_tarefas_db`).
    ```sql
    CREATE DATABASE gerenciador_tarefas_db;
    ```
2.  **Crie um Usuário (Recomendado):**
    Crie um usuário com permissões para acessar este


### Itens Solicitados 

s itens:
a) Criar uma aplicação Java Web utilizando JavaServer Faces (JSF) ✅ 
b) Utilizar persistência em um banco de dados PostgreSQL. ✅ 
Itens opcionais, mas que se feitos mostram um diferencial:
c) Utilizar JPA ✅ 
d) Utilizar testes de unidades ❌ (PENDENTE)
e) Publicar projeto no heroku ou outro ambiente cloud. ❌ (NÃO CONSEGUI PUBLICAR NO HEROKO AINDA)
f) outros diferenciais que julgar conveniente. ❓ Acho que usar o primefaces ou similar seria o principal upgrade.
