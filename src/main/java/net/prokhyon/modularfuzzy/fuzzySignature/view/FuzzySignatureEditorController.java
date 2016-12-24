package net.prokhyon.modularfuzzy.fuzzySignature.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

public class FuzzySignatureEditorController implements LoadableDataController {

	@FXML
	TreeView<String> signatureTreeView;

	@FXML
	Button createSignatureButton;

	@FXML
	Button clearSignatureButton;

	@FXML
	Button saveSignatureButton;

	@FXML
	private void initialize() {

	}

	@Override
	public <T extends WorkspaceElement> void loadWithData(T modelToLoad) {

	}


	@FXML
	public void createSignatureButtonClicked(){

		TreeItem<String> root, node1, node2;

		root = new TreeItem<>("RootNode");
		root.setExpanded(true);

		node1 = makeBranch("SampleNode1", root);
		makeBranch("SampleNode1sSampleChild1", node1);
		makeBranch("SampleNode1sSampleChild2", node1);
		makeBranch("SampleNode1sSampleChild3", node1);

		node2 = makeBranch("SampleNode2", root);
		makeBranch("SampleNode2sSampleChild1", node2);
		makeBranch("SampleNode2sSampleChild1", node2);

		signatureTreeView.setRoot(root);
	}

	@FXML
	public void clearSignatureButtonClicked(){

		signatureTreeView.setRoot(null);
	}


	private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
		TreeItem<String> item = new TreeItem<>(title);
		item.setExpanded(true);
		parent.getChildren().add(item);
		return item;
	}

}