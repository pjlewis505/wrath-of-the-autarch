/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import wota.strategic.model.Hero;

/**
 *
 * @author plewis
 */
public class StringTableCell extends TableCell<Hero, String> {

    private TextField textField;

    public StringTableCell() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            if (textField == null) {
                createTextField();
            }
            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    textField.requestFocus();
                    textField.selectAll();
                }
            });
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (getItem() != null) {
            setText((String) getItem());
        }
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                    Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    commitEdit(textField.getText());
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
