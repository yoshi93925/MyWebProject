<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 申請内容確認</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 680px;">
        <header>
            <div>
                <h1>📋 申請内容確認</h1>
                <div class="subtitle">内容をご確認の上、申請ボタンを押してください</div>
            </div>
            <a href="${pageContext.request.contextPath}/order/address" class="btn btn-secondary btn-icon" title="住所入力画面に戻る" aria-label="住所入力画面に戻る">🔙</a>
        </header>

        <div class="card">
            <h3 class="card-title">📦 申請する物資</h3>
            <table style="margin-bottom: 8px;">
                <thead>
                    <tr>
                        <th>品名</th>
                        <th style="width: 100px; text-align: center;">数量</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cart}">
                        <tr>
                            <td><c:out value="${item.itemName}"/></td>
                            <td style="text-align: center; font-weight: bold;"><c:out value="${item.quantity}"/> 個</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h3 class="card-title">📍 配送先住所</h3>
            <table>
                <tr>
                    <th style="width: 130px;">お名前</th>
                    <td><c:out value="${orderInput.name}"/> 様</td>
                </tr>
                <tr>
                    <th>お届け先住所</th>
                    <td><c:out value="${orderInput.address}"/></td>
                </tr>
                <tr>
                    <th>電話番号</th>
                    <td><a href="tel:<c:out value='${orderInput.phone}'/>" style="color: var(--accent);"><c:out value="${orderInput.phone}"/></a></td>
                </tr>
                <tr>
                    <th>備考</th>
                    <td><c:out value="${empty orderInput.note ? 'なし' : orderInput.note}"/></td>
                </tr>
            </table>
        </div>

        <form action="${pageContext.request.contextPath}/order/confirm" method="post" onsubmit="document.getElementById('submitOrderBtn').disabled = true;">
            <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
            <div class="action-bar">
                <a href="${pageContext.request.contextPath}/order/address" class="btn btn-secondary btn-icon" title="住所入力画面に戻る" aria-label="住所入力画面に戻る">🔙</a>
                <button type="submit" id="submitOrderBtn" class="btn btn-success" style="padding: 14px 28px; font-size: 1.05rem;">
                    🆘 物資リクエストを申請する
                </button>
            </div>
        </form>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
