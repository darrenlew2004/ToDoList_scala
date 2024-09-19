package assignment3.darren.view

import assignment3.darren.MainApp
import assignment3.darren.model.TodoItem
import assignment3.darren.MainApp.{loader, showDialog}
import assignment3.darren.util.DateUtil.DateFormatter
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafxml.core.{DependenciesByType, FXMLView}

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.{Failure, Success, Try}
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.cell.TextFieldListCell

@sfxml
class Controller(
                  private var todoTable: TableView[TodoItem],
                  private var itemColumn: TableColumn[TodoItem, String],
                  private var detailsLabel: Label,
                  private var deadlineLabel: Label,
                  private var shortDesLabel: Label
                ) {
  todoTable.items = MainApp.todoData
  itemColumn.cellValueFactory = {_.value.shortDescription}

  private def showToDoList(todoItem: Option[TodoItem]) = {
    todoItem match {
      case Some(todoItem: TodoItem) =>
        shortDesLabel.text <== todoItem.shortDescription
        detailsLabel.text <== todoItem.details
        deadlineLabel.text = todoItem.deadline.value.asString

      case None =>
//        shortDesLabel.text.unbind()
//        detailsLabel.text.unbind()
//        deadlineLabel.text.unbind()
        shortDesLabel.text = ""
        detailsLabel.text = ""
        deadlineLabel.text = null
    }
  }

  showToDoList(None)

  todoTable.selectionModel.value.selectedItem.onChange((_, _, newValue) => {
    showToDoList(Option(newValue))
  })

  def handleDeleteTodo(action: ActionEvent): Any = {
    val selectedIndex = todoTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val removeTodo = MainApp.todoData.remove(selectedIndex)
        removeTodo.delete() match {
          case Success(x) =>
            new Alert(AlertType.Information){
              initOwner(MainApp.stage)
              title = "Successful"
              headerText = "To Do Item Removed"
              contentText = "To do item has been removed"
            }.showAndWait()
          case Failure(e) =>
            new Alert(AlertType.Error){
              initOwner(MainApp.stage)
              title = "FAILED"
              headerText = "To Do Item not remove"
              contentText = "To do item has not been removed"
            }.showAndWait()
        }
    } else {
      val alert = new Alert(AlertType.Error) {
        initOwner(MainApp.stage)
        title = "NO SELECTION"
        headerText = "No To Do Selected"
        contentText = "Select a to do item in the table"
      }.showAndWait()
    }
  }

  def handleNewTodo(action: ActionEvent) = {
    val todoItem = new TodoItem("", "")
    val okClicked = MainApp.showDialog(todoItem);
    if (okClicked) {
      todoItem.save() match {
        case Success(x) =>
          if (x >= 1) {
            MainApp.todoData += todoItem
          } else {
            new Alert(Alert.AlertType.Warning){
              initOwner(MainApp.stage)
              title = "No To Do is added to Database"
              headerText = "No To Do Added"
              contentText = "No To Do is added to database"
            }.showAndWait()
          }
        case Failure (e) =>
          new Alert(Alert.AlertType.Warning){
            initOwner(MainApp.stage)
            title = "FAILED"
            headerText = "No To Do Added"
            contentText = e.toString
          }.showAndWait()
      }
    }
  }

  def handleEditTodo(action: ActionEvent) = {
    val selectedTodo = todoTable.selectionModel().selectedItem.value
    if (selectedTodo != null) {
      val okClicked = MainApp.showDialog(selectedTodo)
      if (okClicked) {
        showToDoList(Some(selectedTodo))
        selectedTodo.save() match {
          case Success (x) =>

          case Failure (e) =>
            new Alert(Alert.AlertType.Warning){
              initOwner(MainApp.stage)
              title = "FAILED"
              headerText = "No To Do Updated!!"
              contentText = e.toString
            }.showAndWait()
        }
      }
    } else {
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "NO SELECTION"
        headerText = "No To Do Selected"
        contentText = "Select a to do item in the table"
      } showAndWait()
    }
  }
}