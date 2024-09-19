package assignment3.darren.view

import assignment3.darren.MainApp
import scalafxml.core.macros.sfxml

@sfxml
class WelcomeController(){
  def getStarted(): Unit = {
    MainApp.showMainScreen()
  }
}
