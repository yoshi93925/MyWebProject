<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 物資配達</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <div>
                <h1>🚚 物資配達</h1>
            </div>
            <div style="display: flex; gap: 8px;">
                <a href="${pageContext.request.contextPath}/volunteer/list?tab=<c:out value='${currentTab}'/>" class="btn btn-secondary btn-icon" title="画面更新" aria-label="画面更新">🔄</a>
                <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
            </div>
        </header>

        <div class="tabs">
            <a href="${pageContext.request.contextPath}/volunteer/list?tab=undelivery" class="tab-link ${currentTab == 'undelivery' ? 'active' : ''}">未対応</a>
            <a href="${pageContext.request.contextPath}/volunteer/list?tab=delivery" class="tab-link ${currentTab == 'delivery' ? 'active' : ''}">対応中</a>
            <a href="${pageContext.request.contextPath}/volunteer/list?tab=complete" class="tab-link ${currentTab == 'complete' ? 'active' : ''}">完了</a>
            <a href="${pageContext.request.contextPath}/volunteer/list?tab=unable" class="tab-link ${currentTab == 'unable' ? 'active' : ''}">配送不可</a>
        </div>

        <c:if test="${empty orders}">
            <div class="card" style="text-align: center; color: var(--text-muted); padding: 40px 20px;">
                該当する配送データはありません。
            </div>
        </c:if>

        <c:forEach var="order" items="${orders}">
            <div class="card" style="position: relative;">
                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px;">
                    <span style="font-size: 0.85rem; color: var(--text-muted);">注文日時: <c:out value="${order.orderedAtFormatted}"/></span>
                    <c:choose>
                        <c:when test="${order.deliveryStatus == '未対応'}">
                            <span class="badge badge-warning">未対応</span>
                        </c:when>
                        <c:when test="${order.deliveryStatus == '対応中'}">
                            <span class="badge badge-info">対応中 (担当: <c:out value="${order.deliveryStaff}"/>)</span>
                        </c:when>
                        <c:when test="${order.deliveryStatus == '完了'}">
                            <span class="badge badge-success">完了</span>
                        </c:when>
                        <c:when test="${order.deliveryStatus == '配送不可'}">
                            <span class="badge badge-danger">配送不可</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-gray"><c:out value="${order.deliveryStatus}"/></span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div style="font-size: 1.2rem; font-weight: 700; margin-bottom: 8px; color: var(--primary);">
                    📍 <c:out value="${order.address}"/>
                </div>

                <div style="font-size: 0.95rem; margin-bottom: 12px;">
                    申請者: <c:out value="${order.name}"/> 様 (<a href="tel:<c:out value="${order.phone}"/>" style="color: var(--accent);"><c:out value="${order.phone}"/></a>)
                </div>

                <div style="background-color: var(--bg); padding: 10px 14px; border-radius: 8px; font-size: 0.9rem; margin-bottom: ${not empty order.note ? '10px' : '16px'}; border: 1px solid var(--border);">
                    <strong>配送物資:</strong> <c:out value="${order.itemSummary}"/>
                </div>

                <c:if test="${not empty order.note}">
                    <div style="background-color: var(--bg); padding: 10px 14px; border-radius: 8px; font-size: 0.9rem; margin-bottom: 16px; border: 1px solid var(--border);">
                        <strong>備考:</strong> <c:out value="${order.note}"/>
                    </div>
                </c:if>

                <div style="text-align: right;">
                    <a href="${pageContext.request.contextPath}/volunteer/detail?order_id=<c:out value='${order.orderId}'/>&tab=<c:out value='${currentTab}'/>" class="btn btn-primary" style="padding: 10px 24px;">
                        配送詳細・対応開始 ➔
                    </a>
                </div>
            </div>
        </c:forEach>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
