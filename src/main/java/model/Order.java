package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.DateUtil;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private String orderNo;
    private String name;
    private String address;
    private String phone;
    private String note;
    private Timestamp orderedAt;
    private String deliveryStaff;
    private String deliveryStatus;
    private String notdeliveryNote;
    private List<OrderDetail> details = new ArrayList<>();

    public Order() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Timestamp orderedAt) {
        this.orderedAt = orderedAt;
    }

    public String getDeliveryStaff() {
        return deliveryStaff;
    }

    public void setDeliveryStaff(String deliveryStaff) {
        this.deliveryStaff = deliveryStaff;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getNotdeliveryNote() {
        return notdeliveryNote;
    }

    public void setNotdeliveryNote(String notdeliveryNote) {
        this.notdeliveryNote = notdeliveryNote;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    public String getOrderedAtFormatted() {
        return DateUtil.formatOrderDate(this.orderedAt);
    }

    public String getItemSummary() {
        if (details == null || details.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < details.size(); i++) {
            OrderDetail d = details.get(i);
            sb.append(d.getItemName()).append(" × ").append(d.getQuantity()).append(" 個");
            if (i < details.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String getStatusBadgeClass() {
        if ("未対応".equals(this.deliveryStatus)) {
            return "badge badge-warning";
        } else if ("対応中".equals(this.deliveryStatus)) {
            return "badge badge-info";
        } else if ("完了".equals(this.deliveryStatus)) {
            return "badge badge-success";
        } else if ("配送不可".equals(this.deliveryStatus)) {
            return "badge badge-danger";
        }
        return "badge badge-gray";
    }
}
