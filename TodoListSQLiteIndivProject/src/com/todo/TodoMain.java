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
		//l.importData("todolist.txt");
		Menu.displaymenu();
		
		do {
			Menu.prompt();
			String choice = sc.next();
			String keyword=sc.nextLine().trim();
			
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
			
			case "multi_del":
				TodoUtil.multiDeleteItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l);
				break;
				
			case "ls_name_asc":
				System.out.println("\n'List sorted by title'\n");
				TodoUtil.listAll(l,"title",1);
				break;

			case "ls_name_desc":
				System.out.println("\n'List sorted by title in reverse order'\n");
				TodoUtil.listAll(l,"title",0);
				break;
				
			case "ls_date_asc":
				System.out.println("\n'List sorted by date'\n");
				TodoUtil.listAll(l,"due_date",1);
				break;
			
			case "ls_date_desc":
				System.out.println("\n'List sorted by date in reverse order'\n");
				TodoUtil.listAll(l,"due_date",0);
				break;
				
			case "find":
				TodoUtil.KeyWordFind(l,keyword);
				break;
				
			case "find_cate":
				TodoUtil.KeyWordFindCate(l,keyword);
				break;
				
			case "ls_cate":
				TodoUtil.PrintCat(l);
				break;
			
			case "comp":
				TodoUtil.CompleteItem(l,Integer.parseInt(keyword));
				break;
			
			case "multi_comp":
				TodoUtil.multiCompleteItem(l,keyword.split(","));
				break;
					
			case "ls_comp":
				TodoUtil.PrintCompletedItem(l);
				break;
			
			case "es_time":
				TodoUtil.AddEstimatedTimeInfo(l);
				break;
				
			case "help":
				Menu.displaymenu();
				break;
				
			case "exit":
				System.out.println("\n'Program ended!!'\n");
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
