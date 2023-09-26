package organizer.utils;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;

import java.util.Set;
import java.util.TreeSet;

import static organizer.utils.Constants.CONTEXT_MENU_COPY_CELL;
import static organizer.utils.Constants.CONTEXT_MENU_COPY_ROW;

public class TableviewElementUtils {

	public static void setContextMenuForTable(final TableView<?> table) {
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		MenuItem copyCellItem = new MenuItem(CONTEXT_MENU_COPY_CELL);
		copyCellItem.setOnAction(actionEvent -> copyCell(table));

		MenuItem copyRowItem = new MenuItem(CONTEXT_MENU_COPY_ROW);
		copyRowItem.setOnAction(actionEvent -> copyRow(table));

		ContextMenu menu = new ContextMenu();
		menu.getItems().addAll(copyCellItem, copyRowItem);
		table.setContextMenu(menu);

		table.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<>() {
			final KeyCombination copyCellKeyCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
			final KeyCombination copyRowKeyCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

			@Override
			public void handle(KeyEvent event) {
				if (copyCellKeyCombination.match(event)) {
					copyCell(table);
					event.consume();
				}
				if (copyRowKeyCombination.match(event)){
					copyRow(table);
					event.consume();
				}
			}
		});

	}

	private static void copyRow(TableView<?> table) {
		final Set<Integer> rows = new TreeSet<>();
		for (final TablePosition tablePosition : table.getSelectionModel().getSelectedCells()) {
			rows.add(tablePosition.getRow());
		}
		final StringBuilder clipboardString = new StringBuilder();
		boolean firstRow = true;
		for (final Integer row : rows) {
			if (!firstRow) {
				clipboardString.append('\n');
			}
			firstRow = false;
			boolean firstCol = true;
			for (final TableColumn<?, ?> column : table.getColumns()) {
				if (!firstCol) {
					clipboardString.append('\t');
				}
				firstCol = false;
				final Object cellData = column.getCellData(row);
				clipboardString.append(cellData == null ? "" : cellData.toString().replaceAll("\\u00a0", ""));
			}
		}
		final ClipboardContent content = new ClipboardContent();
		content.putString(clipboardString.toString());
		Clipboard.getSystemClipboard().setContent(content);
	}

	private static void copyCell(TableView<?> table) {
		ObservableList<TablePosition> posList = table.getSelectionModel().getSelectedCells();
		int old_row = -1;
		StringBuilder clipboardString = new StringBuilder();
		for (TablePosition p : posList) {
			int row = p.getRow();
			int col = p.getColumn();
			Object cell = table.getColumns().get(col).getCellData(row);
			if (cell == null)
				cell = "";
			if (old_row == row)
				clipboardString.append('\t');
			else if (old_row != -1)
				clipboardString.append('\n');
			String temp = cell.toString().replaceAll(" ", "").replaceAll("\\u00a0", "").trim();
			if (FormatUtils.isNumeric(temp)) {
				clipboardString.append(temp);
			} else {
				clipboardString.append(cell);
			}

			old_row = row;
		}
		final ClipboardContent content = new ClipboardContent();
		content.putString(clipboardString.toString());
		Clipboard.getSystemClipboard().setContent(content);
	}
}
