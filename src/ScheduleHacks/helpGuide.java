package ScheduleHacks;

import java.util.ArrayList;

public class helpGuide {


	ArrayList<String> addList = new ArrayList<String>();
	ArrayList<String> deleteList = new ArrayList<String>();
	ArrayList<String> editList = new ArrayList<String>();
	ArrayList<String> searchList = new ArrayList<String>();
	ArrayList<String> undoAndRedoList = new ArrayList<String>();
	ArrayList<String> completeList = new ArrayList<String>();
	ArrayList<String> exitList = new ArrayList<String>();
	ArrayList<String> blockList = new ArrayList<String>();
	ArrayList<String> collatedHelpList = new ArrayList<String>();

	
	public ArrayList<String> getCollatedList() {
		return collatedHelpList;
	}

	public void collateGuide() {
		collatedHelpList.addAll(addList);
		addList.addAll(deleteList);
		deleteList.addAll(editList);
		editList.addAll(searchList);
		searchList.addAll(undoAndRedoList);
		undoAndRedoList.addAll(completeList);
		completeList.addAll(blockList);
		blockList.addAll(exitList);
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
		completeList.add("********COMPLETE********");
		completeList.add("complete <index>");
		completeList.add("done <index>, <index>, <index> ");
		completeList.add("finish <index range>");
		completeList.add("\n");
		completeList.add("\n");
	}

	public void blockGuide() {
		searchList.add("********BLOCK********");
		searchList.add("block <index> ");
		searchList.add("block <index>, <index>, <index>");
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
