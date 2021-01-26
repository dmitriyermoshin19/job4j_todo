package todolist.controller;

import todolist.models.Item;
import todolist.memory.DBStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/doTask")
public class DoTask extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Item item = DBStore.getInstance().findById(id);
        item.setDone(true);
        DBStore.getInstance().updateItem(item);
    }

}
