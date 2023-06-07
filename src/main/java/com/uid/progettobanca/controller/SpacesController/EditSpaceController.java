package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.GetTransactionService;
import com.uid.progettobanca.model.services.SpaceService;
import com.uid.progettobanca.model.services.TransactionService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class EditSpaceController implements Initializable {
    @FXML
    private ImageView back;
    @FXML
    private Button editSpaceButton;
    @FXML
    private ImageView imagePicked;
    @FXML
    private TextField inputSpaceName;
    @FXML
    private FlowPane listOfImage;
    @FXML
    private Label warningName;
    private List<Image> images = ImageUtils.getAllImageOfSpecificFolder("src/main/resources/assets/images/spacesImage");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editSpaceButton.setDisable(true);
        inputSpaceName.setText(SpacesManager.getInstance().getCurrentSpace().getNome());

        //I take the current space image form the space's image's list
        imagePicked.setImage(images.stream().filter(i -> i.getUrl().contains(SpacesManager.getInstance().getCurrentSpace().getImage())).findFirst().orElse(null));

        GenericController.loadImage(back);

        SpaceFormController.checkTextField(inputSpaceName, warningName, editSpaceButton);
    }
    @FXML
    void enter(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER && !inputSpaceName.getText().isEmpty()){
            updateSpace(new ActionEvent());
        }
    }
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
    @FXML
    void updateSpace(ActionEvent event) {
        String oldName = SpacesManager.getInstance().getCurrentSpace().getNome();
        String newName = inputSpaceName.getText();
        String image = ImageUtils.getImageViewImageName(imagePicked);
        String action = "update";

        // space is a fictitious Space to store al the new information
        Space space = SpacesManager.getInstance().getCurrentSpace();
        space.setNome(newName);
        space.setImagePath(image);
        SpaceService spaceService = new SpaceService(action, space);
        spaceService.restart();
        spaceService.setOnSucceeded(e -> {

            //2  atomic integer so I can use it all around the services
            AtomicInteger count = new AtomicInteger();
            AtomicInteger size = new AtomicInteger(0);

            //start of the Service Transaction that return all the transaction
            GetTransactionService getTransactionService = new GetTransactionService("filterAllTransaction");
            getTransactionService.restart();
            getTransactionService.setOnSucceeded(e1 -> {
                if(e1.getSource().getValue() instanceof List<?> result){

                    //I cast the result into a List, this list contain all the transaction
                    List<Transazione> allTransactions = (List<Transazione>) result;

                    //Filtered list, so now I have all the transaction of the current space
                    List<Transazione> transactions = allTransactions.stream().filter(t -> t.getSpaceFrom() == SpacesManager.getInstance().getCurrentSpace().getSpaceId() || t.getSpaceTo() == SpacesManager.getInstance().getCurrentSpace().getSpaceId()).toList();

                    //set the atomic int size to act like a barrier, because we have to wait all the service that have modified the single transaction
                    size.set(transactions.size());

                    //update of the transaction
                    for(Transazione transaction : transactions){
                        transaction.setName(transaction.getName().replace(oldName, newName));
                        TransactionService transactionService = new TransactionService();
                        transactionService.setAction("update");
                        transactionService.setTransaction(transaction);

                        //start of the service that update the single transaction
                        transactionService.restart();
                        transactionService.setOnSucceeded(e2 -> {
                            if(e2.getSource().getValue() instanceof Boolean result2){

                                //if result2 is true then the service has modified correctly the transaction
                                if(result2){

                                    //increment the barrier value
                                    count.getAndIncrement();

                                    //if all the service have successfully modified the transaction we can refresh all the dynamic page inside the stack
                                    if (count.get() == size.get()) {
                                        SceneHandler.getInstance().reloadPageInHashMap(Settings.HOME_PATH + "home.fxml");
                                        SceneHandler.getInstance().reloadPageInHashMap(Settings.SPACES_PATH + "spaces.fxml");
                                        SceneHandler.getInstance().reloadPageInHashMap(Settings.SPACES_PATH + "singleSpacePage.fxml");
                                        try {
                                            BackStack.getInstance().loadPreviousPage();
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                }
                            }
                        });
                        transactionService.setOnFailed(e2 -> {
                            SceneHandler.getInstance().setPage("errorPage.fxml");
                        });
                    }
                }
            });
            getTransactionService.setOnFailed(e1 -> {
                SceneHandler.getInstance().setPage("errorPage.fxml");
            });
        });
        spaceService.setOnFailed(e -> {
            SceneHandler.getInstance().setPage("errorPage.fxml");
        });
    }
    @FXML
    void openImageList(MouseEvent event) {
        GenericController.openImageFlowPane(listOfImage, imagePicked, images);
    }
}
