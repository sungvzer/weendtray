package it.salvatoregargano.weendtray.ui;

import it.salvatoregargano.weendtray.acl.RegularUser;
import it.salvatoregargano.weendtray.acl.User;
import it.salvatoregargano.weendtray.acl.UserPersistence;
import it.salvatoregargano.weendtray.acl.UserRole;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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
                                            "Sicuro di voler disattivare l'utente " + selectedUser.getUsername() + "?")
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
}
