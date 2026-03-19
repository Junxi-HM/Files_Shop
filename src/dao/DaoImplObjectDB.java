package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Product;

public class DaoImplObjectDB implements Dao{

	private EntityManagerFactory emf;
	private EntityManager em;
 
	@Override
	public void connect() {
		emf = Persistence.createEntityManagerFactory("objects/employee.odb");
		em = emf.createEntityManager();
		
		TypedQuery<Employee> check = em.createQuery(
				"SELECT e FROM Employee e WHERE e.employeeId = :id", Employee.class);
			check.setParameter("id", 1);
			if (check.getResultList().isEmpty()) {
				em.getTransaction().begin();
				Employee jux = new Employee(1, "userTest", "123");
				em.persist(jux);
				em.getTransaction().commit();
			}
	}
 
	@Override
	public void disconnect() {
		if (em != null && em.isOpen()) {
			em.close();
		}
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		try {
			TypedQuery<Employee> query = em.createQuery(
				"SELECT e FROM Employee e WHERE e.employeeId = :id AND e.password = :pwd",
				Employee.class
			);
			query.setParameter("id", employeeId);
			query.setParameter("pwd", password);
 
			java.util.List<Employee> results = query.getResultList();
			if (results != null && !results.isEmpty()) {
				return results.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(int productId) {
		// TODO Auto-generated method stub
		return false;
	}}