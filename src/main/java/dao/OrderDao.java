package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Order;
import model.OrderDetail;
import util.DBUtil;

public class OrderDao {

    public Order findByOrderNo(String orderNo) throws SQLException {
        String sql = "SELECT order_id, order_no, name, address, phone, note, ordered_at, delivery_staff, delivery_status, notdelivery_note "
                   + "FROM orders WHERE order_no = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = extractOrder(rs);
                    order.setDetails(findOrderDetails(conn, order.getOrderId()));
                    return order;
                }
            }
        }
        return null;
    }

    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT order_id, order_no, name, address, phone, note, ordered_at, delivery_staff, delivery_status, notdelivery_note "
                   + "FROM orders WHERE order_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = extractOrder(rs);
                    order.setDetails(findOrderDetails(conn, order.getOrderId()));
                    return order;
                }
            }
        }
        return null;
    }

    public Order findByIdForUpdate(Connection conn, int orderId) throws SQLException {
        String sql = "SELECT order_id, order_no, name, address, phone, note, ordered_at, delivery_staff, delivery_status, notdelivery_note "
                   + "FROM orders WHERE order_id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractOrder(rs);
                }
            }
        }
        return null;
    }

    public List<Order> findAllWithDetails(String statusFilter) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.order_id, o.order_no, o.name, o.address, o.phone, o.note, o.ordered_at, ")
           .append("o.delivery_staff, o.delivery_status, o.notdelivery_note, ")
           .append("od.order_detail_id, od.item_id, od.quantity, i.item_name, i.location, i.genre_id ")
           .append("FROM orders o ")
           .append("LEFT JOIN order_details od ON o.order_id = od.order_id ")
           .append("LEFT JOIN items i ON od.item_id = i.item_id ");

        String statusParam = null;
        if (statusFilter != null && !statusFilter.isEmpty() && !"all".equalsIgnoreCase(statusFilter)) {
            switch (statusFilter.toLowerCase()) {
                case "undelivery":
                    statusParam = "未対応";
                    break;
                case "delivery":
                    statusParam = "対応中";
                    break;
                case "complete":
                    statusParam = "完了";
                    break;
                case "unable":
                    statusParam = "配送不可";
                    break;
                default:
                    statusParam = statusFilter;
                    break;
            }
            sql.append("WHERE o.delivery_status = ? ");
        }
        sql.append("ORDER BY o.ordered_at DESC, o.order_id DESC, od.order_detail_id ASC");

        Map<Integer, Order> orderMap = new LinkedHashMap<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (statusParam != null) {
                ps.setString(1, statusParam);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Order order = orderMap.get(orderId);
                    if (order == null) {
                        order = extractOrder(rs);
                        orderMap.put(orderId, order);
                    }
                    int detailId = rs.getInt("order_detail_id");
                    if (detailId > 0) {
                        OrderDetail detail = new OrderDetail(
                            detailId,
                            rs.getInt("item_id"),
                            orderId,
                            rs.getInt("quantity"),
                            rs.getString("item_name"),
                            rs.getString("location"),
                            rs.getInt("genre_id")
                        );
                        order.getDetails().add(detail);
                    }
                }
            }
        }
        return new ArrayList<>(orderMap.values());
    }

    public List<OrderDetail> findOrderDetails(Connection conn, int orderId) throws SQLException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.order_detail_id, od.item_id, od.order_id, od.quantity, i.item_name, i.location, i.genre_id "
                   + "FROM order_details od "
                   + "JOIN items i ON od.item_id = i.item_id "
                   + "WHERE od.order_id = ? "
                   + "ORDER BY od.order_detail_id ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("quantity"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getInt("genre_id")
                    ));
                }
            }
        }
        return list;
    }

    public List<OrderDetail> findOrderDetailsForUpdate(Connection conn, int orderId) throws SQLException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.order_detail_id, od.item_id, od.order_id, od.quantity, i.item_name, i.location, i.genre_id "
                   + "FROM order_details od "
                   + "JOIN items i ON od.item_id = i.item_id "
                   + "WHERE od.order_id = ? "
                   + "ORDER BY od.item_id ASC FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("quantity"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getInt("genre_id")
                    ));
                }
            }
        }
        return list;
    }

    public int insertOrder(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders (order_no, name, address, phone, note, ordered_at, delivery_staff, delivery_status, notdelivery_note) "
                   + "VALUES (?, ?, ?, ?, ?, now(), ?, ?, ?) RETURNING order_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getOrderNo());
            ps.setString(2, order.getName());
            ps.setString(3, order.getAddress());
            ps.setString(4, order.getPhone());
            ps.setString(5, order.getNote());
            ps.setString(6, order.getDeliveryStaff());
            ps.setString(7, order.getDeliveryStatus() != null ? order.getDeliveryStatus() : "未対応");
            ps.setString(8, order.getNotdeliveryNote());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to retrieve generated order_id");
    }

    public void insertOrderDetail(Connection conn, OrderDetail detail) throws SQLException {
        String sql = "INSERT INTO order_details (item_id, order_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getItemId());
            ps.setInt(2, detail.getOrderId());
            ps.setInt(3, detail.getQuantity());
            ps.executeUpdate();
        }
    }

    public boolean updateStatusAndStaff(Connection conn, int orderId, String status, String staff) throws SQLException {
        String sql = "UPDATE orders SET delivery_status = ?, delivery_staff = ? WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, staff);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStatusAndNote(Connection conn, int orderId, String status, String notdeliveryNote) throws SQLException {
        String sql = "UPDATE orders SET delivery_status = ?, notdelivery_note = ? WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, notdeliveryNote);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public void deleteOrderDetails(Connection conn, int orderId) throws SQLException {
        String sql = "DELETE FROM order_details WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }
    }

    public void deleteOrder(Connection conn, int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }
    }

    private Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setOrderNo(rs.getString("order_no"));
        order.setName(rs.getString("name"));
        order.setAddress(rs.getString("address"));
        order.setPhone(rs.getString("phone"));
        order.setNote(rs.getString("note"));
        order.setOrderedAt(rs.getTimestamp("ordered_at"));
        order.setDeliveryStaff(rs.getString("delivery_staff"));
        order.setDeliveryStatus(rs.getString("delivery_status"));
        order.setNotdeliveryNote(rs.getString("notdelivery_note"));
        return order;
    }
}
