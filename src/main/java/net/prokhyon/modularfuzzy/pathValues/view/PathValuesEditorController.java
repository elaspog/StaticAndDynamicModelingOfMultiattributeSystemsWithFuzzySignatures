package net.prokhyon.modularfuzzy.pathValues.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

import java.util.Map;

public class PathValuesEditorController implements LoadableDataController {

    /*
     * UI elements
     */

    @FXML
    private SplitPane verticalSplitPane;

    @FXML
    private ComboBox signatureTypeComboBox;

    /*
     * Services
     */

    private CommonServices commonServices;

    private FuzzySignatureModuleDescriptor fuzzySignatureModuleDescriptor;


    /*
     * Methods
     */

    @FXML
    private void initialize() {

        this.commonServices = new ServiceFactory().getCommonServices();

        final Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = this.commonServices.getPseudoModules();
        for (Map.Entry<Class<? extends ModuleDescriptor>, ModuleDescriptor> classModuleDescriptorEntry : pseudoModules.entrySet()) {
            final Class<? extends ModuleDescriptor> key = classModuleDescriptorEntry.getKey();
            final ModuleDescriptor value = classModuleDescriptorEntry.getValue();
            if (key == FuzzySignatureModuleDescriptor.class ) {
                final FuzzySignatureModuleDescriptor fsmdv = (FuzzySignatureModuleDescriptor) value;
                if(fsmdv.getViewName().equals("Signatures")){
                    this.fuzzySignatureModuleDescriptor = fsmdv;
                }
            }
        }

        verticalSplitPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            double height = verticalSplitPane.getHeight();
            double dividerPositionFromTop = 35;
            verticalSplitPane.setDividerPosition(0, dividerPositionFromTop/height);
        });


        signatureTypeComboBox.setCellFactory(new Callback<ListView<FuzzySignature>,ListCell<FuzzySignature>>(){

            @Override
            public ListCell<FuzzySignature> call(ListView<FuzzySignature> p) {

                final ListCell<FuzzySignature> cell = new ListCell<FuzzySignature>(){

                    @Override
                    protected void updateItem(FuzzySignature t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null)
                            setText(t.getFuzzySignatureName() + " : " + t.getUuid());
                        else
                            setText(null);
                    }
                };
                return cell;
            }
        });

        signatureTypeComboBox.setButtonCell(
                new ListCell<FuzzySignature>() {
                    @Override
                    protected void updateItem(FuzzySignature t, boolean bln) {
                        super.updateItem(t, bln);
                        if (bln) {
                            setText("");
                        } else {
                            setText(t.getFuzzySignatureName());
                        }
                    }
                });

    }

    @Override
    public <T extends WorkspaceElement> void loadWithData(T modelToLoad) {

    }

    @FXML
    private void createModel(){

    }

    @FXML
    private void clearModel(){

    }

    @FXML
    private void saveModel(){

    }

    @FXML
    private void loadActualFuzzySignatures(){

        final Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores = this.commonServices.getRegisteredStores();
        final WorkspaceInfo workspaceInfo = fuzzySignatureModuleDescriptor.getWorkspaceInfo();
        final ObservableList<? extends WorkspaceElement> workspaceElements = registeredStores.get(workspaceInfo);

        final ObservableList<FuzzySignature> fuzzySignatures = (ObservableList<FuzzySignature>) workspaceElements;
        this.signatureTypeComboBox.setItems(FXCollections.observableArrayList(fuzzySignatures));
    }

}
