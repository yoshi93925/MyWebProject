<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - トップ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 960px;">
        <header style="display: flex; justify-content: space-between; align-items: center; padding-bottom: 20px; border-bottom: 1px solid var(--border); margin-bottom: 28px;">
            <div>
                <h1 style="font-size: 1.8rem; font-weight: 900; color: var(--primary); display: flex; align-items: center; gap: 10px; margin: 0;">
                    <span style="font-size: 1.9rem;">📦</span> 避難所ラストワンマイル物資配送支援
                </h1>
            </div>
            <c:if test="${isAdminLoggedIn}">
                <div>
                    <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn-danger" style="padding: 8px 18px; font-size: 0.95rem; border-radius: 6px;">ログアウト</a>
                </div>
            </c:if>
        </header>

        <!-- メインメニューカード -->
        <div class="card" style="text-align: center; padding: 40px 28px; border-radius: 12px; margin-bottom: 28px;">
            <h2 style="color: var(--primary); font-size: 1.5rem; font-weight: 900; margin-bottom: 10px;">ご利用のメニューをお選びください</h2>
            <p style="color: var(--text-muted); font-size: 0.98rem; margin-bottom: 32px;">被災者の方の物資申請、ボランティアの配達管理、管理者の運用が行えます。</p>

            <!-- 3つの横並びカードグリッド -->
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 20px;">
                <!-- 物資依頼カード -->
                <a href="${pageContext.request.contextPath}/order" style="text-decoration: none; background-color: #f1ffff; border-radius: 12px; border: 2px solid #6495ed; padding: 32px 20px; display: flex; flex-direction: column; align-items: center; justify-content: center; transition: all 0.2s ease; box-shadow: 0 4px 12px rgba(30, 58, 138, 0.15);">
                    <div style="font-size: 2.2rem; margin-bottom: 12px;">
                        <span style="color: #dc2626; border: 2px solid rgba(220, 38, 38, 0.4); padding: 4px 8px; border-radius: 6px; font-weight: bold;">🆘</span>
                    </div>
                    <span style="color: var(--primary); font-size: 1.25rem; font-weight: 900; letter-spacing: 1px;">物資依頼</span>
                </a>

                <!-- 物資配達カード -->
                <a href="${pageContext.request.contextPath}/volunteer/list" style="text-decoration: none; background-color: #f0f7ff; border: 2px solid #2563eb; border-radius: 12px; padding: 32px 20px; display: flex; flex-direction: column; align-items: center; justify-content: center; transition: all 0.2s ease;">
                    <div style="font-size: 2.4rem; margin-bottom: 12px;">🚚</div>
                    <span style="color: var(--primary); font-size: 1.25rem; font-weight: 900; letter-spacing: 1px;">物資配達</span>
                </a>

                <!-- 管理者メニューカード -->
                <a href="${pageContext.request.contextPath}/admin/menu" style="text-decoration: none; background-color: #f8fafc; border: 2px solid var(--primary); border-radius: 12px; padding: 32px 20px; display: flex; flex-direction: column; align-items: center; justify-content: center; transition: all 0.2s ease;">
                    <div style="font-size: 2.4rem; margin-bottom: 12px;">⚙️</div>
                    <span style="color: var(--primary); font-size: 1.25rem; font-weight: 900; letter-spacing: 1px;">管理者メニュー</span>
                </a>
            </div>
        </div>

        <!-- 配送状況の確認・申請の取り消しカード -->
        <div class="card" style="padding: 28px;">
            <div style="font-size: 1.25rem; font-weight: 900; color: var(--primary); margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px dashed var(--border); display: flex; align-items: center; gap: 8px;">
                <span>🔍</span> 配送状況の確認・申請の取り消し
            </div>
            <p style="font-size: 0.95rem; color: var(--text-muted); margin-bottom: 20px; line-height: 1.6;">
                申請完了時に発行された「注文番号（7桁英数字）」を入力して、配達状況の確認や未対応申請の取り消しを行えます。
            </p>

            <c:if test="${not empty errorMessage and empty modalAction}">
                <div style="color: var(--danger); font-weight: bold; margin-bottom: 14px; font-size: 1rem;">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/order/status" method="get" style="display: flex; gap: 14px; align-items: center; flex-wrap: wrap;">
                <div style="flex: 1; min-width: 260px;">
                    <input type="text" name="order_no" class="form-control" placeholder="注文番号を入力 (7桁英数字 例: A8K9X2P)" required style="padding: 14px 18px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 1rem; width: 100%;">
                </div>
                <button type="submit" class="btn btn-primary" style="padding: 14px 28px; border-radius: 8px; font-weight: 900; font-size: 1rem; border: none; cursor: pointer; white-space: nowrap;">
                    確認する
                </button>
            </form>
        </div>
    </div>

    <c:if test="${logoutSuccess}">
        <div id="logoutModal" class="modal" style="display: flex;">
            <div class="modal-content">
                <h3 style="color: var(--primary); margin-bottom: 12px; display: flex; align-items: center; gap: 8px;">
                    <span>ℹ️</span> ログアウト完了
                </h3>
                <p style="margin-bottom: 24px; font-size: 1.05rem; line-height: 1.5; color: var(--text);">
                    ログアウトしました
                </p>
                <div style="text-align: right;">
                    <button type="button" class="btn btn-primary" onclick="closeLogoutModal();">
                        OK
                    </button>
                </div>
            </div>
        </div>
        <script>
            function closeLogoutModal() {
                var m = document.getElementById('logoutModal');
                if (m) {
                    m.style.display = 'none';
                }
                if (window.history && window.history.replaceState) {
                    window.history.replaceState(null, '', '${pageContext.request.contextPath}/top');
                }
            }
        </script>
    </c:if>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
