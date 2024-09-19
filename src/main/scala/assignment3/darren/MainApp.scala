package assignment3.darren

import assignment3.darren.model.TodoItem
import assignment3.darren.util.Database
import assignment3.darren.view.DialogController
import scalafx.stage.Modality
import scalafx.scene.Scene
import scalafx.application.JFXApp
import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.Image
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Dialog
import scalafx.stage.Stage


object MainApp extends JFXApp {
  Database.setupDB()
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  stage = new PrimaryStage {
    title = "To Do List"
    icons += new Image(getClass.getResourceAsStream("/images/todoicon.png"))
    scene = new Scene {
      root = roots
    }
  }
  val todoData = ObservableBuffer[TodoItem]()
//  todoData += TodoItem("Do laundry", "wash all my clothes from the week", "20/08/2024")
//  todoData += TodoItem("OOP assignment", "finish up assignment3 so I can pass", "20/08/2024")
//  todoData += TodoItem("Laufey Concert", "attend concert", "27/08/2024")
  todoData ++= TodoItem.getAllTodoItem

  def showMainScreen() = {
    val resource = getClass.getResource("view/MainScreen.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.BorderPane]
    this.roots.setCenter(roots)
  }

  def showWelcome(): Unit = {
    val resource = getClass.getResource("view/Welcome.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showDialog(todoItem: TodoItem): Boolean = {
    val resource = getClass.getResourceAsStream("view/Dialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[DialogController#Controller]

    val dialog = new Stage(){
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.todoItem = todoItem
    dialog.showAndWait()
    control.okClicked
  }

  showWelcome()
}
