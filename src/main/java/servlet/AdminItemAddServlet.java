package servlet;

import dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import model.Item;
import util.CsrfTokenUtil;

public class AdminItemAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ItemDao itemDao = new ItemDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CsrfTokenUtil.generateToken(request);
        request.getRequestDispatcher("/WEB-INF/jsp/admin_item_add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!CsrfTokenUtil.validateToken(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/items");
            return;
        }

        String genreParam = request.getParameter("genre_id");
        String itemName = request.getParameter("item_name");
        String stockParam = request.getParameter("stock");
        String location = request.getParameter("location");

        if (itemName != null) itemName = itemName.trim();
        if (location != null) location = location.trim();

        request.setAttribute("genre_id", genreParam);
        request.setAttribute("item_name", itemName);
        request.setAttribute("stock", stockParam);
        request.setAttribute("location", location);

        int genreId;
        int stock;
        try {
            genreId = Integer.parseInt(genreParam);
            stock = Integer.parseInt(stockParam.trim());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "入力内容を確認してください。在庫数は半角数字で入力してください。");
            CsrfTokenUtil.generateToken(request);
            request.getRequestDispatcher("/WEB-INF/jsp/admin_item_add.jsp").forward(request, response);
            return;
        }

        if (itemName == null || itemName.isEmpty() || location == null || location.isEmpty() || stock < 0) {
            request.setAttribute("errorMessage", "全項目を正しく入力してください。");
            CsrfTokenUtil.generateToken(request);
            request.getRequestDispatcher("/WEB-INF/jsp/admin_item_add.jsp").forward(request, response);
            return;
        }

        try {
            if (itemDao.isDuplicate(genreId, itemName, location, null)) {
                request.setAttribute("errorMessage", "品名と場所が重複しているため追加できません");
                CsrfTokenUtil.generateToken(request);
                request.getRequestDispatcher("/WEB-INF/jsp/admin_item_add.jsp").forward(request, response);
                return;
            }

            Item item = new Item();
            item.setGenreId(genreId);
            item.setItemName(itemName);
            item.setStock(stock);
            item.setLocation(location);
            itemDao.insert(item);

            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "新しい物資を追加しました。");
            response.sendRedirect(request.getContextPath() + "/admin/items");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
