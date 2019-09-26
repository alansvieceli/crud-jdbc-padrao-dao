package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class App {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== Teste 1: Seller - findById(Integer id) ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n=== Teste 2: Seller - findByDepartment(Department department) ===");		
		Department dep = new Department();
		dep.setId(2);
		List<Seller> lista = sellerDao.findByDepartment(dep);
		lista.forEach(s -> {
			System.out.println(s);
		});	
		
		System.out.println("\n=== Teste 3: Seller - findAll() ===");		
		List<Seller> listaAll = sellerDao.findAll();
		listaAll.forEach(s -> {
			System.out.println(s);
		});
		
		System.out.println("\n=== Teste 4: Seller - insert(Seller seller) ===");
		seller = new Seller(0, "Rosa", "rosa.maria@empresa.com.br", new Date(), 3200.12, dep);
		sellerDao.insert(seller);
		System.out.println("Depois de Inserir: " + seller);
		
		System.out.println("\n=== Teste 5: Seller - update(Seller seller) ===");
		seller = sellerDao.findById(7);
		seller.setName(seller.getName() + " XXX ");
		sellerDao.update(seller);
		System.out.println(seller);
		
		System.out.println("\n === Teste 6: Seller - delete(Integer id) ===");
		seller = sellerDao.findById(10);
		sellerDao.deleteById(seller.getId());
		System.out.println(seller.getId() + " Foi deletado");
		
	}

}
