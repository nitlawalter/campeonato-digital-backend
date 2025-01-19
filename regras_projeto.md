### Prompt para Especificar o Desenvolvimento do Projeto

---

**Projeto**: Sistema de Gerenciamento de Campeonatos de Futebol Digital  
**Descrição**: Este sistema permite a administração de campeonatos com fases de grupos e eliminatórias, incluindo funcionalidades como inscrição de times, registro de resultados, geração de confrontos, e relatórios consolidados. Além disso, inclui notificações em tempo real via WebSockets.

---

### **Especificação do Backend**

#### **Tecnologias Utilizadas**
- **Linguagem**: Java 17 ou superior.
- **Framework**: Spring Boot.
- **Banco de Dados**: PostgreSQL.
- **Migrações de Banco**: Flyway.
- **Comunicação em Tempo Real**: WebSockets (STOMP).
- **Gerenciamento de Dependências**: Maven.

#### **Funcionalidades Implementadas**
1. **Gestão de Campeonatos**:
   - Criação, atualização, e exclusão de campeonatos.
   - Configuração de número de grupos e times por campeonato.

2. **Inscrição de Times**:
   - Jogadores podem inscrever seus times em campeonatos abertos.
   - Controle do número máximo de inscrições permitidas.

3. **Geração de Grupos e Partidas**:
   - Distribuição automática de times nos grupos.
   - Geração de confrontos para as fases eliminatórias com chaveamento fixo (1º do Grupo A x 2º do Grupo B).

4. **Registro de Resultados**:
   - Gols marcados e sofridos por time.
   - Atualização da classificação dos grupos e definição dos times classificados.

5. **Fases Eliminatórias**:
   - Geração de partidas automáticas para oitavas, quartas, semifinais e final.
   - Registro de resultados e avanço de times.

6. **Relatórios Consolidados**:
   - Relatório de gols (marcados, sofridos, saldo de gols).
   - Classificação final dos grupos.
   - Histórico consolidado do campeonato (fases, partidas e resultados).

7. **Notificações em Tempo Real**:
   - Registro de resultados e eventos importantes transmitidos via WebSocket para os usuários conectados.

#### **Endpoints Criados**
1. **Campeonatos**:
   - `POST /api/campeonatos`: Criar um novo campeonato.
   - `GET /api/campeonatos`: Listar todos os campeonatos.
   - `PUT /api/campeonatos/{id}`: Atualizar um campeonato.
   - `DELETE /api/campeonatos/{id}`: Excluir um campeonato.

2. **Inscrições**:
   - `POST /api/campeonatos/{id}/inscricoes`: Inscrever um time.
   - `GET /api/campeonatos/{id}/inscricoes`: Listar inscrições de um campeonato.

3. **Fases**:
   - `POST /api/campeonatos/{id}/fases`: Gerar próxima fase.
   - `GET /api/campeonatos/{id}/fases`: Listar fases de um campeonato.

4. **Partidas**:
   - `GET /api/fases/{id}/partidas`: Listar partidas de uma fase.
   - `PUT /api/partidas/{id}/resultado`: Registrar resultado de uma partida.

5. **Relatórios**:
   - `GET /api/campeonatos/{id}/relatorios/gols`: Relatório de gols.
   - `GET /api/campeonatos/{id}/relatorios/classificacao`: Relatório de classificação.
   - `GET /api/campeonatos/{id}/relatorios/historico`: Histórico consolidado.

6. **WebSocket**:
   - Endpoint: `/ws` com tópico `/topic/resultados`.

