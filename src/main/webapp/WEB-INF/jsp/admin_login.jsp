<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 管理者ログイン</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 480px; padding-top: 40px;">
        <header style="justify-content: center; text-align: center; border-bottom: none; margin-bottom: 20px;">
            <div>
                <h1>⚙️ 管理者ログイン</h1>
            </div>
        </header>

        <div class="card" style="padding: 32px 28px;">
            <c:if test="${not empty errorMessage and empty modalAction}">
                <div class="inline-error">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/login" method="post">
                <div class="form-group">
                    <label for="name">管理者名</label>
                    <input type="text" name="name" id="name" class="form-control" placeholder="例: admin" value="<c:out value='${name}'/>" required autofocus>
                </div>

                <div class="form-group">
                    <label for="password">パスワード</label>
                    <input type="password" name="password" id="password" class="form-control" placeholder="パスワードを入力" required>
                </div>

                <button type="submit" class="btn btn-primary btn-lg" style="margin-top: 8px;">
                    ログイン
                </button>
            </form>
        </div>

        <div style="text-align: center; margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
