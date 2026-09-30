<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 一覧選択</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 600px; padding-top: 20px;">
        <header style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
            <div>
                <h1 style="font-size: 1.6rem; color: var(--primary); display: flex; align-items: center; gap: 8px;">
                    <span>⚙️</span> 一覧選択
                </h1>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn-danger" style="padding: 8px 18px; font-weight: bold; border-radius: 6px;">ログアウト</a>
            </div>
        </header>

        <div class="card" style="text-align: center; padding: 40px 24px;">
            <h2 style="font-size: 1.35rem; color: var(--primary); margin-bottom: 32px; font-weight: 900;">管理メニューをお選びください</h2>

            <div style="display: flex; flex-direction: column; gap: 16px; margin-bottom: 32px;">
                <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-primary btn-lg" style="padding: 16px 20px; font-size: 1.1rem; border-radius: 10px;">
                    📦 管理者物資一覧画面へ
                </a>

                <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary btn-lg" style="padding: 16px 20px; font-size: 1.1rem; border-radius: 10px; border-color: var(--accent); color: var(--primary);">
                    🚚 管理者配送一覧画面へ
                </a>
            </div>

            <div>
                <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
            </div>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
