package dao;

import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;


import java.util.ArrayList;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.Amount;
import model.Employee;
import model.Product;

import org.bson.Document;

public class DaoImplMongoDB implements Dao{
	private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> employeeCollection;
    private MongoCollection<Document> inventoryCollection;
    private MongoCollection<Document> historicalCollection;
    
	MongoCollection<Document> collection;
	ObjectId id;

	@Override
	public void connect() {
	    String uri = "mongodb://localhost:27017";
	    MongoClientURI mongoClientURI = new MongoClientURI(uri);
	    this.mongoClient = new MongoClient(mongoClientURI);
	    this.database = mongoClient.getDatabase("Shop");
	    this.employeeCollection = database.getCollection("Users");
	    this.inventoryCollection = database.getCollection("Inventory");
	    this.historicalCollection = database.getCollection("Historical_Inventory");
	}

	@Override
	public void disconnect() {
		if (mongoClient != null) {
	        mongoClient.close();
	    }
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
	    Document query = new Document("employeeId", employeeId).append("password", password);
	    Document doc = employeeCollection.find(query).first();

	    if (doc != null) {
	        Employee employee = new Employee();
	        employee.setEmployeeId(doc.getInteger("employeeId"));
	        employee.setName(doc.getString("name"));
	        return employee;
	    }
	    return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
	    ArrayList<Product> inventory = new ArrayList<>();
	    
	    for (Document doc : inventoryCollection.find()) {
	        int id = doc.getInteger("id").intValue();
	        String name = doc.getString("name");
	        
	        Document priceDoc = (Document) doc.get("wholesalerPrice");
	        double price = priceDoc.getDouble("value").doubleValue();
	        String currency = priceDoc.getString("currency");
	        Amount wholesalerPrice = new Amount(price, currency);
	        
	        boolean available = doc.getBoolean("available");
	        int stock = doc.getInteger("stock").intValue();
	        Product product = new Product(name, wholesalerPrice, available, stock);
	        product.setId(id); 
	        inventory.add(product);
	    }
	    
	    return inventory;
	}

	@Override
	public boolean addProduct(Product product) {
	    try {
	        Document maxIdDoc = inventoryCollection.find()
	                .sort(new Document("id", -1))
	                .first();
	        int nextId = (maxIdDoc != null && maxIdDoc.getInteger("id") != null)
	                     ? maxIdDoc.getInteger("id") + 1 : 1;

	        double priceValue = (product.getWholesalerPrice() != null)
	                            ? product.getWholesalerPrice().getValue()
	                            : product.getPrice();

	        Document doc = new Document("id", nextId)
	                .append("name", product.getName())
	                .append("wholesalerPrice", new Document("value", priceValue)
	                        .append("currency", "€"))
	                .append("available", product.isAvailable())
	                .append("stock", product.getStock());

	        inventoryCollection.insertOne(doc);
	        product.setId(nextId);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean updateProduct(Product product) {
	    Document query = new Document("id", product.getId());

	    double priceValue = (product.getWholesalerPrice() != null)
	                        ? product.getWholesalerPrice().getValue()
	                        : product.getPrice();

	    Document updateData = new Document("name", product.getName())
	            .append("wholesalerPrice", new Document("value", priceValue)
	                    .append("currency", "€"))
	            .append("available", product.isAvailable())
	            .append("stock", product.getStock());

	    UpdateResult result = inventoryCollection.updateOne(query, new Document("$set", updateData));
	    return result.getModifiedCount() > 0;
	}

	@Override
	public boolean deleteProduct(int productId) {
	    DeleteResult result = inventoryCollection.deleteOne(eq("id", productId));
	    return result.getDeletedCount() > 0;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
	    try {
	        for (Product product : inventory) {
	            double priceValue = (product.getWholesalerPrice() != null) 
	                                ? product.getWholesalerPrice().getValue() 
	                                : product.getPrice();

	            Document doc = new Document("id", product.getId())
	                    .append("name", product.getName())
	                    .append("wholesalerPrice", new Document("value", priceValue)
	                            .append("currency", "€"))
	                    .append("available", product.isAvailable())
	                    .append("stock", product.getStock())
	                    .append("export_date", java.time.LocalDateTime.now().toString());
	            
	            historicalCollection.insertOne(doc);
	        }
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
}