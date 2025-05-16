# Sistema de Gestão Infomaz

O Sistema de Gestão Infomaz é uma aplicação Java desenvolvida para análise de dados comerciais, 
que processa informações provenientes de planilhas Excel relacionadas a vendas, estoque, clientes e fornecedores. 
A plataforma gera relatórios estratégicos detalhados que auxiliam gestores na tomada de decisões mais assertivas,
promovendo uma visão integrada e eficiente do negócio.

---

## Funcionalidades

### Análise de Dados
- Margem de Lucro: Cálculo por produto e categoria
- Médias por Categoria: Valor médio de venda por categoria mensal
- Vendas por Categoria: Totalização de vendas agrupadas por categoria

### Rankings Estratégicos
- Clientes: Ordenação por volume de compras mensal
- Fornecedores: Classificação por quantidade fornecida
- Margem de Lucro: Por categoria de produto
- **Produtos**: 
  - Por quantidade vendida
  - Por valor total vendido
  - Por quantidade em estoque

### Relacionamentos Comerciais
- Produtos por Cliente: Listagem completa de compras por cliente
- Estoque-Produto-Fornecedor: Integração completa dos dados

---

## Tecnologias Utilizadas

### Backend
- Java 8+: Linguagem principal
- Apache POI: Processamento de arquivos Excel
- Collections Framework: Manipulação eficiente de dados
- Stream API: Processamento de dados em paralelo
- **Estrutura**
- Padrão MVC: Separação clara de responsabilidades
- Injeção de Dependência: Manual para serviços
- POJOs: Modelos de dados bem definidos

---

## COMO EXECUTAR O PROGRAMA?
### Pré-requisitos
- JDK 8 ou superior
- Apache NetBeans 13 ou superior
- Arquivo Excel no formato especificado (já fornecido)

- **Abrir o NetBeans**
- Inicie o NetBeans normalmente.

- **Importar o Projeto**
- Vá em File > Open Project (Arquivo > Abrir Projeto).
- Navegue até a pasta do seu projeto e selecione-o (a pasta deve conter um arquivo nbproject).

- **Executar o Programa**
- Clique com o botão direito no projeto > Run (ou pressione F6).
- A saída será exibida no terminal do NetBeans.

---
