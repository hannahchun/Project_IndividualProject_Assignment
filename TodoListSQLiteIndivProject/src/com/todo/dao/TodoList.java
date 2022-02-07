package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;

import com.todo.service.DbConnect;

public class TodoList {
	Connection conn;
	
	public TodoList() {
		this.conn = DbConnect.getConnection();
	}

	public int addItem(TodoItem t) {
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		PreparedStatement pstmt3;
		ResultSet rs;
		int count=0;
		
		try {
			//추가하는 데이터의 category가 category table에 있는지 확인
			boolean val;
			val=isInCategory(t.getCategory());
			if(val==false) { //카테고리가 category 테이블에 없다면
				String sql = "insert into category (name_cate)" + "values (?);";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, t.getCategory());
				pstmt.executeUpdate();
				pstmt.close();
			}
			
			//해당 category의 id 정보를 불러와
			String sql2 = "SELECT * FROM category WHERE name_cate= ?";
			pstmt2=conn.prepareStatement(sql2);
			pstmt2.setString(1, t.getCategory());
			rs = pstmt2.executeQuery();
			rs.next();
			int cat_id = rs.getInt("id");
			pstmt2.close();
			
			//list 테이블에에 최종 추가
			String sql3 = "insert into list (title, memo, current_date, due_date, category_id)"
					+ "values (?,?,?,?,?);";
			pstmt3 = conn.prepareStatement(sql3);
			pstmt3.setString(1, t.getTitle());
			pstmt3.setString(2, t.getDesc());
			pstmt3.setString(3, t.getCurrent_date());
			pstmt3.setString(4, t.getDue_date());
			pstmt3.setInt(5, cat_id);
			count=pstmt3.executeUpdate();
			pstmt3.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	private boolean isInCategory(String category) {
		ArrayList<String> cat = getCategories();
		for(String s : cat) {
			if(category.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public int deleteItem(int index) {
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int count=0;
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count=pstmt.executeUpdate();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int updateItem(TodoItem t) {
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		int count=0;
		
		try {
			String sql = "SELECT * FROM category WHERE name_cate=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getCategory());
			ResultSet rs =pstmt.executeQuery();
			rs.next();
			int cat_id = rs.getInt("id");
			pstmt.close();
			
			String sql2 = "update list set title=?, memo=?, current_date=?, due_date=?, category_id=?"
					+ "where id=?;";
			pstmt2=conn.prepareStatement(sql2);
			pstmt2.setString(1, t.getTitle());
			pstmt2.setString(2, t.getDesc());
			pstmt2.setString(3, t.getCurrent_date());
			pstmt2.setString(4, t.getDue_date());
			pstmt2.setInt(5, cat_id);
			pstmt2.setInt(6, t.getId());
			count=pstmt2.executeUpdate();
			pstmt2.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		PreparedStatement pstmt;
		ResultSet rs;
		ResultSet rs2;
		
		try {
			String sql = "SELECT *FROM list";
			stmt=conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String current_date = rs.getString("current_date");
				String due_date = rs.getString("due_date");
				int comp = rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				int cat_id = rs.getInt("category_id");
				
				String sql2 = "SELECT *FROM category WHERE id=? ";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, cat_id);
				rs2 = pstmt.executeQuery();
				String category = rs2.getString("name_cate");
								
				TodoItem t = new TodoItem(title, desc, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setComp(comp);
				t.setEstimate(es_time);
				t.setActual(as_time);
				list.add(t);
				pstmt.close();
			}
			stmt.close();
		} 
		catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int getCount() {
		Statement stmt;
		int count=0;
		
		try {
			stmt = conn.createStatement();
			String sql = "SELECT count(id) FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count=rs.getInt("count(id)");
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public TodoItem getItem(int index) {
		ArrayList <TodoItem> list = getList();
		for(TodoItem item : list) {
			if(item.getId()==index)
				return item;
		}
		return null;
	}
	
	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		PreparedStatement pstmt;
		ResultSet rs;
		ResultSet rs2;
		
		try {
			String sql = "SELECT * FROM  list ORDER BY " + orderby;
			stmt=conn.createStatement();
			if(ordering==0)
				sql+=" desc";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String current_date = rs.getString("current_date");
				String due_date = rs.getString("due_date");
				int comp = rs.getInt("is_completed");
				String es_time = rs.getString("estimated_time");
				String as_time = rs.getString("actual_time");
				int cat_id = rs.getInt("category_id");
				
				String sql2 = "SELECT * FROM category WHERE id=?";
				pstmt=conn.prepareStatement(sql2);
				pstmt.setInt(1, cat_id);
				rs2 = pstmt.executeQuery();
				String category = rs2.getString("name_cate");
				
				TodoItem t = new TodoItem(title, desc, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setComp(comp);
				t.setEstimate(es_time);
				t.setActual(as_time);
				list.add(t);
				pstmt.close();
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(String keyword){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		ResultSet rs;
		ResultSet rs2;
		
		try {
			String sql = "SELECT * FROM list WHERE title like ? or memo like ?";
			pstmt = conn.prepareStatement(sql);
			keyword = "%" + keyword + "%";
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String current_date = rs.getString("current_date");
				String due_date = rs.getString("due_date");
				int comp = rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				int cat_id = rs.getInt("category_id");
				
				String sql2 = "SELECT * FROM category WHERE id=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1,cat_id);
				rs2 = pstmt2.executeQuery();
				String category = rs2.getString("name_cate");
				
				TodoItem t = new TodoItem(title, desc, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setComp(comp);
				t.setEstimate(es_time);
				t.setActual(as_time);
				list.add(t);
				pstmt2.close();
			}
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<TodoItem> getListCategory(String keyword){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		PreparedStatement pstmt;
		ResultSet rs;
		ResultSet rs2;
		
		try {
			String sql = "SELECT * FROM list";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int comp = rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				int cat_id = rs.getInt("category_id");
				
				String sql2 = "SELECT * FROM category WHERE id=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, cat_id);
				rs2 = pstmt.executeQuery();
				String category=rs2.getString("name_cate");
				
				if(keyword.equals(category)) {
					TodoItem t = new TodoItem(title, desc, category, due_date);
					t.setId(id);
					t.setCurrent_date(current_date);
					t.setComp(comp);
					t.setEstimate(es_time);
					t.setActual(as_time);
					list.add(t);
				}
				pstmt.close();
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<String> getCategories(){
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM category";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String category = rs.getString("name_cate");
				list.add(category);
			}
			stmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int completeItem(int index, String a) {
		String sql="update list set is_completed=?, actual_time=? " + "where id=?;";
		PreparedStatement pstmt;
		int count=0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setString(2, a);
			pstmt.setInt(3, index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public ArrayList<TodoItem> getList(int c){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		ResultSet rs;
		ResultSet rs2;
		
		try {
			String sql = "SELECT * FROM list WHERE is_completed = ?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, c);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("memo");
				String current_date = rs.getString("current_date");
				String due_date = rs.getString("due_date");
				int comp = rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				int cat_id = rs.getInt("category_id");
				
				String sql2 = "SELECT * FROM category WHERE id=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, cat_id);
				rs2 = pstmt2.executeQuery();
				String category=rs2.getString("name_cate");
				
				TodoItem t = new TodoItem(title, desc, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setComp(comp);
				t.setEstimate(es_time);
				t.setActual(as_time);
				list.add(t);
				pstmt2.close();
			}
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Boolean isDuplicate(String title) {
		for (TodoItem item : getList()) {
			if (title.equals(item.getTitle())) return true;
		}
		return false;
	}
	
	public int addEsTimeInfo(TodoItem item) {
		PreparedStatement pstmt;
		int count=0;
		try {
			String sql = "update list set estimated_time=?" + "where id=?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getEstimate());
			pstmt.setInt(2, item.getId());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public void importCategory(String filename) {
		//category table에 category 정보 넣기
		Set<String> cat = new HashSet<>();
		PreparedStatement pstmt;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			
			while((line=br.readLine())!=null) {
				StringTokenizer st = new StringTokenizer(line,"##");
				String category = st.nextToken();
				cat.add(category);
			}
			br.close();
			
			String sql = "insert into category (name_cate)" + " values (?);";
			pstmt=conn.prepareStatement(sql);
			for(String s : cat) {
				pstmt.setString(1, s);
				pstmt.executeUpdate();
			}
			pstmt.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public void importData(String filename) {
		//list table에 파일 내용 담기 
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		ResultSet rs;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql = "insert into list (title, memo, current_date, due_date, category_id)" 
			+ "values (?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);

			int records=0;
			while((line=br.readLine())!=null) {
				StringTokenizer st = new StringTokenizer(line,"##");
				String category =st.nextToken();
				String title = st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				String sql2 = "SELECT * FROM category WHERE name_cate = ?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, category);
				rs = pstmt2.executeQuery();
				rs.next();
				int cat_id = rs.getInt("id");
				
				pstmt.setString(1, title);
				pstmt.setString(2, description);
				pstmt.setString(3, current_date);
				pstmt.setString(4, due_date);
				pstmt.setInt(5, cat_id);
				int count = pstmt.executeUpdate();
				if(count>0) records++;
				
				pstmt2.close();
			}
			System.out.println(records + " records read!!");
			pstmt.close();
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
}