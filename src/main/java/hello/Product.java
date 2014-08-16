package hello;

import java.util.List;

public class Product {

    private final long sku;
    private final String productype;
    private final String quantity;
    private final String overview;
    private final String description;
    private final String pdfLink;
    private final List<String> benefits;
    private final String productname;
    private final String message;
    
    public Product(long sku, String productype, String quantity, String productname, 
    		String overview, String description, String pdfLink, List<String> benefits, String message) {
        this.sku = sku;
        this.productype = productype;
        this.quantity = quantity;
        this.productname = productname;
        this.overview = overview;
        this.description = description;
        this.pdfLink = pdfLink;
        this.benefits = benefits;
        //this.message = message;
        this.message = this.toString();
    }
    
    @Override
    public String toString() {
        return String.format(
                "Product[sku=%d, productname='%s', productype='%s', quantity='%s']",
                sku, productname, productype, quantity);
    }
    
    public String getMessage() {
		return message;
	}

	public long getSku() {
        return sku;
    }

    public String getProductype() {
        return productype;
    }

	public String getQuantity() {
		return quantity;
	}

	public String getOverview() {
		return overview;
	}

	public String getDescription() {
		return description;
	}

	public String getPdfLink() {
		return pdfLink;
	}

	public List<String> getBenefits() {
		return benefits;
	}

	public String getProductname() {
		return productname;
	}
	
}
