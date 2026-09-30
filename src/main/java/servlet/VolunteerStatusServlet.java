package servlet;

import dao.ItemDao;
import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Order;
import model.OrderDetail;
import util.DBUtil;

public class VolunteerStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();
    private final ItemDao itemDao = new ItemDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty() || "all".equalsIgnoreCase(tab.trim())) {
            tab = "undelivery";
        }
        response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=" + tab);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("order_id");
        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty() || "all".equalsIgnoreCase(tab.trim())) {
            tab = "undelivery";
        }
        request.setAttribute("currentTab", tab);

        int orderId;
        try {
            orderId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=" + tab);
            return;
        }

        String actionType = request.getParameter("action_type"); // "start" or "update"

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Order order = orderDao.findByIdForUpdate(conn, orderId);
            if (order == null) {
                conn.rollback();
                request.setAttribute("errorMessage", "対象の注文は既に削除されています");
                request.setAttribute("modalAction", "redirect");
                request.setAttribute("redirectUrl", request.getContextPath() + "/volunteer/list?tab=" + tab);
                request.setAttribute("orders", orderDao.findAllWithDetails(tab));
                request.getRequestDispatcher("/WEB-INF/jsp/volunteer_list.jsp").forward(request, response);
                return;
            }

            if ("start".equals(actionType)) {
                String deliveryStaff = request.getParameter("delivery_staff");
                if (deliveryStaff != null) {
                    deliveryStaff = deliveryStaff.trim();
                }

                if (deliveryStaff == null || deliveryStaff.isEmpty()) {
                    conn.rollback();
                    order.setDetails(orderDao.findOrderDetails(conn, orderId));
                    request.setAttribute("order", order);
                    request.setAttribute("errorMessage", "配送担当者名を入力してください。");
                    request.getRequestDispatcher("/WEB-INF/jsp/volunteer_detail.jsp").forward(request, response);
                    return;
                }

                if (!"未対応".equals(order.getDeliveryStatus())) {
                    conn.rollback();
                    order.setDetails(orderDao.findOrderDetails(conn, orderId));
                    request.setAttribute("order", order);
                    request.setAttribute("errorMessage", "他の担当者が既に対応を開始したか、ステータスが変更されています");
                    request.setAttribute("modalAction", "reload");
                    request.setAttribute("redirectUrl", request.getContextPath() + "/volunteer/detail?order_id=" + orderId + "&tab=" + tab);
                    request.getRequestDispatcher("/WEB-INF/jsp/volunteer_detail.jsp").forward(request, response);
                    return;
                }

                orderDao.updateStatusAndStaff(conn, orderId, "対応中", deliveryStaff);
                conn.commit();
                response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=delivery");
                return;
            } else if ("update".equals(actionType)) {
                String newStatus = request.getParameter("new_status");
                String notdeliveryNote = request.getParameter("notdelivery_note");
                if (notdeliveryNote != null) {
                    notdeliveryNote = notdeliveryNote.trim();
                }

                if (!"対応中".equals(order.getDeliveryStatus())) {
                    conn.rollback();
                    order.setDetails(orderDao.findOrderDetails(conn, orderId));
                    request.setAttribute("order", order);
                    request.setAttribute("errorMessage", "他の担当者が既に対応を開始したか、ステータスが変更されています");
                    request.setAttribute("modalAction", "reload");
                    request.setAttribute("redirectUrl", request.getContextPath() + "/volunteer/detail?order_id=" + orderId + "&tab=" + tab);
                    request.getRequestDispatcher("/WEB-INF/jsp/volunteer_detail.jsp").forward(request, response);
                    return;
                }

                if ("配送不可".equals(newStatus)) {
                    if (notdeliveryNote == null || notdeliveryNote.isEmpty()) {
                        conn.rollback();
                        order.setDetails(orderDao.findOrderDetails(conn, orderId));
                        request.setAttribute("order", order);
                        request.setAttribute("errorMessage", "配送不可理由を入力してください");
                        request.getRequestDispatcher("/WEB-INF/jsp/volunteer_detail.jsp").forward(request, response);
                        return;
                    }
                    List<OrderDetail> details = orderDao.findOrderDetailsForUpdate(conn, orderId);
                    for (OrderDetail detail : details) {
                        itemDao.addStock(conn, detail.getItemId(), detail.getQuantity());
                    }
                    orderDao.updateStatusAndNote(conn, orderId, "配送不可", notdeliveryNote);
                    conn.commit();
                    response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=" + tab);
                    return;
                } else if ("未対応".equals(newStatus)) {
                    orderDao.updateStatusAndStaff(conn, orderId, "未対応", null);
                    conn.commit();
                    response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=" + tab);
                    return;
                } else if ("完了".equals(newStatus)) {
                    orderDao.updateStatusAndStaff(conn, orderId, "完了", order.getDeliveryStaff());
                    conn.commit();
                    response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=" + tab);
                    return;
                }
            }

            conn.rollback();
            response.sendRedirect(request.getContextPath() + "/volunteer/list?tab=" + tab);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw new ServletException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
