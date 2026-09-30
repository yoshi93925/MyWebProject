<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 配送詳細</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <div>
                <h1>📦 配送詳細</h1>
            </div>
            <div style="display: flex; gap: 8px;">
                <a href="${pageContext.request.contextPath}/volunteer/detail?order_id=<c:out value='${order.orderId}'/>&tab=<c:out value='${currentTab}'/>" class="btn btn-secondary btn-icon" title="画面更新" aria-label="画面更新">🔄</a>
                <a href="${pageContext.request.contextPath}/volunteer/list?tab=<c:out value='${currentTab}'/>" class="btn btn-secondary btn-icon" title="前に戻る" aria-label="前に戻る">🔙</a>
            </div>
        </header>

        <c:if test="${not empty errorMessage and empty modalAction}">
            <div class="inline-error">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <div class="card" style="display: flex; justify-content: space-between; align-items: center; background: var(--primary-light);">
            <div>
                <span style="font-size: 0.85rem; color: var(--text-muted);">注文日時: <c:out value="${order.orderedAtFormatted}"/></span>
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
                <c:if test="${order.deliveryStatus != '未対応'}">
                    <tr>
                        <th>配送担当者</th>
                        <td><c:out value="${empty order.deliveryStaff ? '未定' : order.deliveryStaff}"/></td>
                    </tr>
                </c:if>
                <c:if test="${order.deliveryStatus == '配送不可'}">
                    <tr>
                        <th style="color: var(--danger);">配送不可理由</th>
                        <td style="color: var(--danger); font-weight: bold;"><c:out value="${order.notdeliveryNote}"/></td>
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

        <c:if test="${order.deliveryStatus == '未対応'}">
            <div class="card" style="border: 2px solid var(--accent);">
                <h3 class="card-title" style="color: var(--primary);">🚚 配送対応の更新</h3>
                <form action="${pageContext.request.contextPath}/volunteer/status" method="post" style="display: flex; gap: 12px; align-items: flex-end; flex-wrap: wrap;">
                    <input type="hidden" name="order_id" value="<c:out value='${order.orderId}'/>">
                    <input type="hidden" name="tab" value="<c:out value='${currentTab}'/>">
                    <input type="hidden" name="action_type" value="start">

                    <div style="flex: 1; min-width: 240px;" class="form-group" style="margin: 0;">
                        <label for="delivery_staff">配送担当者名（ボランティア名） <span style="color:var(--danger);">*</span></label>
                        <input type="text" name="delivery_staff" id="delivery_staff" class="form-control" placeholder="例: 佐藤 ボランティア" required>
                    </div>
                    <button type="submit" class="btn btn-primary" style="height: 46px; margin-bottom: 20px;">
                        🚚 対応開始する
                    </button>
                </form>
            </div>
        </c:if>

        <c:if test="${order.deliveryStatus == '対応中'}">
            <div class="card" style="border: 2px solid var(--accent);">
                <h3 class="card-title" style="color: var(--primary);">🚚 配送対応の更新</h3>
                <form id="statusUpdateForm" action="${pageContext.request.contextPath}/volunteer/status" method="post">
                    <input type="hidden" name="order_id" value="<c:out value='${order.orderId}'/>">
                    <input type="hidden" name="tab" value="<c:out value='${currentTab}'/>">
                    <input type="hidden" name="action_type" value="update">
                    <input type="hidden" name="notdelivery_note" id="hiddenNotdeliveryNote" value="">

                    <div style="display: flex; gap: 12px; align-items: flex-end; flex-wrap: wrap;">
                        <div style="flex: 1; min-width: 240px;" class="form-group" style="margin: 0;">
                            <label for="newStatusSelect">対応ステータス選択</label>
                            <select name="new_status" id="newStatusSelect" class="form-control" required>
                                <option value="">-- 選択してください --</option>
                                <option value="未対応">未対応</option>
                                <option value="完了">完了</option>
                                <option value="配送不可">配送不可</option>
                            </select>
                        </div>
                        <button type="button" class="btn btn-primary" onclick="handleStatusConfirm();" style="height: 46px; margin-bottom: 20px;">
                            確定
                        </button>
                    </div>
                </form>
            </div>
        </c:if>
    </div>

    <!-- ステータス変更確認ダイアログ -->
    <div id="statusModal" class="modal" style="display: none;">
        <div class="modal-content">
            <h3 style="color: var(--primary); margin-bottom: 12px; display: flex; align-items: center; gap: 8px;">
                <span>🚚</span> 配送ステータスの更新確認
            </h3>
            <p id="statusModalMsg" style="margin-bottom: 16px; font-size: 1rem; color: var(--text);"></p>

            <div id="unableReasonArea" style="display: none; margin-bottom: 16px;">
                <label for="modalReasonInput" style="display: block; font-weight: bold; margin-bottom: 6px; font-size: 0.9rem;">
                    配送不可理由 <span style="color:var(--danger);">*</span>
                </label>
                <textarea id="modalReasonInput" class="form-control" rows="3" placeholder="理由を入力してください（例: 道路寸断のため立ち入り不能）"></textarea>
                <div id="reasonErrorMsg" style="color: var(--danger); font-size: 0.85rem; font-weight: bold; margin-top: 4px; display: none;">
                    配送不可理由を入力してください
                </div>
            </div>

            <div style="display: flex; justify-content: flex-end; gap: 12px;">
                <button type="button" class="btn btn-secondary" onclick="closeStatusModal();">キャンセル</button>
                <button type="button" class="btn btn-primary" onclick="submitStatusUpdate();">登録</button>
            </div>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>

    <script>
        function handleStatusConfirm() {
            var sel = document.getElementById('newStatusSelect');
            var val = sel ? sel.value : '';
            if (!val) {
                alert('配送ステータスを選択してください');
                return;
            }

            var msgElem = document.getElementById('statusModalMsg');
            var reasonArea = document.getElementById('unableReasonArea');
            var reasonError = document.getElementById('reasonErrorMsg');
            if (reasonError) reasonError.style.display = 'none';

            if (val === '未対応') {
                msgElem.textContent = '配送ステータスを未対応に戻します';
                reasonArea.style.display = 'none';
            } else if (val === '完了') {
                msgElem.textContent = '配送ステータスを完了にします';
                reasonArea.style.display = 'none';
            } else if (val === '配送不可') {
                msgElem.textContent = '配送不可の場合は理由を入力してください';
                reasonArea.style.display = 'block';
            }

            document.getElementById('statusModal').style.display = 'flex';
        }

        function closeStatusModal() {
            document.getElementById('statusModal').style.display = 'none';
        }

        function submitStatusUpdate() {
            var sel = document.getElementById('newStatusSelect');
            var val = sel ? sel.value : '';

            if (val === '配送不可') {
                var reasonInput = document.getElementById('modalReasonInput');
                var reasonVal = reasonInput ? reasonInput.value.trim() : '';
                if (!reasonVal) {
                    var reasonError = document.getElementById('reasonErrorMsg');
                    if (reasonError) reasonError.style.display = 'block';
                    return;
                }
                document.getElementById('hiddenNotdeliveryNote').value = reasonVal;
            }

            document.getElementById('statusUpdateForm').submit();
        }
    </script>
</body>
</html>
