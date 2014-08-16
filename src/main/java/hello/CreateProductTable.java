package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class CreateProductTable {
	
	public static SimpleDriverDataSource getDatasource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:mem");
        dataSource.setPassword("");
        return dataSource;
	}
	
	public static void main(String[] args) {
        // simple DS for test (not for production!)
        SimpleDriverDataSource dataSource = getDatasource();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creating product table");
        jdbcTemplate.execute("drop table product if exists");
        jdbcTemplate.execute("create table product(" +
                "sku serial, productype varchar(255), quantity varchar(255), productname varchar(255), "
                + "overview varchar(255), description TEXT, pdfLink varchar(255), benefits Text)");
        
        List<Product> products = new ArrayList<Product>();
        List<String> benefits = new ArrayList<String>();
		benefits.add("Re-energize your life");
		benefits.add("Lose weight");
		benefits.add("Tastes great");
		benefits.add("High protein, low calorie.");
		
		List<String> benefits2 = new ArrayList<String>();
		benefits2.add("Re-balance your life");
		benefits2.add("Control your health");
		benefits2.add("Provides recommended DRA of vitamins and minerals");
		
        Product prod1 = new Product(1,"shake","55","Cleanse me now drink","Great taste and cleans your body.", 
        		"Have the shake as a meal 2 times a day for good health.", "/yaddayadda/herbalife-pdf-056.pdf", benefits, "");
        Product prod2 = new Product(3,"vitamin tablet pack","125","Vita-Good Tablets","Provides essential nutrients and vitamins for daily health.", 
        		"Take by mouth once a day for good health.", "/vitamins/herbalpack-06.pdf", benefits2, "");
        /*Product prod3 = new Product();
        Product prod4 = new Product();*/
        products.add(prod1);
        products.add(prod2);
        /*products.add(prod3);
        products.add(prod4);*/
        for (Product prod : products) {
            System.out.printf("Inserting product record for %s\n", prod.getProductname());
            String benefitString = StringUtils.join(prod.getBenefits(),"|");
            jdbcTemplate.update(
                    "INSERT INTO product(sku, productype, quantity, productname, "
                + "overview, description, pdfLink, benefits) values(?,?,?,?,?,?,?,?)",
                    prod.getSku(), prod.getProductype(), prod.getQuantity(), prod.getProductname(),
                    prod.getOverview(), prod.getDescription(), prod.getPdfLink(), benefitString );
        }
        
        //now retieve records
        getRecords(dataSource,jdbcTemplate);
	}
	
	public static void getRecords(SimpleDriverDataSource dataSource, JdbcTemplate jdbcTemplate) {
		List<Product> results = jdbcTemplate.query(
                "select * from product where sku > ?", new Object[] { 0 },
                new RowMapper<Product>() {
                    @Override
                    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                    	String benefitStr = rs.getString("benefits");
                    	List<String> benefitList = new ArrayList<String>();
                    	if (benefitStr.length()>0) {
                    		String[] benes = benefitStr.split("\\|");
                    		for (String b : benes) {
                    			benefitList.add(b);
                    		}
                    	}
                        return new Product(rs.getLong("sku"), rs.getString("productype"),
                                rs.getString("quantity"), rs.getString("productname"), rs.getString("overview"),
                                rs.getString("description"), rs.getString("pdfLink"), benefitList, "" );
                    }
                });

        for (Product product : results) {
            System.out.println(product.toString());
        }
	}
}