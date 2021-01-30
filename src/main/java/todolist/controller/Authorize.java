package todolist.controller;

import todolist.models.User;
import todolist.memory.DBStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth")
public class Authorize extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = DBStore.getInstance().findByEmail(email);
        if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password)) {
            getServletContext().setAttribute("currentUser", user);
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/tasks.html");
        } else {
            req.setAttribute("error", "Не верный email или пароль");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
