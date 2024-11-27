<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="br.com.ucsal.model.Produto" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulário de Produto</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-top: 20px;
        }

        form {
            width: 300px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        input[type="text"] {
            width: 100%;
            padding: 8px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        .form-container {
            text-align: center;
        }

        .table-container {
            width: 80%;
            margin: 20px auto;
            padding: 10px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        a {
            color: #4CAF50;
            text-decoration: none;
            margin-right: 10px;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<h2>
    <c:choose>
        <c:when test="${not empty produto}">Editar</c:when>
        <c:otherwise>Criar</c:otherwise>
    </c:choose>
</h2>

<%
    Produto produto = (Produto) request.getAttribute("produto");
    String id = "";
    String nome = "";
    String preco = "";
    String action = "adicionarProduto";
    String titulo = "Adicionar Novo Produto";
    String botao = "Adicionar";

    if (produto != null) {
        id = produto.getId().toString();
        nome = produto.getNome();
        preco = String.valueOf(produto.getPreco());
        action = "editarProduto";
        titulo = "Editar Produto";
        botao = "Atualizar";
    }
%>

<div class="form-container">
    <h2><%= titulo %></h2>
    <form action="<c:choose><c:when test='${not empty produto}'>editarServlets</c:when><c:otherwise>adicionarProdutos</c:otherwise></c:choose>" method="post">
        <c:if test="${not empty produto}">
            <input type="hidden" name="id" value="${produto.id}">
        </c:if>
        Nome: <input type="text" name="nome" value="${produto.nome}"/><br/>
        Preço: <input type="text" name="preco" value="${produto.preco}"/><br/>
        <input type="submit" value="<%= botao %>"/>
    </form>
</div>

</body>
</html>
