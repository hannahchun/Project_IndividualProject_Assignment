package com.todo.menu;

public class Menu {

    public static void displaymenu()
    {
        System.out.println();
        System.out.println("<ToDoList>");
        System.out.println("^ User's guide ^");
        System.out.println("add - Add a new item");
        System.out.println("del - Delete an existing item");
        System.out.println("edit - Update an item");
        System.out.println("ls - List all items");
        System.out.println("ls_name - Sort the list in standard order by title");
        System.out.println("ls_name_desc - Sort the list in reverse order by title");
        System.out.println("ls_date - Sort the list by date");
        System.out.println("ls_date_desc - Sort the list in reverse order by date");
        System.out.println("find (keyword) - Print all items that include the keyword in either the title or the description");
        System.out.println("find_cate (keyword) - Print all items that include the keyword in the category");
        System.out.println("ls_cate - Print all categories of the items");
        System.out.println("comp - Mark an item - 'completed'");
        System.out.println("ls_comp - Find all completed items");
        System.out.println("es_time - Add estimated time to the item (does not modify other information)");
        System.out.println("exit(Or escape key) - end the program");
    }
    
    public static void prompt() {
    	System.out.print("\nEnter your choice >");
    }
}
