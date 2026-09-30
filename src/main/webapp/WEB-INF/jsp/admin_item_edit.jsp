<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 物資編集</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 600px;">
        <header>
            <div>
                <h1>✏️ 物資編集</h1>
                <div class="subtitle">物資情報の変更・更新</div>
            </div>
            <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-secondary btn-icon" title="物資一覧へ戻る" aria-label="物資一覧へ戻る">🔙</a>
        </header>

        <div class="card">
            <h3 class="card-title">📝 物資情報の編集 (ID: #<c:out value="${item.itemId}"/>)</h3>

            <c:if test="${not empty errorMessage and empty modalAction}">
                <div class="inline-error">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/item/edit" method="post">
                <input type="hidden" name="item_id" value="<c:out value='${item.itemId}'/>">

                <div class="form-group">
                    <label for="genre_id">カテゴリ（ジャンル） <span style="color:var(--danger);">*</span></label>
                    <select name="genre_id" id="genre_id" class="form-control" required>
                        <option value="1" ${item.genreId == 1 ? 'selected' : ''}>食料・飲料</option>
                        <option value="2" ${item.genreId == 2 ? 'selected' : ''}>衛生・医療用品</option>
                        <option value="3" ${item.genreId == 3 ? 'selected' : ''}>生活・日用品</option>
                        <option value="4" ${item.genreId == 4 ? 'selected' : ''}>防寒・睡眠・衣類</option>
                        <option value="5" ${item.genreId == 5 ? 'selected' : ''}>インフラ・環境整備</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="item_name">品目名 <span style="color:var(--danger);">*</span></label>
                    <input type="text" name="item_name" id="item_name" class="form-control" value="<c:out value='${item.itemName}'/>" required>
                </div>

                <div class="form-group">
                    <label for="stock">現在の在庫数 <span style="color:var(--danger);">*</span></label>
                    <input type="number" name="stock" id="stock" class="form-control" min="0" value="<c:out value='${item.stock}'/>" required>
                </div>

                <div class="form-group">
                    <label for="location">場所（保管場所・倉庫名） <span style="color:var(--danger);">*</span></label>
                    <input type="text" name="location" id="location" class="form-control" value="<c:out value='${item.location}'/>" required>
                </div>

                <div class="action-bar">
                    <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-secondary btn-icon" title="物資一覧へ戻る" aria-label="物資一覧へ戻る">🔙</a>
                    <button type="submit" class="btn btn-primary" style="padding: 12px 28px; font-size: 1.05rem;">
                        💾 保存する
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
