package assignment3.darren.view

import assignment3.darren.model.TodoItem
import assignment3.darren.MainApp
import assignment3.darren.MainApp.todoData
import assignment3.darren.util.DateUtil.{DateFormatter, StringFormatter}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, DatePicker, TextArea, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.Includes._

import java.time.LocalDate
@sfxml
class DialogController(
                        private var shortDescriptionField: TextField,
                        private var detailsArea: TextArea,
                        private var deadlineField: TextField
                      ) {

  var dialogStage: Stage = null
  private var _todoItem: TodoItem=null
  var okClicked = false

  def todoItem = _todoItem
  def todoItem_= (x: TodoItem){
    _todoItem = x

    shortDescriptionField.text = _todoItem.shortDescription.value
    detailsArea.text = _todoItem.details.value
    deadlineField.text = _todoItem.deadline.value.asString
    deadlineField.setPromptText("dd.mm.yyyy")
  }

  def handleOk(action: ActionEvent) {
    if (isInputValid()){
      _todoItem.shortDescription <== shortDescriptionField.text
      _todoItem.details <== detailsArea.text
      _todoItem.deadline.value = deadlineField.text.value.parseLocalDate;

      okClicked = true;
      dialogStage.close()
//      todoData += _todoItem
    }
  }

  def handleCancel(action: ActionEvent){
    dialogStage.close()
  }

  def nullChecking (x: String)=x==null || x.length ==0

  def isInputValid(): Boolean = {
    var errorMessage = ""

    if (nullChecking(shortDescriptionField.text.value))
      errorMessage += "No valid short description!\n"
    if (nullChecking((detailsArea.text.value)))
      errorMessage += "No valid details\n"
    if (nullChecking(deadlineField.text.value))
      errorMessage += "No valid date, Use date format dd/mm/yyyy\n"
    else{
      if (!deadlineField.text.value.isValid){
        errorMessage += "No valid date, Use date format dd/mm/yyyy\n"
      }
    }

    if (errorMessage.length()==0){
      return true
    } else{
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()

      return false
    }
  }
}
