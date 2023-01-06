package vttp2022.paf.assessment.eshop.respositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.Customer;
import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class CustomerRepository {

	@Autowired
	private JdbcTemplate template;

	// You cannot change the method's signature
	public Optional<Customer> findCustomerByName(String name) {
		// TODO: Task 3 
		System.out.println("Checking Customer name....");
		//Prepared statement for finding customer by the specify name
		final SqlRowSet rs = template.queryForRowSet(SQL_FIND_CUSTOMER_BY_NAME, name);
		System.out.println();
		List<Customer> result = new LinkedList<>();
		while(rs.next()){
			result.add(Customer.create(rs));
		}
		if(result.size()<1)
			return Optional.empty();

		return Optional.of(result.get(0));

	}
}
