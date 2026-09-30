<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<input type="hidden" id="modalErrorMessage" value="<c:out value='${errorMessage}'/>">
<input type="hidden" id="modalAction" value="<c:out value='${modalAction}'/>">
<input type="hidden" id="modalRedirectUrl" value="<c:out value='${redirectUrl}'/>">

<div id="errorModal" class="modal" style="display: none;">
    <div class="modal-content">
        <h3 style="color: var(--danger); margin-bottom: 12px; display: flex; align-items: center; gap: 8px;">
            <span>⚠️</span> エラー
        </h3>
        <p id="errorModalText" style="margin-bottom: 24px; font-size: 1.05rem; line-height: 1.5; color: var(--text);">
            <c:out value="${errorMessage}"/>
        </p>
        <div style="text-align: right;">
            <button type="button" class="btn btn-primary" id="errorModalOkBtn" onclick="handleErrorModalOk();">
                OK
            </button>
        </div>
    </div>
</div>

<script>
(function() {
    function initModal() {
        var msgElem = document.getElementById('modalErrorMessage');
        var actionElem = document.getElementById('modalAction');
        var redirectElem = document.getElementById('modalRedirectUrl');
        if (msgElem && actionElem) {
            var msg = msgElem.value;
            var action = actionElem.value;
            if (msg && action) {
                var modal = document.getElementById('errorModal');
                if (modal) {
                    modal.style.display = 'flex';
                }
                if (redirectElem && redirectElem.value && window.history && window.history.replaceState) {
                    window.history.replaceState(null, '', redirectElem.value);
                }
            }
        }
    }
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initModal);
    } else {
        initModal();
    }
})();

function handleErrorModalOk() {
    var actionElem = document.getElementById('modalAction');
    var redirectElem = document.getElementById('modalRedirectUrl');
    var action = actionElem ? actionElem.value : '';
    var redirectUrl = redirectElem ? redirectElem.value : '';

    var msgElem = document.getElementById('modalErrorMessage');
    if (msgElem) msgElem.value = '';
    if (actionElem) actionElem.value = '';
    if (redirectElem) redirectElem.value = '';

    var modal = document.getElementById('errorModal');
    if (modal) modal.style.display = 'none';

    if (redirectUrl) {
        window.location.href = redirectUrl;
    } else if (action === 'reload') {
        window.location.href = window.location.pathname + window.location.search;
    }
}
</script>
