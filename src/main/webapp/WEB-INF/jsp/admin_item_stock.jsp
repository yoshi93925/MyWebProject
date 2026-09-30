<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 物資入荷</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 600px;">
        <header>
            <div>
                <h1>📦 物資入荷</h1>
            </div>
            <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-secondary btn-icon" title="物資一覧へ戻る" aria-label="物資一覧へ戻る">🔙</a>
        </header>

        <div class="card">
            <h3 class="card-title">ℹ️ 対象物資の情報</h3>

            <table style="width: 100%; margin-bottom: 24px;">
                <tr>
                    <th style="width: 140px;">品目名</th>
                    <td style="font-weight: bold; font-size: 1.05rem;"><c:out value="${item.itemName}"/></td>
                </tr>
                <tr>
                    <th>カテゴリ</th>
                    <td><span class="badge badge-gray"><c:out value="${item.genreName}"/></span></td>
                </tr>
                <tr>
                    <th>現在の在庫数</th>
                    <td style="font-weight: 900; font-size: 1.15rem; color: var(--primary);"><c:out value="${item.stock}"/> 個</td>
                </tr>
                <tr>
                    <th>保管場所</th>
                    <td><c:out value="${item.location}"/></td>
                </tr>
            </table>

            <c:if test="${not empty errorMessage and empty modalAction}">
                <div class="inline-error">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/item/stock" method="post">
                <input type="hidden" name="item_id" value="<c:out value='${item.itemId}'/>">

                <div class="form-group">
                    <label for="quantity">入荷（加算）数量 <span style="color:var(--danger);">*</span></label>
                    <input type="number" name="quantity" id="quantity" class="form-control" placeholder="入荷する数量を入力" min="1" required autofocus>
                </div>

                <div class="action-bar">
                    <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-secondary btn-icon" title="物資一覧へ戻る" aria-label="物資一覧へ戻る">🔙</a>
                    <button type="submit" class="btn btn-primary" style="padding: 12px 28px; font-size: 1.05rem;">
                        📦 入荷実行
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
