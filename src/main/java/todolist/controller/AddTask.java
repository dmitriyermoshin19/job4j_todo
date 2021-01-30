package todolist.controller;

import todolist.models.Item;
import todolist.memory.DBStore;
import todolist.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addTask")
public class AddTask extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String text = req.getParameter("text");
        Item item = new Item(text);
        User user = (User) getServletContext().getAttribute("currentUser");
        item.setUser(user);
        DBStore.getInstance().addItem(item);
    }

}
