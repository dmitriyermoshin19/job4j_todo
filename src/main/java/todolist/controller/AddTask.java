package todolist.controller;

import com.google.gson.Gson;
import todolist.models.Category;
import todolist.models.Item;
import todolist.memory.DBStore;
import todolist.models.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/addTask")
public class AddTask extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = DBStore.getInstance().findAll(Category.class);
        String json = new Gson().toJson(categories);
        PrintWriter out = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        out.println(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String text = req.getParameter("text");
        String[] cIds = req.getParameterValues("cIds[]");
        Item item = new Item(text);
        User user = (User) getServletContext().getAttribute("currentUser");
        item.setUser(user);
        item = DBStore.getInstance().addItem(item, cIds);
        String json = new Gson().toJson(item);
        PrintWriter out = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        out.println(json);
    }

}
