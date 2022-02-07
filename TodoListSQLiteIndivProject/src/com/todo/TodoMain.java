package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		boolean quit = false;
		//l.importCategory("todolist.txt");
		//l.importData("todolist.txt");
		Menu.displaymenu();
		
		do {
			Menu.prompt();
			String choice = sc.next();
			String keyword= sc.nextLine().trim();
			
			switch (choice) {

			case "add":
				TodoUtil.createItem(l);
				break;
			
			case "del":
				TodoUtil.deleteItem(l);
				break;
				
			case "edit":
				TodoUtil.updateItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l);
				break;

			case "ls_name":
				System.out.println("'List sorted by title'\n");
				TodoUtil.listAll(l, "title", 1);
				break;
			
			case "ls_name_desc":
				System.out.println("'List sorted by title in reverse order'\n");
				TodoUtil.listAll(l, "title", 0);
				break;
				
			case "ls_date":
				System.out.println("'List sorted by date'\n");
				TodoUtil.listAll(l, "due_date", 1);
				break;
			
			case "ls_date_desc":
				System.out.println("'List sorted by date in reverse order'\n");
				TodoUtil.listAll(l, "due_date", 0);
				break;
				
			case "find":
				System.out.println("'Find items that include the above keyword in either the title or the description'\n");
				TodoUtil.findList(l,keyword);
				break;
			
			case "find_cate":
				System.out.println("'Find all items whose category include keyword'\n");
				TodoUtil.findCateList(l,keyword);
				break;
			
			case "ls_cate":
				System.out.println("'Find all distinct categories'\n");
				TodoUtil.listCateAll(l);
				break;
			
			case "comp" :
				TodoUtil.completeItem(l, keyword);
				break;
				
			case "ls_comp" :
				System.out.println("'All completed items'\n");
				TodoUtil.listAll(l, 1);
				break;
			
			case "es_time":
				TodoUtil.AddEstimatedTimeInfo(l);
				break;
				
			case "help":
				Menu.displaymenu();
				break;
				
			case "exit":
				quit = true;
				break;

			default:
				System.out.println("Choose one from the commands provided!");
				System.out.println("(Need help? -help command)");
				break;
			}
		} while (!quit);
	}
}
