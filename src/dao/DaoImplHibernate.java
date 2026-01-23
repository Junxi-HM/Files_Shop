package dao;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {

    private Session getHibernateSession() {
        try {
            return new org.hibernate.cfg.Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(ProductHistory.class) // Añadida la clase de historial
                    .buildSessionFactory()
                    .openSession();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Product> getInventory() {
        Session session = getHibernateSession();
        ArrayList<Product> inventory = new ArrayList<>();
        if (session != null) {
            try {
                inventory.addAll(session.createQuery("from Product", Product.class).list());
            } finally {
                session.close();
            }
        }
        return inventory;
    }

    @Override
    public boolean addProduct(Product product) {
        Session session = null;
        try {
            session = getHibernateSession();
            session.beginTransaction();
            session.save(product);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        Session session = null;
        try {
            session = getHibernateSession();
            session.beginTransaction();
            session.update(product);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        Session session = null;
        try {
            session = getHibernateSession();
            session.beginTransaction();
            Product product = session.get(Product.class, productId);
            if (product != null) {
                session.delete(product);
            }
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        Session session = null;
        try {
            session = getHibernateSession();
            if (session == null) {
                return false;
            }

            session.beginTransaction();

            for (Product p : inventory) {
                ProductHistory history = new ProductHistory();
                history.setName(p.getName());
                history.setPrice(p.getPrice());
                history.setStock(p.getStock());
                history.setAvailable(p.isAvailable());
                history.setCreatedAt(new Date());
                session.save(history);
            }

            session.getTransaction().commit();
            return true;

        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override public void connect() {}
    @Override public void disconnect() {}
    @Override public Employee getEmployee(int employeeId, String password) { return null; }
}