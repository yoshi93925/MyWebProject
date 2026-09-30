<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 住所入力</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 680px;">
        <header>
            <div>
                <h1>📍 住所入力</h1>
                <div class="subtitle">お届け場所とお名前をご入力ください</div>
            </div>
            <a href="${pageContext.request.contextPath}/order" class="btn btn-secondary btn-icon" title="物資申請画面に戻る" aria-label="物資申請画面に戻る">🔙</a>
        </header>

        <div class="card">
            <h3 class="card-title">📝 お届け先情報の入力</h3>

            <c:if test="${not empty errorMessage and empty modalAction}">
                <div class="inline-error">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/order/address" method="post">
                <div class="form-group">
                    <label for="name">お名前 <span style="color:var(--danger);">*</span></label>
                    <input type="text" name="name" id="name" class="form-control" placeholder="例: 山田 太郎" value="<c:out value='${name}'/>" maxlength="20" required>
                </div>

                <div class="form-group">
                    <label for="address">お届け先住所・避難所名・部屋番号 <span style="color:var(--danger);">*</span></label>
                    <input type="text" name="address" id="address" class="form-control" placeholder="例: 第一避難所 体育館 2階" value="<c:out value='${address}'/>" maxlength="255" required>
                </div>

                <div class="form-group">
                    <label for="phone">電話番号（半角数字のみ） <span style="color:var(--danger);">*</span></label>
                    <input type="tel" name="phone" id="phone" class="form-control" placeholder="例: 09012345678" value="<c:out value='${phone}'/>" maxlength="11" pattern="[0-9]{1,11}" required>
                </div>

                <div class="form-group">
                    <label for="note">備考（設置場所や注意点など）</label>
                    <textarea name="note" id="note" class="form-control" rows="3" placeholder="例: 入口近くのテーブルの上に置いてください。"><c:out value='${note}'/></textarea>
                </div>

                <div class="action-bar">
                    <a href="${pageContext.request.contextPath}/order" class="btn btn-secondary btn-icon" title="物資申請画面に戻る" aria-label="物資申請画面に戻る">🔙</a>
                    <button type="submit" class="btn btn-primary" style="padding: 12px 28px; font-size: 1.05rem;">
                        内容確認へ進む ➔
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
