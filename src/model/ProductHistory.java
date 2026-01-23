package model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historical_inventory")
public class ProductHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @Column
    private String name;
    
    @Column
    private double price;

    @Column
    private boolean available;
    
    @Column
    private int stock;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    public final static double EXPIRATION_RATE=0.60;
    
//  private Amount publicPrice;
//  private Amount wholesalerPrice;
//  private static int totalProducts;
    
    public ProductHistory() {

    }
	public ProductHistory(String name, double price, boolean available, int stock) {
		super();
		this.name = name;
		this.price = price;
//		this.wholesalerPrice = wholesalerPrice;
//		this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
		this.available = available;
		this.stock = stock;
		this.createdAt = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

	

//	public double getPublicPrice() {
//		return publicPrice;
//	}
//
//	public void setPublicPrice(double publicPrice) {
//		this.publicPrice = publicPrice;
//	}
//
//	public double getWholesalerPrice() {
//		return wholesalerPrice;
//	}
//
//	public void setWholesalerPrice(double wholesalerPrice) {
//		this.wholesalerPrice = wholesalerPrice;
//	}
	
	

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

//	public static int getTotalProducts() {
//		return totalProducts;
//	}
//
//	public static void setTotalProducts(int totalProducts) {
//		Product.totalProducts = totalProducts;
//	}
	
	public void expire() {
//		this.publicPrice.setValue(this.getPublicPrice().getValue()*EXPIRATION_RATE); ;
		this.price = (this.getPrice()*EXPIRATION_RATE); ;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price
				+ ", available=" + available + ", stock=" + stock + "]";
	}

	
	
	
	
	

    

    
}
