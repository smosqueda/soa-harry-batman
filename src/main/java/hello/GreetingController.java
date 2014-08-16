package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private static final String template2 = "Here is your product, %s %s!";
    private final AtomicLong counter = new AtomicLong();
     
    @Autowired 
    private ProductSearch pSearch;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    
    @RequestMapping("/product-search-by-type")
    public Product product(@RequestParam(value="productype", required=false, defaultValue="capsules") String productype, String quantity) {
    	return pSearch.lookupProductByType(productype,quantity,template2);
    	//return new Product(counter.incrementAndGet(), String.format(template2, productype), quantity);
    }
    
    @RequestMapping("/product-search-by-sku")
    public Product product(@RequestParam(value="sku", required=false, defaultValue="1") long sku) {
        return pSearch.lookupProductBySku(sku,template2);
    }
    
    @RequestMapping("/product-search-by-lookup-sku")
    public Product product(@RequestParam(value="sku", required=false, defaultValue="1") long sku, String nothing) {
        return pSearch.queryTableBySku(String.valueOf(sku));
    }
}
