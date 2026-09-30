<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 配送状況確認</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 720px;">
        <header>
            <div>
                <h1>🔍 配送状況確認</h1>
            </div>
            <div style="display: flex; gap: 8px;">
                <a href="${pageContext.request.contextPath}/order/status?order_no=<c:out value='${order.orderNo}'/>" class="btn btn-secondary btn-icon" title="画面更新" aria-label="画面更新">🔄</a>
                <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
            </div>
        </header>

        <div class="card" style="display: flex; justify-content: space-between; align-items: center; background: var(--primary-light); border-color: var(--accent);">
            <div>
                <div style="font-size: 0.85rem; color: var(--text-muted);">注文番号: <c:out value="${order.orderNo}"/> | 申請日時: <c:out value="${order.orderedAtFormatted}"/></div>
                <div style="font-size: 1.3rem; font-weight: bold; margin-top: 4px; color: var(--primary);">
                    📍 <c:out value="${order.address}"/>
                </div>
            </div>
            <div>
                <c:choose>
                    <c:when test="${order.deliveryStatus == '未対応'}">
                        <span class="badge badge-warning" style="font-size: 1rem; padding: 8px 16px;">未対応（準備中）</span>
                    </c:when>
                    <c:when test="${order.deliveryStatus == '対応中'}">
                        <span class="badge badge-info" style="font-size: 1rem; padding: 8px 16px;">配送中 (担当: <c:out value="${order.deliveryStaff}"/>)</span>
                    </c:when>
                    <c:when test="${order.deliveryStatus == '完了'}">
                        <span class="badge badge-success" style="font-size: 1rem; padding: 8px 16px;">配送完了</span>
                    </c:when>
                    <c:when test="${order.deliveryStatus == '配送不可'}">
                        <span class="badge badge-danger" style="font-size: 1rem; padding: 8px 16px;">配送不可</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-gray" style="font-size: 1rem; padding: 8px 16px;"><c:out value="${order.deliveryStatus}"/></span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="card">
            <h3 class="card-title">👤 注文者エリア</h3>
            <table style="width: 100%;">
                <tr>
                    <th style="width: 130px;">お名前</th>
                    <td><c:out value="${order.name}"/> 様</td>
                </tr>
                <tr>
                    <th>お届け先住所</th>
                    <td><c:out value="${order.address}"/></td>
                </tr>
                <tr>
                    <th>電話番号</th>
                    <td><a href="tel:<c:out value='${order.phone}'/>" style="color: var(--accent);"><c:out value="${order.phone}"/></a></td>
                </tr>
                <tr>
                    <th>備考</th>
                    <td><c:out value="${empty order.note ? 'なし' : order.note}"/></td>
                </tr>
                <c:if test="${order.deliveryStatus == '配送不可'}">
                    <tr>
                        <th style="color: var(--danger);">配送不可理由</th>
                        <td style="color: var(--danger); font-weight: bold;"><c:out value="${order.notdeliveryNote}"/></td>
                    </tr>
                </c:if>
            </table>
        </div>

        <div class="card">
            <h3 class="card-title">📦 申請物資明細</h3>
            <table>
                <thead>
                    <tr>
                        <th>品名</th>
                        <th style="width: 100px; text-align: center;">数量</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${order.details}">
                        <tr>
                            <td><c:out value="${detail.itemName}"/></td>
                            <td style="text-align: center; font-weight: bold;"><c:out value="${detail.quantity}"/> 個</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
            <c:if test="${order.deliveryStatus == '未対応'}">
                <button type="button" class="btn btn-danger" onclick="openCancelModal();" style="padding: 12px 24px; font-weight: bold;">
                    ❌ 申請を取り消す
                </button>
            </c:if>
        </div>
    </div>

    <!-- 申請取り消し確認モーダル -->
    <c:if test="${order.deliveryStatus == '未対応'}">
        <div id="cancelModal" class="modal" style="display: none;">
            <div class="modal-content">
                <h3 style="color: var(--danger); margin-bottom: 12px; display: flex; align-items: center; gap: 8px;">
                    <span>⚠️</span> 申請取り消しの確認
                </h3>
                <p style="margin-bottom: 16px; font-size: 1rem; color: var(--text);">
                    以下の申請を取り消しますか？取り消すと物資は配送されません。
                </p>

                <!-- 取り消し対象の明細表示 -->
                <div style="background-color: var(--bg); border: 1px solid var(--border); border-radius: 8px; padding: 12px; margin-bottom: 24px; max-height: 200px; overflow-y: auto;">
                    <div style="font-weight: bold; margin-bottom: 8px; font-size: 0.9rem;">取り消し対象物資:</div>
                    <table style="width: 100%;">
                        <c:forEach var="detail" items="${order.details}">
                            <tr>
                                <td style="padding: 4px 8px; font-size: 0.9rem;"><c:out value="${detail.itemName}"/></td>
                                <td style="padding: 4px 8px; text-align: right; font-weight: bold; font-size: 0.9rem;"><c:out value="${detail.quantity}"/> 個</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>

                <form action="${pageContext.request.contextPath}/order/cancel" method="post">
                    <input type="hidden" name="order_id" value="<c:out value='${order.orderId}'/>">
                    <div style="display: flex; justify-content: flex-end; gap: 12px;">
                        <button type="button" class="btn btn-secondary" onclick="closeCancelModal();">取り消さない</button>
                        <button type="submit" class="btn btn-danger">取り消す</button>
                    </div>
                </form>
            </div>
        </div>
    </c:if>

    <%@ include file="common_error_modal.jsp" %>

    <script>
        function openCancelModal() {
            var m = document.getElementById('cancelModal');
            if (m) m.style.display = 'flex';
        }
        function closeCancelModal() {
            var m = document.getElementById('cancelModal');
            if (m) m.style.display = 'none';
        }
    </script>
</body>
</html>
