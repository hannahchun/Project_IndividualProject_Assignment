package com.todo.menu;

public class Menu {

    public static void displaymenu()
    {
        System.out.println();
        System.out.println("<ToDoList>\n");
        System.out.println("^ User's guide ^\n");
        System.out.println("add - Add a new item");
        System.out.println("del - Delete 1 item");
        System.out.println("multi_del - Delete multiple items");
        System.out.println("edit - Update an item");
        System.out.println("ls - List all items");
        System.out.println("ls_name_asc - sort the list in standard order by title");
        System.out.println("ls_name_desc - sort the list in reverse order by title");
        System.out.println("ls_date_asc - sort the list by due_date");
        System.out.println("ls_date_desc - sort the list in reverse order by due_date");
        System.out.println("find <KEYWORD> - find all items that include KEYWORD in the title or the description");
        System.out.println("find_cate <KEYWORD> - find all items that include KEYWORD in the category");
        System.out.println("ls_cate - print non duplicate categories of the list");
        System.out.println("comp <Num> - mark 1 item (id:<Num>) completed");
        System.out.println("multi_comp <Num> - mark multiple items (id:<Num>) completed");
        System.out.println("ls_comp - print completed items");
        System.out.println("es_time - add estimated time to the item (does not modify other information)");
        System.out.println("exit(Or escape key) - end the program");
    }
    
    public static void prompt() {
    	System.out.print("\nEnter your choice >");
    }
}
