package net.prokhyon.modularfuzzy.fuzzySignature.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode;

public class FuzzySignatureEditorController implements LoadableDataController {

	@FXML
	TreeView<FuzzyNode> signatureTreeView;

	@FXML
	Button createSignatureButton;

	@FXML
	Button clearSignatureButton;

	@FXML
	Button saveSignatureButton;

	private int createdSignatureCounter;
	private int latestCreatedSignatureCounterValue;
	private int createdNodeCounter;

	@FXML
	private void initialize() {

		createdSignatureCounter = 0;
		latestCreatedSignatureCounterValue = 0;
	}

	public int getNextNodeCounterValue(){

		if (createdSignatureCounter == latestCreatedSignatureCounterValue)
			createdNodeCounter++;
		else {
			latestCreatedSignatureCounterValue = createdSignatureCounter;
			createdNodeCounter = 0;
		}
		return createdNodeCounter;
	}

	@Override
	public <T extends WorkspaceElement> void loadWithData(T modelToLoad) {

	}

	@FXML
	public void createSignatureButtonClicked(){

		createdSignatureCounter++;
		TreeItem<FuzzyNode> root;

		final FuzzyNode rootNode = new FuzzyNode("fuzzySignature" + createdSignatureCounter);
		root = new TreeItem<>(rootNode);
		root.setExpanded(true);

		signatureTreeView.setCellFactory(new Callback<TreeView<FuzzyNode>, TreeCell<FuzzyNode>>() {

			@Override
			public TreeCell<FuzzyNode> call(TreeView<FuzzyNode> arg0) {
				// custom tree cell that defines a context menu for the root tree item
				return new FuzzyNodeTreeCell();
			}

		});

		signatureTreeView.setRoot(root);
	}

	@FXML
	public void clearSignatureButtonClicked(){

		signatureTreeView.setRoot(null);
	}

	private TreeItem<FuzzyNode> makeBranch(FuzzyNode fuzzyNode, TreeItem<FuzzyNode> parent) {
		TreeItem<FuzzyNode> item = new TreeItem<>(fuzzyNode);
		item.setExpanded(true);
		parent.getChildren().add(item);
		return item;
	}

}