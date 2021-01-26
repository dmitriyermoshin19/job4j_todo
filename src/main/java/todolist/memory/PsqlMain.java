package todolist.memory;

import todolist.models.Item;

public class PsqlMain {
    public static void main(String[] args) throws ClassNotFoundException {
        DBStore store = DBStore.getInstance();
        store.addItem(new Item("1task"));
        for (Item p : store.findAll())  {
            System.out.println(p.getId() + " " + p.getDesc());
        }
    }
}
