package todolist.memory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import todolist.models.Item;

import java.util.ArrayList;
import java.util.List;

public class DBStore {
    private static final DBStore INSTANCE = new DBStore();
    private static final Logger LOG = LogManager.getLogger(DBStore.class.getName());
    private final SessionFactory factory = new Configuration()
            .configure("items.cfg.xml")
            .buildSessionFactory();

    public static DBStore getInstance() {
        return INSTANCE;
    }

    public void addItem(Item item) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public Item findById(int id) {
        Item item = new Item();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            item = session.get(Item.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return item;
    }

    public void updateItem(Item item) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("UPDATE "
                    + "todolist.models.Item SET done = :done1 where id = :id");
            query.setParameter("done1", item.isDone());
            query.setParameter("id", item.getId());
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public List<Item> findAll() {
        List result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM "
                    + "todolist.models.Item ORDER BY id").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    public List<Item> showFilterItems() {
        List result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM "
                    + "todolist.models.Item "
                    + "WHERE done = false ORDER BY id").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

}
