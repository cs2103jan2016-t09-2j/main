package ScheduleHacks;

import java.util.ArrayList;

public class HelpGuide {

	private static HelpGuide object;

	public HelpGuide() {
		collateGuide();
	}

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
	ArrayList<String> collatedHelpList = new ArrayList<String>();

	public ArrayList<String> getCollatedList() {
		return collatedHelpList;
	}

	public void collateGuide() {
		addGuide();
		deleteGuide();
		editGuide();
		searchGuide();
		undoAndRedoGuide();
		completeGuide();
		exitGuide();

		collatedHelpList.addAll(addList);
		collatedHelpList.addAll(deleteList);
		collatedHelpList.addAll(editList);
		collatedHelpList.addAll(undoAndRedoList);
		collatedHelpList.addAll(searchList);
		collatedHelpList.addAll(completeAndIncompleteList);
		collatedHelpList.addAll(exitList);
	}

	public void addGuide() {
		addList.add("\n");
		addList.add("   Press <Esc> to exit the Help Guide");
		addList.add("\n");
		addList.add("**********ADD**********");
		addList.add("\n");
		addList.add("   For Scheduled Task:");
		addList.add("\n");
		addList.add("       add <task details> <date> <time> ");
		addList.add("\n");
		addList.add("      <task details> <date> <time>");
		addList.add("\n");
		addList.add("   For Floating Task:");
		addList.add("\n");
		addList.add("      add <task details> ");
		addList.add("\n");
		addList.add("      <task details>");
		addList.add("\n");

	}

	public void deleteGuide() {
		deleteList.add("**********DELETE**********");
		deleteList.add("\n");
		deleteList.add("   For single delete:");
		deleteList.add("\n");
		deleteList.add("      delete <index>");
		deleteList.add("\n");
		deleteList.add("   For multiple deletions:");
		deleteList.add("\n");
		deleteList.add("      del <index>, <index>, <index> ");
		deleteList.add("\n");
		deleteList.add("      d <index range>; eg. d 3-8");
		deleteList.add("\n");
	
	}

	public void editGuide() {
		editList.add("**********EDIT**********");
		editList.add("\n");
		editList.add("      edit <number> <description> ");
		editList.add("\n");
		editList.add("      e <number>  <date>");
		editList.add("\n");
		editList.add("      edit <number> delete <date>");
		editList.add("\n");
	
	}

	public void searchGuide() {
		searchList.add("\n");
		searchList.add("**********SEARCH**********");
		searchList.add("\n");
		searchList.add("      search <category>");
		searchList.add("\n");;
		searchList.add("      view <category>");
		searchList.add("\n");
		searchList.add("      display <category>");
		searchList.add("\n");
		searchList.add("   The various categories made available");
		searchList.add("\n");
		searchList.add("   for the view command are as follows:");
		searchList.add("\n");
		searchList.add("      Today / Tomorrow/");
		searchList.add("\n");
		searchList.add("      This(or Next) Week/Month/Year");
		searchList.add("\n");
		searchList.add("      Days of the Week");
		searchList.add("\n");
		searchList.add("      Days of the Month");
		searchList.add("\n");
		searchList.add("      Keyword present in any Task");
		searchList.add("\n");
	
	}

	public void undoAndRedoGuide() {
		undoAndRedoList.add("**********UNDO**********");
		undoAndRedoList.add("\n");
		undoAndRedoList.add("      undo");
		undoAndRedoList.add("\n");
		undoAndRedoList.add("**********REDO**********");
		undoAndRedoList.add("\n");
		undoAndRedoList.add("      redo");
		undoAndRedoList.add("\n");
	
	}

	public void completeGuide() {
		completeAndIncompleteList.add("**********MARK COMPLETE**********");
		completeAndIncompleteList.add("\n");
		completeAndIncompleteList.add("      done <index>, <index>, <index> ");
		completeAndIncompleteList.add("\n");
		completeAndIncompleteList.add("      finish <index range>");
		completeAndIncompleteList.add("\n");
		completeAndIncompleteList.add("**********MARK INCOMPLETE**********");
		completeAndIncompleteList.add("\n");
		completeAndIncompleteList.add("      undone <index>, <index>, <index> ");
		completeAndIncompleteList.add("\n");
	
	}

	public void exitGuide() {
		exitList.add("**********EXIT**********");
		exitList.add("\n");
		exitList.add("      exit");
		exitList.add("\n");
		exitList.add("      quit");
		exitList.add("\n");
		exitList.add("      q");
	}

}
