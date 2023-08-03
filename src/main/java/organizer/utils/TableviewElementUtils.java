package organizer.utils;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Set;
import java.util.TreeSet;

public class TableviewElementUtils {

    public static void setContextMenuForTable(final TableView<?> table) {
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MenuItem copyCellItem = new MenuItem("Копировать ячейку");
        copyCellItem.setOnAction(actionEvent -> {
            ObservableList<TablePosition> posList = table.getSelectionModel().getSelectedCells();
            int old_row = -1;
            StringBuilder clipboardString = new StringBuilder();
            for (TablePosition p : posList) {
                int row = p.getRow();
                int col = p.getColumn();
                Object cell =  table.getColumns().get(col).getCellData(row);
                if (cell == null)
                    cell = "";
                if (old_row == row)
                    clipboardString.append('\t');
                else if (old_row != -1)
                    clipboardString.append('\n');
                String temp = cell.toString().replaceAll(" ","").replaceAll("\\u00a0","").trim();
                if (FormatUtils.isNumeric(temp)) {
                    clipboardString.append(temp);
                }else{
                    clipboardString.append(cell);
                }

                old_row = row;
            }
            final ClipboardContent content = new ClipboardContent();
            content.putString(clipboardString.toString());
            Clipboard.getSystemClipboard().setContent(content);
        });

        MenuItem copyRowItem = new MenuItem("Копировать строку");
        copyRowItem.setOnAction(actionEvent -> {
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
                    clipboardString.append(cellData == null ? "" : cellData.toString().replaceAll("\\u00a0",""));
                }
            }
            final ClipboardContent content = new ClipboardContent();
            content.putString(clipboardString.toString());
            Clipboard.getSystemClipboard().setContent(content);
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(copyCellItem, copyRowItem);
        table.setContextMenu(menu);
    }
}
