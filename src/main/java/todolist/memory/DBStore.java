package todolist.memory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import todolist.models.Item;
import todolist.models.User;

import java.util.function.Function;

import java.util.List;

public class DBStore {
    private static final DBStore INSTANCE = new DBStore();
    private static final Logger LOG = LogManager.getLogger(DBStore.class.getName());
    private final SessionFactory factory = new Configuration()
            .configure("todolist.cfg.xml")
            .buildSessionFactory();

    public static DBStore getInstance() {
        return INSTANCE;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = factory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
    }

    public void addItem(Item item) {
        this.tx(
                session -> session.save(item)
        );
    }

    public Item findById(int id) {
        return this.tx(
                session -> session.get(Item.class, id)
        );
    }

    public void updateItem(Item item) {
        this.tx(
                session -> {
                    Query query = session.createQuery("UPDATE "
                            + "todolist.models.Item "
                            + "SET done = :done1 where id = :id");
                    query.setParameter("done1", item.isDone());
                    query.setParameter("id", item.getId());
                    query.executeUpdate();
                    return null;
                }
        );
    }

    public List<Item> findAll() {
        return this.tx(
                session -> session.createQuery("FROM "
                        + "todolist.models.Item ORDER BY id").list()
        );
    }

    public List<Item> showFilterItems() {
        return this.tx(
                session -> session.createQuery("FROM "
                        + "todolist.models.Item "
                        + "WHERE done = false ORDER BY id").list()
        );
    }

    public void addUser(User user) {
        this.tx(session -> session.save(user));
    }

    public User findByEmail(String email) {
        return (User) this.tx(
                session -> session
                        .createQuery("FROM User WHERE email ='" + email + "'")
                        .getSingleResult()
        );
    }
}
