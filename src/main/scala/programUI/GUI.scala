package programUI

import realization.list.{MyListOfArrays, SerializeList}
import realization.types.factory.FactoryUserType
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, ComboBox, TextArea, TextField}
import scalafx.scene.paint.Color.White
import scalafx.scene.text.Font
import types.usertypes.UserType

object GUI extends JFXApp3{
  var userFactory = new FactoryUserType;
  var userType = userFactory.getBuilderByTypeName("Integer")
  var list = new MyListOfArrays(64)
  var defaultType = "Integer"
  private val workWithFiles: SerializeList = new SerializeList();

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "List"
      width = 1100
      height = 800
      scene = new Scene(1000, 700) {
        fill = White

        //---------
        // Buttons
        //---------

        val findBtn = new Button("Найти")
        findBtn.layoutX = 800
        findBtn.layoutY = 200
        findBtn.setPrefWidth(140)
        findBtn.setPrefHeight(30)

        val deleteBtn = new Button("Удалить")
        deleteBtn.layoutX = 800
        deleteBtn.layoutY = 155
        deleteBtn.setPrefWidth(140)
        deleteBtn.setPrefHeight(30)

        val insertBtn = new Button("Вставить")
        insertBtn.layoutX = 800
        insertBtn.layoutY = 70
        insertBtn.setPrefWidth(140)
        insertBtn.setPrefHeight(30)

        val insertByIdBtn = new Button("Вставить")
        insertByIdBtn.layoutX = 800
        insertByIdBtn.layoutY = 110
        insertByIdBtn.setPrefWidth(140)
        insertByIdBtn.setPrefHeight(30)

        val sortBtn = new Button("Сортировать")
        sortBtn.layoutX = 650
        sortBtn.layoutY = 250
        sortBtn.setPrefWidth(290)
        sortBtn.setPrefHeight(30)

        val saveBtn = new Button("Сохранить")
        saveBtn.layoutX = 650
        saveBtn.layoutY = 610
        saveBtn.setPrefWidth(145)
        saveBtn.setPrefHeight(70)

        val loadBtn = new Button("Загрузить")
        loadBtn.layoutX = 850
        loadBtn.layoutY = 610
        loadBtn.setPrefWidth(145)
        loadBtn.setPrefHeight(70)


        //---------
        // Fields
        //---------

        val delByIdField = new TextField
        delByIdField.layoutX = 650
        delByIdField.layoutY = 155
        delByIdField.setPrefWidth(140)
        delByIdField.setPrefHeight(30)

        val insertByIdField = new TextField
        insertByIdField.layoutX = 650
        insertByIdField.layoutY = 110
        insertByIdField.setPrefWidth(140)
        insertByIdField.setPrefHeight(30)

        val findByIdField = new TextField
        findByIdField.layoutX = 650
        findByIdField.layoutY = 200
        findByIdField.setPrefWidth(140)
        findByIdField.setPrefHeight(30)

        val mainTextArea = new TextArea()
        mainTextArea.layoutX = 25
        mainTextArea.layoutY = 10
        mainTextArea.setPrefWidth(600)
        mainTextArea.setPrefHeight(680)
        mainTextArea.setEditable(false)
        mainTextArea.setFont(new Font("Arial", 12))

        val factoryListItemsComboBox = new ComboBox(userFactory.getTypeNameList)
        factoryListItemsComboBox.layoutX = 650
        factoryListItemsComboBox.layoutY = 15
        factoryListItemsComboBox.setPrefWidth(180)
        factoryListItemsComboBox.setPrefHeight(30)

        content = List(findBtn, insertBtn, insertByIdBtn, deleteBtn, saveBtn,
          sortBtn, loadBtn, delByIdField, findByIdField,
          mainTextArea, insertByIdField, factoryListItemsComboBox)

        //---------
        // Обработчики
        //---------

        factoryListItemsComboBox.onAction = (e: ActionEvent) => {
          var item = factoryListItemsComboBox.selectionModel.apply.getSelectedItem

          if (defaultType != item) {
            defaultType = item
            userType = userFactory.getBuilderByTypeName(defaultType)
            list = new MyListOfArrays(64)
            mainTextArea.setText(list.toString)
          }
        }

        insertBtn.onAction = (e: ActionEvent) => {
          list.add(userType.create)
          mainTextArea.setText(list.toString)
        }

        deleteBtn.onAction = (e: ActionEvent) => {
          if (!delByIdField.getText.isEmpty) {
            list.remove(Integer.parseInt(delByIdField.getText));
            mainTextArea.setText(list.toString)
          }
          else
            new Alert(AlertType.Information) {
              title = "Ошибка!"
              headerText = "Ошибка при удалении!"
              contentText = "Введите значение индекса, не оставляйте поле пустым!"
            }.showAndWait()
        }

        insertByIdBtn.onAction = (e: ActionEvent) => {
          if (!insertByIdField.getText.isEmpty) {
            list.insert(userType.create, insertByIdField.getText.toInt)
            mainTextArea.setText(list.toString)
          }
          else
            new Alert(AlertType.Information) {
              title = "Ошибка!"
              headerText = "Ошибка при вставке по индексу!"
              contentText = "Поле пустое!"
            }.showAndWait()
        }

        findBtn.onAction = (e: ActionEvent) => {
          if (!findBtn.getText.isEmpty) {
            val findValue = list.get(findByIdField.getText.toInt).toString
            new Alert(AlertType.Information) {
              title = "Результат поиска"
              headerText = "Результат поиска по индексу"
              contentText = "Значение = " + findValue + "\nпо индексу = " + findByIdField.getText.toInt
            }.showAndWait()
          }
          else
            new Alert(AlertType.Information) {
              title = "Ошибка!"
              headerText = "Ошибка при поиске!"
              contentText = "Поле пустое!"
            }.showAndWait()
        }

        sortBtn.onAction = (e: ActionEvent) => {
          list = list.sortFuncStyle(userType.getTypeComparator)
          mainTextArea.setText(list.toString)
        }


        // Сериализация

        saveBtn.onAction = (e: ActionEvent) => {
          val filename = userType.typeName + ".dat"

          workWithFiles.saveToFile(list, filename, userType)
          new Alert(AlertType.Information) {
            title = "ОК!"
            headerText = "Список успешно сохранен!"
            contentText = "Список сохранен в файл " + filename
          }.showAndWait()
        }

        loadBtn.onAction = (e: ActionEvent) => {
          val filename = userType.typeName + ".dat"
          list.clear()
          list = workWithFiles.loadFromFile(filename, userType)
          new Alert(AlertType.Information) {
            title = "ОК!"
            headerText = "Список успешно загружен!"
            contentText = "Список загружен из файла " + filename
          }.showAndWait()
          mainTextArea.setText(list.toString)
        }

      }
    }

  }
}
