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
	private List<TodoItem> list;
	Connection conn;

	public TodoList() {
		this.list = new ArrayList<TodoItem>();
		this.conn = DbConnect.getConnection();
	}
	
	public int addItem(TodoItem t) {
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		PreparedStatement pstmt3;
		int count=0;
		
		try {
			boolean val;
			//이미 category 테이블에 있는 category인지 확인
			val=isInCategory(t.getCategory());
		
			if (val==false) { //카테고리가 category 테이블에 없다면
				String sql = "insert into category (name_cate)" + "values (?);";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, t.getCategory());
				pstmt.executeUpdate();
				pstmt.close();
			}
			
			//해당 카테고리의 id 정보를 불러와 
			String sql2 = "SELECT * FROM category WHERE name_cate =?";
			pstmt2= conn.prepareStatement(sql2); 
			pstmt2.setString(1, t.getCategory());
			ResultSet rs = pstmt2.executeQuery();
			rs.next();
			int id=rs.getInt("id");
			pstmt2.close();
			
			//list에 최종 추가
			String sql3 = "insert into list (title, memo, current_date, due_date, estimated_time, category_id)" + "values (?,?,?,?,?,?);";
			pstmt3 = conn.prepareStatement(sql3);
			pstmt3.setString(1, t.getTitle());
			pstmt3.setString(2, t.getDesc());
			pstmt3.setString(3, t.getCurrent_date());
			pstmt3.setString(4, t.getDue_date());
			pstmt3.setString(5, t.getEs_time());
			pstmt3.setInt(6, id);
			count = pstmt3.executeUpdate();
			pstmt3.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int deleteItem(int index) {
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count=pstmt.executeUpdate();
			pstmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int updateItem(TodoItem t) {
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		PreparedStatement pstmt3;
		
		int count=0;
		try {
			boolean val;
			//이미 category 테이블에 있는 category인지 확인
			val=isInCategory(t.getCategory());
			
			if (val==false) { //카테고리가 category 테이블에 없다면
				String sql = "insert into category (name_cate)" + "values (?);";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, t.getCategory());
				pstmt.executeUpdate();
				pstmt.close();
			}
			
			//해당 카테고리의 id 정보를 불러와 
			String sql2 = "SELECT * FROM category WHERE name_cate =?";
			pstmt2= conn.prepareStatement(sql2); 
			pstmt2.setString(1, t.getCategory());
			ResultSet rs = pstmt2.executeQuery();
			rs.next();
			int id=rs.getInt("id");
			pstmt2.close();
			
			//list에 최종 업데이트 
			String sql3 = "update list set title=?, memo=?, current_date=?, due_date=?, estimated_time=?, category_id=?" + " where id = ?;";
			pstmt3 = conn.prepareStatement(sql3);
			pstmt3.setString(1, t.getTitle());
			pstmt3.setString(2, t.getDesc());
			pstmt3.setString(3, t.getCurrent_date());
			pstmt3.setString(4, t.getDue_date());
			pstmt3.setString(5, t.getEs_time());
			pstmt3.setInt(6, id);
			pstmt3.setInt(7, t.getId());
			count = pstmt3.executeUpdate();
			pstmt3.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		PreparedStatement pstmt;
		
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				int cate_id = rs.getInt("category_id");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed=rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				
				String sql2 = "SELECT * FROM category WHERE id =?";
				pstmt=conn.prepareStatement(sql2);
				pstmt.setInt(1,cate_id);
				ResultSet rs2 = pstmt.executeQuery();
				String category=rs2.getString("name_cate");
				
				TodoItem t = new TodoItem(category,title,description, due_date,es_time);
				t.setId(id);
				t.setCurrent_date(current_date); 
				t.setIs_completed(is_completed);
				t.setAc_time(as_time);
				list.add(t);
			}
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		keyword="%"+keyword+"%";
		try {
			String sql = "SELECT * FROM List WHERE title like ? or memo like ?;";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,keyword);
			pstmt.setString(2,keyword);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				int cate_id = rs.getInt("category_id");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed=rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				
				String sql2 = "SELECT * FROM category WHERE id =?";
				pstmt2=conn.prepareStatement(sql2);
				pstmt2.setInt(1,cate_id);
				ResultSet rs2 = pstmt2.executeQuery();
				String category=rs2.getString("name_cate");
				
				TodoItem t = new TodoItem(category,title,description, due_date,es_time);
				t.setId(id);
				t.setCurrent_date(current_date); 
				t.setIs_completed(is_completed);
				t.setAc_time(as_time);
				list.add(t);
			}
			pstmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		PreparedStatement pstmt;
		
		try {
			stmt=conn.createStatement();
			String sql="SELECT * FROM list ORDER BY " + orderby;
			if(ordering==0)
				sql+=" desc";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				int cate_id = rs.getInt("category_id");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed=rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				
				String sql2 = "SELECT * FROM category WHERE id =?";
				pstmt=conn.prepareStatement(sql2);
				pstmt.setInt(1,cate_id);
				ResultSet rs2 = pstmt.executeQuery();
				String category=rs2.getString("name_cate");
				
				TodoItem t = new TodoItem(category,title,description, due_date,es_time);
				t.setId(id);
				t.setCurrent_date(current_date); 
				t.setIs_completed(is_completed);
				t.setAc_time(as_time);
				list.add(t);
			}
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<String> getCategories(){
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try {
			stmt=conn.createStatement();
			String sql = "SELECT DISTINCT name_cate FROM category";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String category = rs.getString("name_cate");
				list.add(category);
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<TodoItem> getListCategory(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		PreparedStatement pstmt3;
		
		try {
			//해당 키워드에 해당하는 id를 category 테이블에서 찾는다. 
			String sql = "SELECT * FROM category WHERE name_cate =?";
			pstmt= conn.prepareStatement(sql); 
			ResultSet rs;
			
			pstmt.setString(1, keyword);
			
			rs = pstmt.executeQuery();
			rs.next();
			int id=rs.getInt("id");
			
			//list에서 해당 키워드에 해당하는 id를 가진 항목만 선별
			String sql2 = "SELECT * FROM list WHERE category_id =?";
			pstmt2=conn.prepareStatement(sql2);
			pstmt2.setInt(1,id);
			ResultSet rs2 = pstmt2.executeQuery();
			
			while(rs2.next()) {
				int idOfList = rs2.getInt("id");
				int cate_id = rs2.getInt("category_id");
				String title = rs2.getString("title");
				String description = rs2.getString("memo");
				String due_date = rs2.getString("due_date");
				String current_date = rs2.getString("current_date");
				int is_completed=rs2.getInt("is_completed");
				String es_time=rs2.getString("estimated_time");
				String as_time=rs2.getString("actual_time");
				
				//다시 category 테이블로 이동하여 해당 category id에 해당하는 category 이름 불러와 
				String sql3 = "SELECT * FROM category WHERE id =?";
				pstmt3=conn.prepareStatement(sql3);
				pstmt3.setInt(1,cate_id);
				ResultSet rs3 = pstmt3.executeQuery();
				String category=rs3.getString("name_cate");
				
				TodoItem t = new TodoItem(category,title,description, due_date,es_time);
				t.setId(idOfList);
				t.setCurrent_date(current_date); 
				t.setIs_completed(is_completed);
				t.setAc_time(as_time);
				list.add(t);
			}
			pstmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getListCompleted() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT * FROM list WHERE is_completed =?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1,1);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				int cate_id = rs.getInt("category_id");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed=rs.getInt("is_completed");
				String es_time=rs.getString("estimated_time");
				String as_time=rs.getString("actual_time");
				
				String sql2 = "SELECT * FROM category WHERE id =?";
				pstmt=conn.prepareStatement(sql2);
				pstmt.setInt(1,cate_id);
				ResultSet rs2 = pstmt.executeQuery();
				String category=rs2.getString("name_cate");
				
				TodoItem t = new TodoItem(category,title,description, due_date,es_time);
				t.setId(id);
				t.setCurrent_date(current_date); 
				t.setIs_completed(is_completed);
				t.setAc_time(as_time);
				list.add(t);
			}
			pstmt.close();
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
			String sql="SELECT count(id) FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int completeItem(TodoItem item) {
		String sql="update list set is_completed=?, actual_time=?" + "where id=?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setString(2, item.getAc_time());
			pstmt.setInt(3, item.getId());
			count=pstmt.executeUpdate();
			pstmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public Boolean isDuplicate(String check_title) {
		PreparedStatement pstmt;
		int count=0;
		try {
			String sql = "SELECT count(id) FROM list WHERE title=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,check_title);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			count=rs.getInt("count(id)");
			pstmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		if(count!=0)
			return true;
		else
			return false;
	}
	
	//이미 category 테이블에 있는 category인지 확인하는 메소드
	public Boolean isInCategory(String check_word) {
		ArrayList<String> cateList=getCategories();
		
		int val=0;
		for(String s : cateList) {
			if(check_word.equals(s)) {
				val=1;
				break;
			}
		}
		
		if (val==0) { //카테고리가 category 테이블에 없음
			return false;
		}
		if (val==1) { //카테고리가 category 테이블에 있음
			return true;
		}
		return null;
	}
	
	public int AddEsTimeInfo(TodoItem item) {
		String sql="update list set estimated_time=?" + "where id=?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, item.getEs_time());
			pstmt.setInt(2, item.getId());
			count=pstmt.executeUpdate();
			pstmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public void importData(String filename) {
		PreparedStatement pstmt;
		PreparedStatement pstmt2;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			int records=0;
			String sql = "insert into list (title, memo, current_date, due_date, category_id)" + "values (?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			String sql2 = "SELECT * FROM category WHERE name_cate =?";
			pstmt2= conn.prepareStatement(sql2); 
			ResultSet rs;
			
			while((line=br.readLine()) !=null) {
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				pstmt.setString(1, title);
				pstmt.setString(2, description);
				pstmt.setString(3, current_date);
				pstmt.setString(4, due_date);
				
				pstmt2.setString(1, category);
				
				rs = pstmt2.executeQuery();
				rs.next();
				int id=rs.getInt("id");
				pstmt.setInt(5, id);
				
				int count = pstmt.executeUpdate();
				if(count>0) {
					records++;
				}
			}
			System.out.println(records + " records read!!");
			pstmt.close();
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void importCategory(String filename) {
		Set<String> catHashSet = new HashSet<>();
		PreparedStatement pstmt;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			while((line=br.readLine()) !=null) {
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				catHashSet.add(category);
				continue;
			}
			br.close();
			
			String sql = "insert into category (name_cate)" + "values (?);";
			pstmt=conn.prepareStatement(sql);
			
			for(String s : catHashSet) {
				pstmt.setString(1, s);
				pstmt.executeUpdate();
			}
			
			pstmt.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}