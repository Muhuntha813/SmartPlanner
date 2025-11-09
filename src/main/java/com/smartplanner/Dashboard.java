package com.smartplanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Dashboard {

    String name;
    String occupation;   // fetched from DB
    String experience;   // fetched from DB

    public Dashboard(String name) {
        this.name = name;
    }
    public void setProfile(String occupation,String experience){
        this.occupation=occupation;
        this.experience=experience;
    }

    private Button createPill(String text){
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(48);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0 , 10 , 0 , 10));

        String base = String.join("; ",
                "-fx-background-color: linear-gradient(to right,#ffffff,#eee6ff,#d9cffa)",
                "-fx-background-radius: 12px",
                "-fx-border-color: rgba(0,0,0,0.18)",
                "-fx-border-radius: 12px",
                "-fx-border-width: 1",
                "-fx-font-size: 14px",
                "-fx-font-weight: 700",
                "-fx-text-fill: #2c2c2c",
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 6, 0, 0, 2)"
        );
        btn.setStyle(base);

        btn.setOnMouseEntered(e -> btn.setStyle(base.replace("#eee6ff","#f4eeff")));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return  btn;
    }

    //CHECKBOX DELETE FUNCTION
    private void attachAutoSync(Task t, TaskDao dao, TaskStore store){
        // If user checks a task, delete it from DB and remove from UI
        t.doneProperty().addListener((obs, was, now) -> {
            if (Boolean.TRUE.equals(now)) {
                try {
                    dao.delete(t.getId());
                    store.remove(t);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    t.setDone(false); // revert if delete failed
                }
            }
        });
    }

    public void DashboardView(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eafafa;");

        TaskDao taskDao = new TaskDao();
        TaskStore store = TaskStore.get(); //Bringing here cuz to fetch unfinished tasks

        try {
            var loaded = taskDao.listByUser(name);    // 'name' is your username
            loaded.forEach(t -> attachAutoSync(t, taskDao, store));
            store.tasks().setAll(loaded);             // show in TableView
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //SIDE BAR CODE BELOW !! HEHE

        VBox sideBar = new VBox();
        sideBar.setPrefWidth(320);
        sideBar.setPadding(new Insets(24));
        sideBar.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #9a86f8, #5636d4);" +
                "-fx-background-radius: 0 26 26 0;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 2);"
        );
        root.setLeft(sideBar);
        sideBar.setAlignment(Pos.TOP_CENTER);  // center children horizontally
        sideBar.setFillWidth(true);

        Image imgLogo = new Image(getClass().getResource("/Images/LOGO.png").toExternalForm());

        ImageView logo = new ImageView(imgLogo);
        logo.setFitWidth(150);
        logo.setFitHeight(150);
        logo.setPreserveRatio(true);
        Circle logoClip = new Circle(75,75,75);
        logo.setClip(logoClip);

        StackPane logoHolder = new StackPane(logo);
        logoHolder.setPadding(new Insets(16, 0, 30, 0));

        String occDisplay = (occupation == null || occupation.isBlank()) ? "Occupation Unknown!" : occupation ;
        String expDisplay = (experience == null || experience.isBlank()) ? "Experience Unknown!" : "Experince " + experience + "Years";

        Button btnName = createPill(name.toUpperCase());
        Button btnCourse = createPill(occDisplay); //display from occDisplay String
        Button btnYear = createPill(expDisplay);   //display from expDisplay String
        Button btnUpcmgTsk = createPill("NA Tasks");
        FilteredList<Task> pending = new FilteredList<>(store.tasks(), t -> !t.isDone()); // Count only NOT-done tasks and bind to the pill text
        btnUpcmgTsk.textProperty().bind(Bindings.size(pending).asString("%d Tasks"));
        Button btnLogout = createPill("Log Out!");

        VBox.setMargin(btnName, new Insets(6, 0, 6, 0));
        VBox.setMargin(btnCourse, new Insets(6, 0, 6, 0));
        VBox.setMargin(btnYear, new Insets(6, 0, 6, 0));
        VBox.setMargin(btnUpcmgTsk, new Insets(6, 0, 6, 0));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideBar.getChildren().addAll(
                logoHolder,
                btnName,
                btnCourse,
                btnYear,
                btnUpcmgTsk,
                spacer,
                btnLogout);

        // TASKS CODE BELOW !!HEHE

        TextField taskTitle = new TextField();
        taskTitle.getStyleClass().add("task-input");
        taskTitle.setPromptText("Task title");

        DatePicker taskDate = new DatePicker();
        taskDate.getStyleClass().add("task-input");
        taskDate.setPromptText("Due date (optional)");

        Button addTask = new Button("Add Task");
        addTask.getStyleClass().add("add-task-btn");
        addTask.setOnAction(e->{
            String title = taskTitle.getText().trim();
            if(title.isEmpty()) return;

            try{
                //pass to add in db
                Task created = taskDao.insert(name,title,taskDate.getValue());
                //Attach sync so later changes auto-update / delete
                attachAutoSync(created,taskDao,store);
                //Add to UI
                store.add(created);
                taskTitle.clear();
                taskDate.setValue(null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });


        TableView<Task> table = new TableView<>(store.tasks());
        table.getStyleClass().add("tasks-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Task, String> cTitle = new TableColumn<>("Title");                                //title column
        cTitle.setCellValueFactory((data -> data.getValue().titleProperty())); //get title

        TableColumn<Task, java.time.LocalDate> cDue = new TableColumn<>("Due");                       //Due Column
        cDue.setCellValueFactory(data -> data.getValue().dueDateProperty());//get Due

        TableColumn<Task, Boolean> cDone = new TableColumn<>("Done");                                 //CheckBOX Column
        cDone.setCellValueFactory(data -> data.getValue().doneProperty());   //get Check
        cDone.setCellFactory(CheckBoxTableCell.forTableColumn(cDone));                                  //set checkbox

        table.getColumns().addAll(cTitle,cDue,cDone);
        table.setPrefHeight(500);
        table.setEditable(true);
        cDone.setEditable(true);


        table.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();
            MenuItem toggleDone = new MenuItem("Toggle Done");

            toggleDone.setOnAction(e -> {
                Task t = row.getItem();
                if (t != null) {
                    t.setDone(!t.isDone());
                }
            });

            menu.getItems().add(toggleDone);
            row.setContextMenu(menu);
            return row;
        });


        Button connectGoogle = new Button("Connect Google");
        Button syncAllAsTasks = new Button("Sync All as Google Tasks");
        syncAllAsTasks.setDisable(true);

        connectGoogle.setOnAction(e -> {
            try {
                GoogleOAuth.getCredential();
                connectGoogle.setText("Connected ✓");
                syncAllAsTasks.setDisable(false);
                new Alert(Alert.AlertType.INFORMATION, "Google connected.").showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Google connect failed: " + ex.getMessage()).showAndWait();
            }
        });

        syncAllAsTasks.setOnAction(e -> {
            try {
                String taskListId = GoogleTasksHttp.getDefaultTaskListId();
                int created = 0, skipped = 0, failed = 0;
                String firstError = null;

                for (Task t : TaskStore.get().tasks()) {
                    if (t.isDone()) { skipped++; continue; }
                    try {
                        GoogleTasksHttp.createTask(taskListId, t.getTitle(), "From UniSync", t.getDueDate());
                        created++;
                    } catch (Exception ex) {
                        failed++;
                        if (firstError == null) firstError = ex.getMessage();
                    }
                }

                String msg = "Google Tasks sync complete.\n" +
                        "Created: " + created + "\n" +
                        "Skipped (done): " + skipped + "\n" +
                        "Failed: " + failed;
                if (firstError != null) msg += "\n\nFirst error:\n" + firstError;

                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, msg).showAndWait();

            } catch (Exception ex) {
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                        "Sync failed early:\n" + ex.getMessage()).showAndWait();
            }
        });

        // layout the center content
        HBox inputBar = new HBox(8, taskTitle, taskDate, addTask);
        inputBar.setPadding(new Insets(12));

        table.setPrefHeight(500);
        table.setPlaceholder(new Label("No tasks yet — add one above, or sync with Google."));

        connectGoogle.getStyleClass().add("add-task-btn");
        syncAllAsTasks.getStyleClass().add("add-task-btn");

        HBox cloudBar = new HBox(8, connectGoogle, syncAllAsTasks);
        cloudBar.setPadding(new Insets(12));
        cloudBar.setAlignment(Pos.BASELINE_RIGHT); // or Pos.CENTER_RIGHT

        Label aiLabel = new Label("AI Chat");
        aiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextArea chatInput = new TextArea();
        chatInput.setPromptText("Ask something about your tasks…");
        chatInput.setWrapText(true);
        chatInput.setPrefRowCount(3);              // initial height
        VBox.setVgrow(chatInput, Priority.ALWAYS); // let it expand vertically

        Button sendBtn = new Button("Send");
        sendBtn.getStyleClass().add("add-task-btn");
        sendBtn.setOnAction(ev -> {
            String text = chatInput.getText().trim();
            if (!text.isEmpty()) {
                // TODO: hook your AI here
                System.out.println("AI prompt: " + text);
                chatInput.clear();
            }
        });

        HBox sendRow = new HBox(sendBtn);
        sendRow.setAlignment(Pos.BOTTOM_RIGHT);

        VBox aiChatPane = new VBox(8, aiLabel, chatInput, sendRow);
        aiChatPane.setPadding(new Insets(12));
        aiChatPane.setStyle(
                "-fx-background-color: #f4f4f7;" +
                        "-fx-border-color: #d7d7e0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );
// make it take remaining horizontal space
        HBox.setHgrow(aiChatPane, Priority.ALWAYS);

// put AI chat on the LEFT, cloud buttons on the RIGHT
        HBox bottomBar = new HBox(16, aiChatPane, cloudBar);
        bottomBar.setPadding(new Insets(12));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

// small right margin after the chat pane
        HBox.setMargin(aiChatPane, new Insets(0, 12, 0, 0));


        VBox content = new VBox(12, inputBar, table, bottomBar);
        content.setPadding(new Insets(20));
        root.setCenter(content);



        Scene scene = new Scene(root, 1200, 720);
        scene.getStylesheets().addAll(
                getClass().getResource("/styles/style.css").toExternalForm(),
                getClass().getResource("/styles/app.css").toExternalForm()
        );
        root.getStyleClass().add("app-root");
        stage.setScene(scene);
        stage.setTitle("Dashboard UniSync");
        stage.show();
    }
}
