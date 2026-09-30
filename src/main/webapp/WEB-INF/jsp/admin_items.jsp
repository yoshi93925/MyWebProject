<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 管理者物資一覧</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 980px;">
        <header style="margin-bottom: 20px;">
            <div>
                <h1 style="font-size: 1.7rem; color: var(--primary); font-weight: 900;">📦 管理者物資一覧</h1>
            </div>
            <div style="display: flex; gap: 8px;">
                <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-secondary btn-icon" title="画面更新" aria-label="画面更新">🔄</a>
                <a href="${pageContext.request.contextPath}/admin/menu" class="btn btn-secondary btn-icon" title="一覧選択へ戻る" aria-label="一覧選択へ戻る">🔙</a>
            </div>
        </header>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                <c:out value="${successMessage}"/>
            </div>
        </c:if>

        <c:if test="${not empty errorMessage and empty modalAction}">
            <div class="inline-error">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <!-- カテゴリタブ -->
        <div class="tabs" style="margin-bottom: 16px; display: flex; gap: 8px; flex-wrap: wrap;">
            <button type="button" class="tab-link active" onclick="filterGenre(1, this);">食料・飲料</button>
            <button type="button" class="tab-link" onclick="filterGenre(2, this);">衛生・医療用品</button>
            <button type="button" class="tab-link" onclick="filterGenre(3, this);">生活・日用品</button>
            <button type="button" class="tab-link" onclick="filterGenre(4, this);">防寒・睡眠・衣類</button>
            <button type="button" class="tab-link" onclick="filterGenre(5, this);">インフラ・環境整備</button>
            <button type="button" class="tab-link" onclick="filterGenre('all', this);">すべて</button>
        </div>

        <!-- 新規物資追加ボタン -->
        <div style="margin-bottom: 24px;">
            <a href="${pageContext.request.contextPath}/admin/item/add" class="btn btn-primary" style="padding: 12px 24px; font-weight: bold;">
                ＋ 新規物資の追加
            </a>
        </div>

        <!-- 物資一覧テーブル -->
        <div class="card" style="padding: 0; overflow-x: auto; border-radius: 12px;">
            <table style="width: 100%; border-collapse: collapse;">
                <thead>
                    <tr style="background-color: #f8fafc; border-bottom: 1px solid var(--border);">
                        <th style="width: 70px; padding: 14px 16px; line-height: 1.3;">品目<br>ID</th>
                        <th style="min-width: 180px; padding: 14px 16px;">品目名</th>
                        <th style="width: 140px; padding: 14px 16px;">カテゴリ</th>
                        <th style="width: 100px; padding: 14px 16px;">在庫数</th>
                        <th style="width: 110px; padding: 14px 16px;">場所</th>
                        <th style="width: 160px; padding: 14px 16px;">最終更新</th>
                        <th style="width: 180px; text-align: right; padding: 14px 24px 14px 16px;">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr class="item-row" data-genre-id="<c:out value='${item.genreId}'/>" style="${item.genreId == 1 ? '' : 'display: none;'}">
                            <td style="color: var(--text-muted); padding: 16px;">#<c:out value="${item.itemId}"/></td>
                            <td style="font-weight: 900; font-size: 1.1rem; color: var(--text); padding: 16px;"><c:out value="${item.itemName}"/></td>
                            <td style="padding: 16px;"><span class="badge badge-gray"><c:out value="${item.genreName}"/></span></td>
                            <td style="font-weight: 900; font-size: 1.2rem; color: var(--text); padding: 16px;"><c:out value="${item.stock}"/> 個</td>
                            <td style="font-size: 1rem; padding: 16px;"><c:out value="${item.location}"/></td>
                            <td style="font-size: 0.85rem; color: var(--text-muted); line-height: 1.4; padding: 16px;"><c:out value="${item.updatedAtFormatted}"/></td>
                            <td style="text-align: right; padding: 16px 24px 16px 16px;">
                                <div style="display: inline-flex; gap: 6px;">
                                    <a href="${pageContext.request.contextPath}/admin/item/stock?item_id=<c:out value='${item.itemId}'/>" class="btn btn-secondary" style="padding: 6px 12px; font-size: 0.85rem; background-color: #f0f9ff; color: #0284c7; border: 1px solid #bae6fd; border-radius: 6px; font-weight: bold;">入荷</a>
                                    <a href="${pageContext.request.contextPath}/admin/item/edit?item_id=<c:out value='${item.itemId}'/>" class="btn btn-secondary" style="padding: 6px 12px; font-size: 0.85rem; border-radius: 6px; font-weight: bold;">編集</a>
                                    <button type="button" class="btn btn-danger" style="padding: 6px 12px; font-size: 0.85rem; border-radius: 6px; font-weight: bold;" onclick="confirmDelete(<c:out value='${item.itemId}'/>, '<c:out value='${item.itemName}'/>');">削除</button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 削除確認モーダル -->
    <div id="deleteConfirmModal" class="modal" style="display: none;">
        <div class="modal-content">
            <h3 style="color: var(--danger); margin-bottom: 12px; display: flex; align-items: center; gap: 8px;">
                <span>🗑️</span> 物資の削除確認
            </h3>
            <p id="deleteConfirmText" style="margin-bottom: 24px; font-size: 1rem; color: var(--text);">
                この物資を削除しますか？
            </p>
            <form id="deleteForm" action="${pageContext.request.contextPath}/admin/item/delete" method="post">
                <input type="hidden" name="item_id" id="deleteItemId" value="">
                <div style="display: flex; justify-content: flex-end; gap: 12px;">
                    <button type="button" class="btn btn-secondary" onclick="closeDeleteModal();">キャンセル</button>
                    <button type="submit" class="btn btn-danger">削除する</button>
                </div>
            </form>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>

    <script>
        function filterGenre(genreId, btnElement) {
            document.querySelectorAll('.tabs .tab-link').forEach(function(tab) {
                tab.classList.remove('active');
            });
            if (btnElement) {
                btnElement.classList.add('active');
            }

            document.querySelectorAll('.item-row').forEach(function(row) {
                if (genreId === 'all' || row.getAttribute('data-genre-id') == genreId) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }

        function confirmDelete(itemId, itemName) {
            document.getElementById('deleteItemId').value = itemId;
            document.getElementById('deleteConfirmText').textContent = '物資「' + itemName + '」を削除しますか？注文履歴がある場合は削除できません。';
            document.getElementById('deleteConfirmModal').style.display = 'flex';
        }

        function closeDeleteModal() {
            document.getElementById('deleteConfirmModal').style.display = 'none';
        }
    </script>
</body>
</html>
