package ScheduleHacks;

import java.util.ArrayList;


public class HelpGuide {
	
	
	private static HelpGuide object;
	
	public static HelpGuide getInstance() {
		if (object == null) {
			object = new HelpGuide();
		}
		return object;
	}

	ArrayList<String> addList = new ArrayList<String>();
	ArrayList<String> deleteList = new ArrayList<String>();
	ArrayList<String> editList = new ArrayList<String>();
	ArrayList<String> searchList = new ArrayList<String>();
	ArrayList<String> undoAndRedoList = new ArrayList<String>();
	ArrayList<String> completeAndIncompleteList = new ArrayList<String>();
	ArrayList<String> exitList = new ArrayList<String>();
	ArrayList<String> blockList = new ArrayList<String>();
	ArrayList<String> collatedHelpList = new ArrayList<String>();

	
	public ArrayList<String> getCollatedList(){
		collateGuide();
		return collatedHelpList;
	}

	public void collateGuide() {
		addGuide();
		deleteGuide();
		editGuide();
		searchGuide();
		undoAndRedoGuide();
		completeGuide();
		blockGuide();
		exitGuide();
		
		collatedHelpList.addAll(addList);
		collatedHelpList.addAll(deleteList);
		collatedHelpList.addAll(editList);
		collatedHelpList.addAll(searchList);
		collatedHelpList.addAll(undoAndRedoList);
		collatedHelpList.addAll(completeAndIncompleteList);
		collatedHelpList.addAll(blockList);
		collatedHelpList.addAll(exitList);
	}


	public void addGuide() {
		addList.add("        HELP GUIDE");
		addList.add("\n");
		addList.add("********ADD********");
		addList.add("For Scheduled Task:");
		addList.add("add <task details> <date> <time> ");
		addList.add("<task details> <date> <time>");
		addList.add("For Floating Task:");
		addList.add("add <task details> ");
		addList.add("<task details>");
		addList.add("\n");
		addList.add("\n");
	}

	public void deleteGuide() {
		deleteList.add("********DELETE********");
		deleteList.add("For single delete:");
		deleteList.add("delete <index>");
		deleteList.add("For multiple deletions:");
		deleteList.add("del <index>, <index>, <index> ");
		deleteList.add("d <index range>; eg. d 3-8");
		deleteList.add("\n");
		deleteList.add("\n");
	}

	public void editGuide() {
		editList.add("********EDIT********");
		editList.add("edit <number> <description> ");
		editList.add("e <number>  <date>");
		editList.add("change <number> <time>");
		editList.add("update <number> <time> <date>");
		editList.add("edit <number> delete <date>");
		editList.add("\n");
		editList.add("\n");
	}

	public void searchGuide() {
		searchList.add("********SEARCH********");
		searchList.add("search <category>");
		searchList.add("view <category>");
		searchList.add("display <category>");
		searchList.add("\n");
		searchList.add("The various categories made available for the view command are as follows:");
		searchList.add("Today / Tomorrow/");
		searchList.add("This(or Next) Week/Month/Year");
		searchList.add("Days of the Week");
		searchList.add("Days of the Month");
		searchList.add("Keyword present in any Task");
		searchList.add("\n");
		searchList.add("\n");
	}

	public void undoAndRedoGuide() {
		undoAndRedoList.add("********UNDO********");
		undoAndRedoList.add("undo");
		undoAndRedoList.add("********REDO********");
		undoAndRedoList.add("redo");
		undoAndRedoList.add("\n");
		undoAndRedoList.add("\n");
	}

	public void completeGuide() {
		completeAndIncompleteList.add("********COMPLETE********");
		completeAndIncompleteList.add("complete <index>");
		completeAndIncompleteList.add("done <index>, <index>, <index> ");
		completeAndIncompleteList.add("finish <index range>");
		completeAndIncompleteList.add("********INCOMPLETE********");
		completeAndIncompleteList.add("incomplete <index>");
		completeAndIncompleteList.add("undone <index>, <index>, <index> ");
		completeAndIncompleteList.add("\n");
		completeAndIncompleteList.add("\n");
	}

	public void blockGuide() {
		searchList.add("********BLOCK********");
		searchList.add("block <date> ");
		searchList.add("block <date>, <time>");
		searchList.add("********BLOCK********");
		searchList.add("unblock <date> ");
		searchList.add("unblock <date>, <time>");
		searchList.add("\n");
		searchList.add("\n");
	}

	public void exitGuide() {
		exitList.add("********EXIT********");
		exitList.add("exit");
		exitList.add("quit");
		exitList.add("q");
		exitList.add("\n");
		exitList.add("\n");
		exitList.add("        END OF GUIDE");
	}

}
