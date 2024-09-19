package assignment3.darren.view
import assignment3.darren.MainApp
import scalafx.scene.control.Alert
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController {
  def handleExit(): Unit = {
    scalafx.application.Platform.exit()
  }

  def handleWelcome(): Unit = {
    MainApp.showWelcome()
  }

  def handleView(): Unit = {
    MainApp.showMainScreen()
  }

  def handleAbout(): Unit = {
    val alert = new Alert(Alert.AlertType.Information){
      initOwner(MainApp.stage)
      title = "About Us"
      headerText = "This is a to do list that lets user to put in their upcoming to do. By doing so, they are able to track their timing and not miss anything in the future"
    }.showAndWait()
  }
}
