package com.smartplanner;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Locale;

public class Task {
    private final LongProperty  id = new SimpleLongProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();
    private final BooleanProperty done = new SimpleBooleanProperty(false);

    //load from db rows
    public Task(long id ,String title ,LocalDate dueDate ,boolean done){
        this.id.setValue(id);
        this.title.set(title);
        this.dueDate.set(dueDate);
        this.done.setValue(done);
    }

    //before Insert
    public Task(String title, LocalDate dueDate) {
        this.title.set(title);
        this.dueDate.set(dueDate);
    }

    public long getId(){return  id.get();}
    public String getTitle(){return  title.get();}
    public LocalDate getDueDate(){return  dueDate.get();}
    public boolean isDone(){return  done.get();}

    public void setId(long v) {id.set(v);}
    public void setTitle(String v) { title.set(v); }
    public void setDueDate(LocalDate v) { dueDate.set(v); }
    public void setDone(boolean v) { done.set(v);}

    public LongProperty idProperty() {return  id;}
    public StringProperty titleProperty() {return title; }
    public ObjectProperty<LocalDate> dueDateProperty(){return dueDate; }
    public BooleanProperty doneProperty() {return done;  }

}
