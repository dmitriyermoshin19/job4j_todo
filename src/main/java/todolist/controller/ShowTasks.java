package todolist.controller;

import com.google.gson.Gson;
import todolist.models.Category;
import todolist.models.Item;
import todolist.memory.DBStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(value = "/showTasks", loadOnStartup = 0)
public class ShowTasks extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Item> items = DBStore.getInstance().findAll(Item.class);
        String json = new Gson().toJson(items);
        PrintWriter out = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        out.println(json);
    }
}
