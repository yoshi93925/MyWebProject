<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 物資申請</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 820px;">
        <header>
            <div>
                <h1>🆘 物資申請</h1>
            </div>
            <div style="display: flex; gap: 8px;">
                <a href="${pageContext.request.contextPath}/order" class="btn btn-secondary btn-icon" title="画面更新" aria-label="画面更新">🔄</a>
                <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
            </div>
        </header>

        <!-- カテゴリタブ -->
        <div class="tabs" style="margin-bottom: 16px; display: flex; gap: 8px; flex-wrap: wrap;">
            <button type="button" class="tab-link active" onclick="switchGenre(1, this);">食料・飲料</button>
            <button type="button" class="tab-link" onclick="switchGenre(2, this);">衛生・医療用品</button>
            <button type="button" class="tab-link" onclick="switchGenre(3, this);">生活・日用品</button>
            <button type="button" class="tab-link" onclick="switchGenre(4, this);">防寒・睡眠・衣類</button>
            <button type="button" class="tab-link" onclick="switchGenre(5, this);">インフラ・環境整備</button>
        </div>

        <c:if test="${not empty errorMessage and empty modalAction}">
            <div class="inline-error">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/order" method="post">
            <div class="card">
                <h3 class="card-title">📦 物資一覧（選択してください）</h3>

                <div class="item-scroll-area">
                    <c:forEach var="item" items="${items}">
                        <div class="item-card item-card-wrapper" data-genre-id="<c:out value='${item.genreId}'/>" style="${item.genreId == 1 ? 'display: flex;' : 'display: none;'}">
                            <div>
                                <div style="font-weight: 700; font-size: 1.1rem; color: var(--text);">
                                    <c:out value="${item.itemName}"/>
                                </div>
                                <div style="font-size: 0.85rem; color: var(--text-muted); margin-top: 4px;">
                                    保管場所: <c:out value="${item.location}"/> | 在庫数: <c:out value="${item.stock}"/> 個
                                </div>
                            </div>
                            <div class="qty-control">
                                <button type="button" class="btn-qty" onclick="changeQty(<c:out value='${item.itemId}'/>, -1, <c:out value='${item.stock}'/>);">-</button>
                                <input type="text" name="qty_<c:out value='${item.itemId}'/>" id="qty_<c:out value='${item.itemId}'/>" class="qty-input" value="<c:out value='${empty selectedQtyMap[item.itemId] ? 0 : selectedQtyMap[item.itemId]}'/>" readonly>
                                <button type="button" class="btn-qty" onclick="changeQty(<c:out value='${item.itemId}'/>, 1, <c:out value='${item.stock}'/>);">+</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="action-bar">
                <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
                <button type="submit" class="btn btn-primary" style="padding: 12px 28px; font-size: 1.05rem;">
                    住所入力へ進む ➔
                </button>
            </div>
        </form>
    </div>

    <%@ include file="common_error_modal.jsp" %>

    <script>
        function switchGenre(genreId, btnElement) {
            document.querySelectorAll('.tabs .tab-link').forEach(function(tab) {
                tab.classList.remove('active');
            });
            if (btnElement) {
                btnElement.classList.add('active');
            }

            document.querySelectorAll('.item-card-wrapper').forEach(function(card) {
                if (card.getAttribute('data-genre-id') == genreId) {
                    card.style.display = 'flex';
                } else {
                    card.style.display = 'none';
                }
            });
        }

        function changeQty(itemId, delta, maxQty) {
            const input = document.getElementById('qty_' + itemId);
            let val = parseInt(input.value) || 0;
            val += delta;
            if (val < 0) val = 0;
            if (maxQty !== undefined && val > maxQty) val = maxQty;
            input.value = val;
        }
    </script>
</body>
</html>
