package hello;


import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import org.springframework.stereotype.Service;

@Service
public class ProductSearch {

	public Product lookupProductBySku(long sku, String template) {
		String msg = "";
		List benefits = new ArrayList();
		benefits.add("Re-energize your life");
		benefits.add("Lose weight");
		benefits.add("Tastes great");
		benefits.add("High protein, low calorie.");
		if (sku==1) {
			msg = String.format(template, "shake","10");
			return new Product(sku,"shake","10", "Blast of Bio-Energy","An awesome product that...",
					"Drink as a meal twice a day. It will help trim your middle", "/herbalife/labels/sku="+sku, benefits, msg);
		} else if (sku==3) {
			msg = String.format(template, "supplement","50");
			return new Product(sku,"supplement","50", "Blast of Bio-Energy","An awesome product that...",
					"Drink as a meal twice a day. It will help trim your middle", "/herbalife/labels/sku="+sku, benefits, msg);	
		} else {
			msg = String.format(template, "vitamins","30");
			return new Product(345,"vitamins","30","Blast of Bio-Energy","An awesome product that...",
					"Drink as a meal twice a day. It will help trim your middle", "/herbalife/labels/sku="+sku, benefits, msg);	
		}
	}
	
	public Product lookupProductByType(String productype, String quantity, String template) {
		String msg = "";
		List benefits = new ArrayList();
		benefits.add("Re-energize your life");
		benefits.add("Lose weight");
		benefits.add("Tastes great");
		benefits.add("High protein, low calorie.");
		
		if (productype.equals("shake")) {
			msg = String.format(template, productype,quantity);
			return new Product(33,"shake",quantity,"Blast of Bio-Energy","An awesome product that...",
					"Drink as a meal twice a day. It will help trim your middle", "/herbalife/labels/sku="+33, benefits, msg);	
		} else if (productype.equals("pills")) {
			msg = String.format(template,productype,quantity);
			return new Product(44,productype,quantity,"Blast of Bio-Energy","An awesome product that...",
					"Drink as a meal twice a day. It will help trim your middle", "/herbalife/labels/sku="+44, benefits, msg);	
		} else {
			msg = String.format(template, "vitamins",quantity);
			return new Product(55,productype,quantity,"Blast of Bio-Energy","An awesome product that...",
					"Drink as a meal twice a day. It will help trim your middle", "/herbalife/labels/sku="+55, benefits, msg);	
		}
	}
	
	public Product queryTableBySku(String sku) {
        // simple DS for test (not for production!)
		List<Product> results = null;
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:mem");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Getting product details for sku: "+sku);
        /*jdbcTemplate.execute("drop table product if exists");
        jdbcTemplate.execute("create table product(" +
                "sku serial, productype varchar(255), quantity NUMBER, productname varchar(255), "
                + "overview varchar(255), description TEXT, pdfLink varchar(255), benefits Text)");*/

        
        results = jdbcTemplate.query(
                "select * from product where sku = ?", new Object[] { sku },
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
        if (results.size()>=1) {
        	return results.get(0);
        }
        return null;
    }
}