package com.todo.service;

import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList l) { 
		
		String title, desc, category, date;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n" + "[Create item]\n" + "Category > ");
		category=sc.nextLine();
		
		System.out.print("Title > ");
		title = sc.nextLine();
		if (l.isDuplicate(title)) {
			System.out.println("Duplicate titles not allowed!");
			return;
		}
		
		System.out.print("Description >");
		desc = sc.nextLine();
		
		System.out.print("Due Date >");
		date = sc.nextLine();
		
		TodoItem t = new TodoItem(title, desc, category, date);
		if(l.addItem(t)>0)
			System.out.println("\nItem added!");
	}
	
	public static void deleteItem(TodoList l) { 
		String[] nums; //번호 입력
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n" + "[Delete Item]\n" + "Number of the item to remove > ");
		nums = sc.nextLine().split(",");
		
		for(String n : nums) {
			TodoItem item = l.getItem(Integer.parseInt(n));
			if(item==null) {
				System.out.println("Non-existing item!");
				return;
			}
			System.out.println(item.toString());
		}
		
		String des; //decision
		int cnt=0;
		int len=nums.length;
		
		System.out.print("Delete item? (y/n)");
		des=sc.next(); 
		if(des.equals("y")) {
			for(String n : nums)
				cnt+=l.deleteItem(Integer.parseInt(n));
			if(cnt==len)
				System.out.println("\nItem deleted!");
		}
	}
	
	public static void updateItem(TodoList l) { 
		int index;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n" + "[Edit Item]\n" + "Number of the item to update > ");
		index = sc.nextInt();
		
		if(l.getItem(index)==null) {
			System.out.println("Non-existing item!");
			return;
		}
		
		sc.nextLine();
		System.out.print("Title of the new item > ");
		String new_title = sc.nextLine();
		
		if (l.isDuplicate(new_title)) {
			System.out.println("Existing title!");
			return;
		} 
		
		System.out.print("Category of the new item > ");
		String new_category = sc.nextLine();
		
		System.out.print("Description of the new item > ");
		String new_desc = sc.nextLine();
		
		System.out.print("Due date of the new item > ");
		String new_due_date = sc.nextLine();
		
		TodoItem t = new TodoItem(new_title, new_desc, new_category, new_due_date);
		t.setId(index);
		
		if(l.updateItem(t)>0)
			System.out.println("\nThe item is updated!");
		
	}

	public static void listAll(TodoList l) { 
		System.out.print("[Item List, ");
		System.out.println("a total of " + l.getCount() + " items]");
		for (TodoItem item : l.getList())
			System.out.println(item.toString());
	}
	
	public static void listAll(TodoList l, String orderby, int ordering) { 
		System.out.print("[Item List, ");
		System.out.println("a total of " + l.getCount() + " items]");
		for(TodoItem item : l.getOrderedList(orderby, ordering))
			System.out.println(item.toString());
	}
	
	public static void findList(TodoList l, String keyword) { 
		int cnt=0;
		for (TodoItem item : l.getList(keyword)) {
			System.out.println(item.toString());
			cnt++;
		}
		System.out.println("\nFound a total of " + cnt + " items");
	}
	
	public static void findCateList(TodoList l, String keyword) { 
		int cnt=0;
		for (TodoItem item : l.getListCategory(keyword)) {
			System.out.println(item.toString());
			cnt++;
		}
		System.out.println("\nFound a total of " + cnt + " items");
	}
	
	public static void listCateAll(TodoList l) { 
		int cnt=0;
		for(String item : l.getCategories()) {
			System.out.print(item + " ");
			cnt++;
		}
		System.out.println("\n\nFound a total of " + cnt + " categories");
	}
	
	public static void completeItem(TodoList l, String keyword) {
		String[] nums;
		nums=keyword.split(",");
		
		for(String n : nums) {
			TodoItem item = l.getItem(Integer.parseInt(n));
			if(item==null) {
				System.out.println("Non-existing item!");
				return;
			}
			System.out.println(item.toString());
		}
		
		Scanner sc = new Scanner(System.in);
		
		int cnt=0;
		int len=nums.length;
		
		for(String n : nums) {
			System.out.print("Enter the actual time taken(Format:_hrs _mins) >");
			String ac_time = sc.nextLine(); 
			cnt+=l.completeItem(Integer.parseInt(n), ac_time);
		}
		if(cnt==len)
			System.out.println("\nItem marked completed!");
	}
	
	public static void listAll(TodoList l, int comp) { 
		int cnt=0;
		for(TodoItem item : l.getList(comp)) {
			System.out.println(item.toString());
			cnt++;
		}
		System.out.println("\nA total of " + cnt + " items are completed!");
	}

	public static void AddEstimatedTimeInfo(TodoList l) {
		int idx;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n" + "[Estimated Time Info]\n" + "The item number to add estimated time information > ");
		
		idx = sc.nextInt();
		
		if(l.getItem(idx)==null) {
			System.out.println("Non-existing item!");
			return;
		}
		
		sc.nextLine();
		for(TodoItem item : l.getList()) {
			if(item.getId()==idx) {
				System.out.print("Estimated time taken(if not decided: undecided) >");
				String es_time = sc.nextLine();
				item.setEstimate(es_time);
				if(l.addEsTimeInfo(item)>0) 
					System.out.println("\nEstimated time information updated~");
				break;
			}
		}
	}

}