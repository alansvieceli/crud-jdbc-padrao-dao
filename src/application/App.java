package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class App {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== Teste 1: Seller - findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n=== Teste 2: Seller - findByDepartment ===");		
		Department dep = new Department();
		dep.setId(2);
		List<Seller> lista = sellerDao.findByDepartment(dep);
		lista.forEach(s -> {
			System.out.println(s);
		});	
		
		
	}

}
