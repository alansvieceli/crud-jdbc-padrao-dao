package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement st = null;
		try {
			st = this.conn.prepareStatement(
					"INSERT INTO seller " + 
					"(Name, Email, BirthDate, BaseSalary, DepartmentId) " + 
					"VALUES " + 
					"(?, ?, ?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
			} else {
				throw new DbException("Erro inesperado. Nenhuma linha foi afetada");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller seller) {

		PreparedStatement st = null;
		try {
			st = this.conn.prepareStatement(
					"UPDATE seller " + 
					"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + 
					"WHERE Id = ? ");
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());			
			st.setInt(6, seller.getId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("Erro inesperado. Nenhuma linha foi afetada");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = this.conn.prepareStatement("DELETE FROM seller WHERE Id = ? ");
			st.setInt(1, id);
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("Erro inesperado. Nenhuma linha foi afetada");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = this.conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + 
				    "FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");
			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {

				Department dep = Utils.instantiateDepartment(rs);
				Seller seller = Utils.instantiateSeller(rs, dep);
				
				return seller;

			} else
				return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = this.conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + 
					"ORDER BY Name");

			rs = st.executeQuery();
			
			List<Seller> listaResult = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));
				if ( dep == null ) {
					dep = Utils.instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
				Seller seller = Utils.instantiateSeller(rs, dep);				
				listaResult.add(seller);
			}
			
			return listaResult;
			

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	
	@Override
	public List<Seller> findByDepartment(Department department) {		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = this.conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + 
					"WHERE DepartmentId = ? " + 
					"ORDER BY Name");
			st.setInt(1, department.getId());

			rs = st.executeQuery();
			
			List<Seller> listaResult = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));
				if ( dep == null ) {
					dep = Utils.instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
				Seller seller = Utils.instantiateSeller(rs, dep);				
				listaResult.add(seller);
			}
			
			return listaResult;
			

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
