package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class UsersTabController {
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, Void> actionsColumn;
    @FXML
    private TableView<User> tableView;

    public void initialize() {
        var users = UserPersistence.listUsers();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {
                    private final Button openButton = new Button("Apri");
                    private final Button deactivateButton = new Button("Disattiva");
                    private final Button activateButton = new Button("Attiva");
                    private final HBox pane = new HBox(10, openButton, deactivateButton, activateButton);

                    {
                        pane.setAlignment(Pos.CENTER);

                        openButton.setOnAction((ActionEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                        });

                        deactivateButton.setOnAction((ActionEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            if (!selectedUser.isActive()) {
                                return;
                            }

                            if (users.stream().filter((user) -> user.getRole() == UserRole.ADMIN).count() == 1) {
                                AlertFactory.createAlert(Alert.AlertType.ERROR, "Impossibile disattivare l'utente: è l'unico admin del sistema.").showAndWait();
                                return;
                            }

                            var result = AlertFactory.createAlert(Alert.AlertType.CONFIRMATION, "Sicuro di voler disattivare l'utente " + selectedUser.getUsername() + "?").showAndWait();
                            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                                selectedUser.setActive(!selectedUser.isActive());
                                UserPersistence.saveUser(selectedUser);
                                getTableView().getItems().set(getIndex(), selectedUser);
                            }
                        });

                        activateButton.setOnAction((ActionEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            if (selectedUser.isActive()) {
                                return;
                            }


                            selectedUser.setActive(!selectedUser.isActive());
                            UserPersistence.saveUser(selectedUser);
                            getTableView().getItems().set(getIndex(), selectedUser);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            User currentUser = getTableRow().getItem();

                            if (currentUser.isActive()) {
                                getTableRow().setOpacity(1);
                                deactivateButton.setVisible(true);
                                deactivateButton.setManaged(true);
                                activateButton.setManaged(false);
                                activateButton.setVisible(false);
                            } else {
                                getTableRow().setOpacity(0.5);
                                deactivateButton.setVisible(false);
                                deactivateButton.setManaged(false);
                                activateButton.setVisible(true);
                                activateButton.setManaged(true);
                            }

                            setGraphic(pane);
                        }
                    }
                };
            }
        };

        // Infine, assegniamo questa logica alla colonna
        actionsColumn.setCellFactory(cellFactory);

        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        tableView.setItems(userObservableList);
    }
}
