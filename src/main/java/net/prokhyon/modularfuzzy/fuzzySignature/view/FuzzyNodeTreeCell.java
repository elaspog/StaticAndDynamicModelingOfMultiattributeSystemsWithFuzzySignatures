package net.prokhyon.modularfuzzy.fuzzySignature.view;

import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode;


public class FuzzyNodeTreeCell extends TextFieldTreeCell<FuzzyNode> {

	TreeView<FuzzyNode> signatureTreeView;
	private ContextMenu rootContextMenu;

	public FuzzyNodeTreeCell() {

		rootContextMenu = new ContextMenu();
		MenuItem addMenuItem = new MenuItem("Add child node");
		MenuItem removeMenuItem = new MenuItem("Remove subtree");

		rootContextMenu.getItems().add(addMenuItem);
		rootContextMenu.getItems().add(removeMenuItem);

		addMenuItem.setOnAction(t -> {

			final TreeItem<FuzzyNode> item = getTreeItem();
			final TreeItem<FuzzyNode> parentItem = item.getParent();
			final FuzzyNode parentNode = (parentItem != null ? parentItem.getValue() : null);

			// Handling tree in fuzzy model
			final FuzzyNode fuzzyNode = new FuzzyNode("Node" , parentNode);
			if (parentNode != null)
				parentNode.getChildNodes().add(fuzzyNode);

			// Handling tree in treeView
			TreeItem newItem = new TreeItem<FuzzyNode>(fuzzyNode);
			getTreeItem().getChildren().add(newItem);
		});

		removeMenuItem.setOnAction(t -> {

			final TreeItem<FuzzyNode> item = getTreeItem();
			item.getParent().getChildren().remove(item);
			if(signatureTreeView.getRoot().getChildren().size() == 0) {  // check for empty tree
				signatureTreeView.getSelectionModel().clearSelection();
			}
			t.consume();
		});

	}

	@Override
	public void updateItem(FuzzyNode item, boolean empty) {
		super.updateItem(item, empty);

		if (!empty) {
			setContextMenu(rootContextMenu);
		}

		if (item != null) {
			String str = null;
			try {
				str = item.getName();
			} catch (Exception e) {}
			setText(str + " : ");
		} else
			setText(null);
	}

}