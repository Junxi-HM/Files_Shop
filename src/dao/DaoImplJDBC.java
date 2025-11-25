package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, employeeId);
			ps.setString(2, password);
			// System.out.println(ps.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
				}
			}
		} catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public ArrayList<Product> getInvertory() {
		ArrayList<Product> inventory = new ArrayList<>();
		String query = "SELECT * FROM inventory";
		
		try (Statement stmt = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				double wholesalerPrice = rs.getDouble("wholesalerPrice");
				boolean available = rs.getBoolean("available");
				int stock = rs.getInt("stock");
				
				// Product with a wholesale price
				Product product = new Product(name, new Amount(wholesalerPrice), available, stock);
				// Adjust the ID and public price from the database
				product.setId(id);
				
				inventory.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
	    if (inventory == null || inventory.isEmpty()) {
	        return false;
	    }
	    
	    String query = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement ps = connection.prepareStatement(query)) {
	        for (Product product : inventory) {
	            ps.setInt(1, product.getId());
	            ps.setString(2, product.getName());
	            ps.setDouble(3, product.getWholesalerPrice().getValue());
	            ps.setBoolean(4, product.isAvailable());
	            ps.setInt(5, product.getStock());
	            ps.executeUpdate();
	        }
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean addProduct(Product product) {
		if (product == null) {
			return false;
		}
		
		String query = "INSERT INTO inventory (id, name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?)";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, product.getId());
			ps.setString(2, product.getName());
			ps.setDouble(3, product.getWholesalerPrice().getValue());
			ps.setBoolean(4, product.isAvailable());
			ps.setInt(5, product.getStock());
			
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateProduct(Product product) {
		if (product == null) {
			return false;
		}
		
		String query = "UPDATE inventory SET name = ?, wholesalerPrice = ?, available = ?, stock = ? WHERE id = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, product.getName());
			ps.setDouble(2, product.getWholesalerPrice().getValue());
			ps.setBoolean(3, product.isAvailable());
			ps.setInt(4, product.getStock());
			ps.setInt(5, product.getId());
			
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteProduct(int productId) {
		String query = "DELETE FROM inventory WHERE id = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, productId);
			
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
