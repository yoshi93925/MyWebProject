<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 管理者配送詳細</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <div>
                <h1>📦 管理者配送詳細</h1>
            </div>
            <a href="${pageContext.request.contextPath}/admin/orders?tab=<c:out value='${currentTab}'/>" class="btn btn-secondary btn-icon" title="管理者配送一覧へ戻る" aria-label="管理者配送一覧へ戻る">🔙</a>
        </header>

        <div class="card" style="display: flex; justify-content: space-between; align-items: center; background: var(--primary-light);">
            <div>
                <span style="font-size: 0.85rem; color: var(--text-muted);">注文番号: <c:out value="${order.orderNo}"/> | 申請日時: <c:out value="${order.orderedAtFormatted}"/></span>
                <div style="font-size: 1.3rem; font-weight: bold; color: var(--primary); margin-top: 4px;">
                    📍 <c:out value="${order.address}"/>
                </div>
            </div>
            <div>
                <c:choose>
                    <c:when test="${order.deliveryStatus == '未対応'}">
                        <span class="badge badge-warning" style="font-size: 1rem; padding: 6px 14px;">未対応</span>
                    </c:when>
                    <c:when test="${order.deliveryStatus == '対応中'}">
                        <span class="badge badge-info" style="font-size: 1rem; padding: 6px 14px;">対応中</span>
                    </c:when>
                    <c:when test="${order.deliveryStatus == '完了'}">
                        <span class="badge badge-success" style="font-size: 1rem; padding: 6px 14px;">完了</span>
                    </c:when>
                    <c:when test="${order.deliveryStatus == '配送不可'}">
                        <span class="badge badge-danger" style="font-size: 1rem; padding: 6px 14px;">配送不可</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-gray" style="font-size: 1rem; padding: 6px 14px;"><c:out value="${order.deliveryStatus}"/></span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="card">
            <h3 class="card-title">👤 注文者エリア</h3>
            <table style="width: 100%;">
                <tr>
                    <th style="width: 140px;">お名前</th>
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
                <tr>
                    <th>配送担当者</th>
                    <td><c:out value="${empty order.deliveryStaff ? '未定' : order.deliveryStaff}"/></td>
                </tr>
                <c:if test="${order.deliveryStatus == '配送不可'}">
                    <tr>
                        <th style="color: var(--danger);">配送不可理由</th>
                        <td style="color: var(--danger);"><c:out value="${order.notdeliveryNote}"/></td>
                    </tr>
                </c:if>
            </table>
        </div>

        <div class="card">
            <h3 class="card-title">📦 品名と数量エリア</h3>
            <table>
                <thead>
                    <tr>
                        <th>品名</th>
                        <th>保管場所</th>
                        <th style="text-align: center; width: 120px;">数量</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${order.details}">
                        <tr>
                            <td><c:out value="${detail.itemName}"/></td>
                            <td><c:out value="${detail.location}"/></td>
                            <td style="text-align: center; font-weight: bold;"><c:out value="${detail.quantity}"/> 個</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/admin/orders?tab=<c:out value='${currentTab}'/>" class="btn btn-secondary btn-icon" title="管理者配送一覧へ戻る" aria-label="管理者配送一覧へ戻る">🔙</a>
            <c:if test="${order.deliveryStatus == '未対応'}">
                <button type="button" class="btn btn-danger" onclick="openDeleteOrderModal();" style="padding: 12px 24px; font-weight: bold;">
                    🗑️ 注文データの削除
                </button>
            </c:if>
        </div>
    </div>

    <c:if test="${order.deliveryStatus == '未対応'}">
        <div id="deleteOrderModal" class="modal" style="display: none;">
            <div class="modal-content">
                <h3 style="color: var(--danger); margin-bottom: 12px; display: flex; align-items: center; gap: 8px;">
                    <span>🗑️</span> 注文データの削除確認
                </h3>
                <p style="margin-bottom: 24px; font-size: 1rem; color: var(--text);">
                    この注文データを削除しますか？<br>
                    （該当物資の在庫数は自動で復元されます）
                </p>
                <form action="${pageContext.request.contextPath}/admin/order/delete" method="post">
                    <input type="hidden" name="order_id" value="<c:out value='${order.orderId}'/>">
                    <input type="hidden" name="tab" value="<c:out value='${currentTab}'/>">
                    <div style="display: flex; justify-content: flex-end; gap: 12px;">
                        <button type="button" class="btn btn-secondary" onclick="closeDeleteOrderModal();">キャンセル</button>
                        <button type="submit" class="btn btn-danger">削除する</button>
                    </div>
                </form>
            </div>
        </div>
    </c:if>

    <%@ include file="common_error_modal.jsp" %>

    <script>
        function openDeleteOrderModal() {
            var m = document.getElementById('deleteOrderModal');
            if (m) m.style.display = 'flex';
        }
        function closeDeleteOrderModal() {
            var m = document.getElementById('deleteOrderModal');
            if (m) m.style.display = 'none';
        }
    </script>
</body>
</html>
