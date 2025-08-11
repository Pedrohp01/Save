📚 Save My Studies
Save My Studies é um aplicativo para organização e acompanhamento de estudos, pensado para ajudar estudantes a gerenciar seus cronogramas e metas de forma prática e intuitiva.
Ele possui dois modos principais:

ENEM Mode — voltado para estudantes que estão se preparando para o Exame Nacional do Ensino Médio, com simulados, provas anteriores e questionários.

Programming Mode — focado no aprendizado de programação, com módulos, exercícios e acompanhamento de progresso.

🚀 Funcionalidades
Funcionalidades Gerais
Cadastro e autenticação de usuários (com opção de login social no futuro).

Perfil do usuário com progresso, metas e histórico de estudos.

Cronograma interativo e personalizável.

Notificações e lembretes automáticos.

Relatórios de desempenho.

Modo ENEM
Questionários por matéria.

Provas anteriores do ENEM para treino.

Simulados com cronômetro.

Estatísticas de acertos e erros.

Revisão baseada nas matérias com menor desempenho.

Modo Programação
Organização por linguagens e tecnologias.

Exercícios práticos.

Registro de projetos pessoais.

Revisões e quizzes rápidos.

Sugestão de conteúdo para próximos estudos.

🛠️ Tecnologias Utilizadas
Backend: Java + Spring Boot

Banco de Dados: MySQL (ou PostgreSQL)

Frontend: JavaScript (ou React, futuramente)

Build Tool: Maven

Versionamento: Git & GitHub

📂 Estrutura do Projeto (Backend)
bash
Copiar
Editar
save-my-studies/
│
├── src/main/java/com/savemystudies/
│   ├── controller/       # Controladores REST
│   ├── dto/              # Objetos de Transferência de Dados
│   ├── entity/           # Entidades JPA
│   ├── repository/       # Interfaces de acesso ao banco
│   ├── service/          # Regras de negócio
│   └── exception/        # Tratamento de exceções
│
├── src/main/resources/
│   ├── application.properties  # Configurações
│   └── data.sql / schema.sql   # Scripts iniciais (opcional)
│
└── pom.xml               # Configurações do Maven
⚙️ Como Rodar o Projeto
Pré-requisitos:

Java 17+

Maven

MySQL ou PostgreSQL

Git

Clonar o repositório:

bash
Copiar
Editar
git clone https://github.com/seu-usuario/save-my-studies.git
Configurar o banco de dados no application.properties:

properties
Copiar
Editar
spring.datasource.url=jdbc:mysql://localhost:3306/savemystudies
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
Rodar o projeto:

bash
Copiar
Editar
mvn spring-boot:run
📌 Futuras Melhorias
Autenticação com Google e redes sociais.

Modo offline.

Aplicativo mobile (React Native ou Flutter).

Integração com IA para sugestões personalizadas de estudo.

Dashboard com gráficos interativos.

📜 Licença
Feito por Pedro Henrique para expotec 2025
