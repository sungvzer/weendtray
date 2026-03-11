package it.salvatoregargano.weendtray.ui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.telephone.billing.Wallet;
import it.salvatoregargano.weendtray.telephone.billing.WalletService;
import it.salvatoregargano.weendtray.ui.icons.IconFactory;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UsersTabController {
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, Void> actionsColumn;
    @FXML
    private TableColumn<User, String> phoneNumberColumn;
    @FXML
    private TableColumn<User, String> phonePlanColumn;
    @FXML
    private TableColumn<User, String> creditColumn;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TextField searchField;

    private ObservableList<User> userObservableList;

    public void initialize() {
        var users = UserPersistence.listUsers();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        creditColumn.setCellValueFactory(
                cellData -> {
                    if (cellData.getValue().getRole() == UserRole.ADMIN) {
                        return null;
                    }

                    try {
                        Wallet wallet = WalletService.getInstance().getWallet(cellData.getValue().getId());
                        return new ObservableValue<String>() {
                            @Override
                            public void addListener(ChangeListener<? super String> listener) {
                            }

                            @Override
                            public void removeListener(ChangeListener<? super String> listener) {
                            }

                            @Override
                            public String getValue() {
                                return String.format("%.2f €", wallet.getBalance());
                            }

                            @Override
                            public void addListener(InvalidationListener listener) {
                            }

                            @Override
                            public void removeListener(InvalidationListener listener) {
                            }
                        };
                    } catch (SQLException e) {
                        CombinedLogger.getInstance().error("Could not retrieve wallet for user "
                                + cellData.getValue().getUsername() + ": " + e.getMessage());
                        AlertFactory
                                .createAlert(Alert.AlertType.ERROR,
                                        "Errore non recuperabile, l'applicazione terminerà una volta chiuso questo menù.")
                                .showAndWait();
                        System.exit(1);
                    }

                    return null;
                });

        phonePlanColumn.setCellValueFactory(
                cellData -> {
                    if (cellData.getValue().getRole() == UserRole.ADMIN) {
                        return null;
                    } else {
                        RegularUser regularUser = (RegularUser) cellData.getValue();
                        String planName = regularUser.getPhonePlan().name;
                        return new ObservableValue<String>() {
                            @Override
                            public void addListener(ChangeListener<? super String> listener) {
                            }

                            @Override
                            public void removeListener(ChangeListener<? super String> listener) {
                            }

                            @Override
                            public String getValue() {
                                return planName;
                            }

                            @Override
                            public void addListener(InvalidationListener listener) {
                            }

                            @Override
                            public void removeListener(InvalidationListener listener) {
                            }
                        };
                    }
                });

        phoneNumberColumn.setCellValueFactory(
                cellData -> {
                    if (cellData.getValue().getRole() == UserRole.ADMIN) {
                        return null;
                    } else {
                        RegularUser regularUser = (RegularUser) cellData.getValue();
                        String phoneNumber = regularUser.getPhoneNumber();
                        return new ObservableValue<String>() {
                            @Override
                            public void addListener(ChangeListener<? super String> listener) {
                            }

                            @Override
                            public void removeListener(ChangeListener<? super String> listener) {
                            }

                            @Override
                            public String getValue() {
                                return phoneNumber;
                            }

                            @Override
                            public void addListener(InvalidationListener listener) {
                            }

                            @Override
                            public void removeListener(InvalidationListener listener) {
                            }
                        };
                    }

                });

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {
                    private final Button openButton = new Button("Apri");
                    private final Button deactivateButton = new Button("Disdici");
                    private final Button activateButton = new Button("Riattiva");
                    private final HBox pane = new HBox(10, openButton, deactivateButton, activateButton);

                    {
                        pane.setAlignment(Pos.CENTER);

                        ImageView openIcon = new ImageView(IconFactory.getIconWithColor("open_in_new", "#000000"));
                        openIcon.setFitWidth(16);
                        openIcon.setFitHeight(16);
                        openButton.setGraphic(openIcon);

                        ImageView deactivateIcon = new ImageView(
                                IconFactory.getIconWithColor("phone_disabled", "#FF0000"));
                        deactivateIcon.setFitWidth(16);
                        deactivateIcon.setFitHeight(16);
                        deactivateButton.setGraphic(deactivateIcon);

                        ImageView activateIcon = new ImageView(
                                IconFactory.getIconWithColor("phone_enabled", "#00FF00"));
                        activateIcon.setFitWidth(16);
                        activateIcon.setFitHeight(16);
                        activateButton.setGraphic(activateIcon);

                        openButton.setOnAction((ActionEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            try {
                                URL userInfoFXML = getClass()
                                        .getResource("/it/salvatoregargano/weendtray/UserInfo.fxml");
                                FXMLLoader loader = new FXMLLoader(userInfoFXML);
                                Parent root = loader.load();
                                UserInfoController userInfoController = loader.getController();
                                userInfoController.loadUser(selectedUser);

                                Stage userStage = new Stage();
                                userStage.setTitle("Utente: " + selectedUser.getUsername());
                                userStage.setScene(new Scene(root));
                                userStage.showAndWait();

                                userObservableList.set(userObservableList.indexOf(selectedUser),
                                        UserPersistence.getUserById(selectedUser.getId()));
                            } catch (IOException e) {
                                AlertFactory
                                        .createAlert(Alert.AlertType.ERROR,
                                                "Errore non recuperabile, l'applicazione terminerà una volta chiuso questo menù.")
                                        .showAndWait();
                                CombinedLogger.getInstance().error("Could not load user info: " + e.getMessage());
                                System.exit(1);
                            }
                        });

                        deactivateButton.setOnAction((ActionEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            if (!selectedUser.isActive()) {
                                return;
                            }

                            if (selectedUser.isAdminProperty().get()
                                    && users.stream().filter((user) -> user.getRole() == UserRole.ADMIN).count() == 1) {
                                AlertFactory
                                        .createAlert(Alert.AlertType.ERROR,
                                                "Impossibile disattivare l'utente: è l'unico admin del sistema.")
                                        .showAndWait();
                                return;
                            }

                            var result = AlertFactory
                                    .createAlert(Alert.AlertType.CONFIRMATION,
                                            "Sicuro di voler disdire l'utenza " + selectedUser.getUsername() + "?")
                                    .showAndWait();
                            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                                final var userIndex = userObservableList.indexOf(selectedUser);

                                selectedUser.setActive(!selectedUser.isActive());
                                UserPersistence.saveUser(selectedUser);
                                userObservableList.set(userIndex, selectedUser);
                            }
                        });

                        activateButton.setOnAction((ActionEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            final var userIndex = userObservableList.indexOf(selectedUser);
                            if (selectedUser.isActive()) {
                                return;
                            }

                            selectedUser.setActive(!selectedUser.isActive());
                            UserPersistence.saveUser(selectedUser);
                            userObservableList.set(userIndex, selectedUser);
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
                                deactivateButton.setVisible(!currentUser.isAdminProperty().get());
                                deactivateButton.setManaged(!currentUser.isAdminProperty().get());
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

        actionsColumn.setCellFactory(cellFactory);

        this.userObservableList = FXCollections.observableArrayList(users);
        FilteredList<User> filteredData = new FilteredList<>(userObservableList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (user.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (user.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (user.getRole() == UserRole.USER) {
                    RegularUser regularUser = (RegularUser) user;
                    if (regularUser.getPhoneNumber().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                }
                return false;
            });
        });
        tableView.setItems(filteredData);

        tableView.setRowFactory(new Callback<TableView<User>, TableRow<User>>() {

            @Override
            public TableRow<User> call(TableView<User> param) {
                final var row = new TableRow<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            return;
                        }
                        styleProperty().bind(Bindings.when(item.isAdminProperty())
                                .then("-fx-font-weight: bold; -fx-font-size: 16;")
                                .otherwise(""));
                    }
                };
                return row;
            }

        });
    }

    @FXML
    private void handleNewUser(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/salvatoregargano/weendtray/NewUser.fxml"));
            Stage newUserStage = new Stage();
            newUserStage.setTitle("Nuovo Utente");
            newUserStage.setScene(new Scene(root));
            newUserStage.setResizable(false);
            newUserStage.showAndWait();

            this.userObservableList.setAll(UserPersistence.listUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
