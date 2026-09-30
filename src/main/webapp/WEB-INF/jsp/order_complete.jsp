<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ラストワンマイル - 注文完了</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="max-width: 680px; padding-top: 30px;">
        <div class="card" style="text-align: center; padding: 40px 24px;">
            <div style="font-size: 3.2rem; margin-bottom: 12px;">🎉</div>
            <h2 style="color: var(--success); font-size: 1.5rem; font-weight: 900; margin-bottom: 12px;">物資リクエストの申請が完了しました</h2>
            <p style="color: var(--text-muted); font-size: 0.95rem; margin-bottom: 32px;">
                ご申請ありがとうございます。担当ボランティアが準備でき次第、避難所へお届けいたします。
            </p>

            <div style="background-color: var(--primary-light); border: 2px dashed var(--accent); border-radius: 12px; padding: 28px 20px; margin-bottom: 32px;">
                <div style="font-size: 0.95rem; color: var(--primary); font-weight: bold; margin-bottom: 8px;">
                    【あなたの注文番号】
                </div>
                <div style="font-size: 2.4rem; font-weight: 900; letter-spacing: 4px; color: var(--primary); margin-bottom: 12px;">
                    <c:out value="${orderNo}"/>
                </div>
                <div style="font-size: 0.85rem; color: var(--text-muted); line-height: 1.5;">
                    ※トップ画面でこの注文番号を入力すると、配送状況の確認や申請の取り消しが行えます。メモまたはスクリーンショットをお控えください。
                </div>
            </div>

            <div>
                <a href="${pageContext.request.contextPath}/top" class="btn btn-secondary btn-icon" title="トップに戻る" aria-label="トップに戻る">🏠</a>
            </div>
        </div>
    </div>

    <%@ include file="common_error_modal.jsp" %>
</body>
</html>
